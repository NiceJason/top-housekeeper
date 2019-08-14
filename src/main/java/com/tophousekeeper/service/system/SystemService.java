package com.tophousekeeper.service.system;

import com.tophousekeeper.dao.function.system.NavResourceDao;
import com.tophousekeeper.entity.NavResource;
import com.tophousekeeper.system.SystemContext;
import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/5/24 8:01
 */
@Service
public class SystemService {

    @Autowired
    private NavResourceDao navResourceDao;
    @Autowired
    private SystemContext systemContext;

    public Example getSimpleExample(String condition, String conditionValue, Class clazz) {
        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();
        criteria.orCondition(condition, conditionValue);
        return example;
    }

    /**
     * json格式返回，最外层是jsonArray，包裹一个目录,目录下面有
     *
     * @return
     */
    public String getNavegationURLs() {

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
            try (Connection connection = systemContext.getConnection()) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            NavResource item = new NavResource();
                            item.setId(resultSet.getString(1));
                            item.setName(resultSet.getString(2));
                            item.setContent(resultSet.getString(3));
                            item.setType(resultSet.getString(4));

                            JSONObject jsonItem = new JSONObject();
                            jsonItem.put("itemName",item.getName());
                            jsonItem.put("itemSplit",item.getValue(NavResource.KEY_SPLIT));
                            jsonItem.put("itemHref",item.getValue(NavResource.KEY_HREF));
                            itemsArray.add(jsonItem);
                        }
                    }
                }
            } catch (SQLException e) {
                throw new SystemException(SystemStaticValue.DATASOURCE_EXCEPTION, e.getMessage());
            }
            jsonCatalog.put("items", itemsArray);
            catalogsArray.add(jsonCatalog);
        }
        return catalogsArray.toString();
    }
}
