package com.tophousekeeper.dao.system;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/5/24 9:23
 */
@Repository
public interface BaseDao<T> extends Mapper<T>,MySqlMapper<T> {
}
