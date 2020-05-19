package com.lxm.esaydataconnection.utils;

import com.lxm.esaydataconnection.model.SqlProperties;

import java.sql.*;
import java.util.*;

/**
 * 操作MySQL的工具类
 */
public class MySQLUtils {

    private MySQLUtils() {
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection(String USERNAME, String PASSWORD, String URL, String DRIVERCLASS) {
        Connection connection = null;
        try {
            Class.forName(DRIVERCLASS);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }


    /**
     * 释放资源
     */
    public static void release(Connection connection, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询数据
     */
    public static ResponseUtils query(
            String sql, SqlProperties properties, Integer page, Integer limit) {
        ResponseUtils responseUtils = new ResponseUtils();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Integer count = 0;
        try {
            connection = MySQLUtils.getConnection(properties.getUsername(), properties.getPassword()
                    , properties.getUrl(), properties.getDriver());
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
                        fields.add(metaData.getColumnName(i));
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
        } finally {
            MySQLUtils.release(connection, ps, resultSet);
        }
        return responseUtils;
    }


    public static void main(String[] args) {
        SqlProperties root = new SqlProperties("root", "123", "jdbc:mysql://localhost:3306/testa?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC", "com.mysql.cj.jdbc.Driver");
        MySQLUtils.query("select count(1) as c from content", root, 1, 10);
    }
}
