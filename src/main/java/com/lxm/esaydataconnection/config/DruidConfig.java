package com.lxm.esaydataconnection.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.lxm.esaydataconnection.model.SqlProperties;
import com.lxm.esaydataconnection.utils.BeanUtils;
import com.lxm.esaydataconnection.utils.MySQLUtils;
import com.lxm.esaydataconnection.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.sql.*;
import java.util.*;

/**
 * ProjectName: esay-data-connection-com.lxm.esaydataconnection.config
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 9:43 2020/5/19
 * @Description:
 */
@Configuration
public class DruidConfig {

    private final Integer INITIALSIZE = 5;
    private final Integer MINIDLE = 5;
    private final Integer MAXACTIVE = 20;
    private final Integer MAXWAIT = 60000;
    private final Integer TIMEBETWEENEVICTIONRUNSMILLIS = 50000;
    private final Long MINEVICTABLEIDLETIMEMILLIS = 30000L;
    private final String VALIDATIONQUERY = "select 'X'";
    private final String VALIDATIONQUERY_ORACLE = "SELECT 1 FROM DUAL";
    private final Boolean TESTWHILEIDLE = true;
    private final Boolean TESTONBORROW = false;
    private final Boolean TESTONRETURN = false;
    private final Boolean POOLPREPAREDSTATEMENTS = true;
    private final Integer MAXPOOLPREPAREDSTATEMENTPERCONNECTIONSIZE = 20;
    private final String FILTERS = "stat,wall,slf4j";
    private final Boolean USEGLOBALDATASOURCESTAT = true;

    @Autowired
    private BeanUtils beanUtils;

    public DruidDataSource create(SqlProperties properties, String beanName,String db) throws SQLException {
        DruidDataSource db1 = beanUtils.registDataSource(beanName);
        db1.setUrl(properties.getUrl());
        db1.setUsername(properties.getUsername());
        db1.setPassword(properties.getPassword());
        db1.setDriverClassName(properties.getDriver());
        //初始化大小，最小，最大
        db1.setInitialSize(INITIALSIZE);
        db1.setMinIdle(MINIDLE);
        db1.setMaxActive(MAXACTIVE);
        //配置获取连接等待超时的时间
        db1.setMaxWait(MAXWAIT);
        //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        db1.setTimeBetweenEvictionRunsMillis(TIMEBETWEENEVICTIONRUNSMILLIS);
        //配置一个连接在池中最小生存的时间，单位是毫秒
        db1.setMinEvictableIdleTimeMillis(MINEVICTABLEIDLETIMEMILLIS);
        //校验SQL，Oracle配置 spring.datasource.validationQuery=SELECT 1 FROM DUAL，如果不配validationQuery项，则下面三项配置无用
        if ("mysql".equals(db)){
            db1.setValidationQuery(VALIDATIONQUERY);
        }else if ("oracle".equals(db)){
            db1.setValidationQuery(VALIDATIONQUERY_ORACLE);
        }else if ("pgsql".equals(db)){
            db1.setValidationQuery(VALIDATIONQUERY);
        }

        db1.setTestWhileIdle(TESTWHILEIDLE);
        db1.setTestOnBorrow(TESTONBORROW);
        db1.setTestOnReturn(TESTONRETURN);
        //打开PSCache，并且指定每个连接上PSCache的大小
        db1.setPoolPreparedStatements(POOLPREPAREDSTATEMENTS);
        db1.setMaxPoolPreparedStatementPerConnectionSize(MAXPOOLPREPAREDSTATEMENTPERCONNECTIONSIZE);
        //配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
        db1.setFilters(FILTERS);
        //通过connectProperties属性来打开mergeSql功能；慢SQL记录
        Properties p = new Properties();
        p.setProperty("mergeSql","true");
        p.setProperty("slowSqlMillis","5000");
        db1.setConnectProperties(p);
        //池共享页面
        db1.setUseGlobalDataSourceStat(USEGLOBALDATASOURCESTAT);
        db1.init();
        return db1;
    }

    public ResponseUtils queryMysql(int page,int limit,DruidDataSource dataSource,String sql){
        ResponseUtils responseUtils = new ResponseUtils();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Integer count = 0;
        try {
            connection = dataSource.getConnection();
            if (sql.contains("select") && sql.contains("from")) {
                String countsql = "select count(1) as c from (" + sql + ") temp1";
                ps = connection.prepareStatement(countsql);
                resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    count = resultSet.getInt("c");
                }
                if (count != 0) {
                    if (!sql.contains("limit")) sql += " limit " + (page - 1) * limit + "," + limit;
                    ps = connection.prepareStatement(sql);
                    resultSet = ps.executeQuery();
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    List<String> fields = new LinkedList<>();
                    List<Map<String, String>> maps = new LinkedList<>();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        fields.add(metaData.getColumnLabel(i));
                    }
                    while (resultSet.next()) {
                        Map<String, String> map = new HashMap<>();
                        for (String field : fields) {
                            map.put(field, resultSet.getString(field));
                        }
                        maps.add(map);
                    }
                    responseUtils.setData(maps);
                    responseUtils.setFields(fields);
                    responseUtils.setCount(count);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            responseUtils.setMsg("查询失败\r\n" + e.getMessage());
            responseUtils.setCode("1");
        }
        return responseUtils;
    }




    public ResponseUtils queryOracle(DruidDataSource dataSource,String sql){
        ResponseUtils responseUtils = new ResponseUtils();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Integer count = 0;
        try {
            connection = dataSource.getConnection();
            if (sql.contains("select") && sql.contains("from")) {
                String countsql = "select count(1) as c from (" + sql + ") temp1";
                ps = connection.prepareStatement(countsql);
                resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    count = resultSet.getInt("c");
                }
                if (count != 0) {
                    ps = connection.prepareStatement(sql);
                    resultSet = ps.executeQuery();
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    List<String> fields = new LinkedList<>();
                    List<Map<String, String>> maps = new LinkedList<>();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        fields.add(metaData.getColumnLabel(i));
                    }
                    while (resultSet.next()) {
                        Map<String, String> map = new HashMap<>();
                        for (String field : fields) {
                            map.put(field, resultSet.getString(field));
                        }
                        maps.add(map);
                    }
                    responseUtils.setData(maps);
                    responseUtils.setFields(fields);
                    responseUtils.setCount(count);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            responseUtils.setMsg("查询失败\r\n" + e.getMessage());
            responseUtils.setCode("1");
        }
        return responseUtils;
    }

}
