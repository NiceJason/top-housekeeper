package com.tophousekeeper.service.system;

import com.tophousekeeper.dao.function.system.SystemResourceDao;
import com.tophousekeeper.entity.SystemResource;
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
    private SystemResourceDao systemResourceDao;
    @Autowired
    private SystemContext systemContext;

    public List<SystemResource> selectResourcesByType(String type){
        Example example = new Example(SystemResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.orCondition("type = ", type);
        List<SystemResource> systemResources = systemResourceDao.selectByExample(example);
        return systemResources;
    }
    /**
     * json格式返回，最外层是jsonArray，包裹一个目录,目录下面有
     * @return
     */
    public String getNavegationURLs(){

        List<SystemResource> catalogs = selectResourcesByType(SystemStaticValue.RS_NAV_CATALOG);
        JSONArray catalogsArray = new JSONArray();
        //解析资源
        for(int i = 0,lenI = catalogs.size();i<lenI;i++){

            SystemResource catalog = catalogs.get(i);

            //构建导航目录Json对象
            JSONObject jsonCatalog = new JSONObject();
            jsonCatalog.put("catalog",JSONObject.fromObject(catalog));
            //构建导航内容Json对象数组，为的是内容和目录要绑定
            JSONArray itemsArray = new JSONArray();


            String[] ids = catalog.getContent().split(",");
            StringBuilder sql = new StringBuilder("select * from t_system_resource where id = ");

            //根据id个数构建sql语句
            for(int j=0,lenJ = ids.length;j<lenJ;j++){
                if(j==0){
                    sql.append(ids[j]);
                }else {
                    sql.append(" or id = "+ids[j]);
                }
            }
            try(Connection connection = systemContext.getConnection()) {
                try(PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())){
                    try(ResultSet resultSet = preparedStatement.executeQuery()){
                        while(resultSet.next()){
                            SystemResource item = new SystemResource();
                            item.setId(resultSet.getString(1));
                            item.setName(resultSet.getString(2));
                            item.setContent(resultSet.getString(3));
                            item.setType(resultSet.getString(4));

                            itemsArray.add(JSONObject.fromObject(item));
                        }
                    }
                }
            } catch (SQLException e) {
                throw new SystemException(SystemStaticValue.DATASOURCE_EXCEPTION,e.getMessage());
            }
            jsonCatalog.put("items",itemsArray);
            catalogsArray.add(jsonCatalog);
        }
        return catalogsArray.toString();
    }
}
