package com.lxm.esaydataconnection;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.lxm.esaydataconnection.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@SpringBootApplication
@Controller
@ServletComponentScan
public class EsayDataConnectionApplication {


    public static void main(String[] args) {
        SpringApplication.run(EsayDataConnectionApplication.class, args);
        System.out.println("项目情动成功,访问地址:http://localhost:8888");
        System.out.println("druid 监控页面:http://localhost:8888/druid/api.html");
    }

    @RequestMapping("/")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("index.html");
        return modelAndView;
    }
    @Autowired
    private BeanUtils beanUtils;

    @ResponseBody
    @RequestMapping("/a")
    public String a () throws SQLException {

        return "OK";
    }

    @ResponseBody
    @RequestMapping("/b")
    public String b () throws SQLException {
        long start = System.currentTimeMillis();
        DruidDataSource db1 = (DruidDataSource) beanUtils.getBean("db1");
        DruidPooledConnection connection = db1.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from service_spark t1,content t2,nodes t3 where t1.contentid = t2.contentid and t1.nodeid = t3.nodeid");
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString("p_date")+" "
                    +resultSet.getString("nodeid")+" "+resultSet.getString("contentid")+
                    " " + resultSet.getString("flow") +" "+ resultSet.getString("contentid")+
                    " " + resultSet.getString("contentname")+" "+ resultSet.getString("nodeid")+
                    " " + resultSet.getString("nodename"));
        }
        return String.valueOf(System.currentTimeMillis()-start);
    }

    @ResponseBody
    @RequestMapping("/c")
    public String c () throws SQLException {
        long start = System.currentTimeMillis();
        DruidDataSource db1 = (DruidDataSource) beanUtils.getBean("db1");
        DruidPooledConnection connection = db1.getConnection();
        PreparedStatement ps = connection.prepareStatement("select count(1) as c from (select t1.contentid,t2.contentname,t3.nodename from service_spark t1,content t2,nodes t3 where t1.contentid = t2.contentid and t1.nodeid = t3.nodeid) temp1");
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString("c"));
        }
        return String.valueOf(System.currentTimeMillis()-start);
    }
}
