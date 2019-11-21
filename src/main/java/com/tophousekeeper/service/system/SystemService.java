package com.tophousekeeper.service.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tophousekeeper.dao.function.system.NavResourceDao;
import com.tophousekeeper.entity.NavResource;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.running.SystemContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author NiceBin
 * @description: 1.加载导航目录
 * @date 2019/5/24 8:01
 */
@Service
public class SystemService {
    private final Logger logger = LoggerFactory.getLogger(SystemService.class);

    @Autowired
    private NavResourceDao navResourceDao;
    @Autowired
    private SystemContext systemContext;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Example getSimpleExample(String condition, String conditionValue, Class clazz) {
        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();
        criteria.orCondition(condition, conditionValue);
        return example;
    }

    /**
     * json格式返回，最外层是jsonArray，包裹一个目录,目录下面有
     * @return
     */
    @Cacheable(value = "dailyCache",key = SystemStaticValue.CACHE_ID_WELCOMENAVEGATION)
    public String getNavegationURLs() {

        String navegationJson ;

        logger.info("系统加载目录");

        List<NavResource> catalogs = navResourceDao.selectByExample(
                getSimpleExample("type =",NavResource.TYPE_NAV_CATALOG, NavResource.class)
        );

        //根据目录位置进行排序

        JSONArray catalogsArray = new JSONArray();
        //解析资源
        for (int i = 0, lenI = catalogs.size(); i < lenI; i++) {

            NavResource catalog = catalogs.get(i);

            //构建导航目录Json对象
            JSONObject jsonCatalog = new JSONObject();
            jsonCatalog.put("catalogName", catalog.getName());
            jsonCatalog.put("catalogIndex",catalog.getValue(NavResource.KEY_CATALOG_INDEX));
            //构建导航内容Json对象数组，为的是内容和目录要绑定
            JSONArray itemsArray = new JSONArray();


            String[] ids = catalog.getValue(NavResource.KEY_IDS).split(",");
            StringBuilder sql = new StringBuilder("select * from t_system_resource where id = ");

            //根据id个数构建sql语句
            for (int j = 0, lenJ = ids.length; j < lenJ; j++) {
                if (j == 0) {
                    sql.append(ids[j]);
                } else {
                    sql.append(" or id = " + ids[j]);
                }
            }
            sql.append(" order by id");

            RowMapper<NavResource>  resourceRowMapper = new BeanPropertyRowMapper<>(NavResource.class);
            List<NavResource> navResources = jdbcTemplate.query(sql.toString(),resourceRowMapper);
            for(int k=0,len = navResources.size();k<len;k++){
                NavResource navResource = navResources.get(k);
                JSONObject jsonItem = new JSONObject();
                jsonItem.put("itemName",navResource.getName());
                jsonItem.put("itemSplit",navResource.getValue(NavResource.KEY_SPLIT));
                jsonItem.put("itemHref",navResource.getValue(NavResource.KEY_HREF));
                itemsArray.add(jsonItem);
            }

            jsonCatalog.put("items", itemsArray);
            catalogsArray.add(jsonCatalog);
        }
        navegationJson = catalogsArray.toString();
        return navegationJson;
    }
}
