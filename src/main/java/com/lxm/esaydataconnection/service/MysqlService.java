package com.lxm.esaydataconnection.service;

import com.lxm.esaydataconnection.model.SqlProperties;
import com.lxm.esaydataconnection.utils.ResponseUtils;

import java.sql.SQLException;

/**
 * ProjectName: esay-data-connection-com.lxm.esaydataconnection.sqlweb
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 11:54 2020/5/19
 * @Description:
 */
public interface MysqlService {
    public ResponseUtils create(SqlProperties properties);
    public ResponseUtils del(Integer id);
    public ResponseUtils update(Integer id, SqlProperties properties);
    public ResponseUtils get(Integer page, Integer limit, String sql,String beanName, SqlProperties properties) throws SQLException;
}
