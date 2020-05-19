package com.lxm.esaydataconnection.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.lxm.esaydataconnection.config.DruidConfig;
import com.lxm.esaydataconnection.model.SqlProperties;
import com.lxm.esaydataconnection.service.MysqlService;
import com.lxm.esaydataconnection.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * ProjectName: esay-data-connection-com.lxm.esaydataconnection.mysql
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 10:13 2020/5/15
 * @Description:
 */
@Service
public class MysqlServiceImpl implements MysqlService {

    private static Map<String, DruidDataSource> dataSourceMap = new HashMap();
    @Autowired
    private DruidConfig config;

    @Override
    public ResponseUtils create(SqlProperties properties) {
        return ResponseUtils.success("创建成功");
    }
    @Override
    public ResponseUtils del(Integer id) {
        return ResponseUtils.success("删除成功");
    }
    @Override
    public ResponseUtils update(Integer id, SqlProperties properties) {
        return ResponseUtils.success("修改成功");
    }
    @Override
    public ResponseUtils get(Integer page, Integer limit, String sql,String beanName, SqlProperties properties) throws SQLException {
        if (dataSourceMap.get(beanName) == null){
            DruidDataSource datasource = config.create(properties, beanName,"mysql");
            dataSourceMap.put(beanName,datasource);
        }
        return config.queryMysql(page,limit,dataSourceMap.get(beanName),sql);
    }

}
