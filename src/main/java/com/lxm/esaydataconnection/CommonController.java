package com.lxm.esaydataconnection;

import com.alibaba.druid.util.StringUtils;
import com.lxm.esaydataconnection.model.SqlProperties;
import com.lxm.esaydataconnection.service.MysqlService;
import com.lxm.esaydataconnection.service.OracleService;
import com.lxm.esaydataconnection.service.PgsqlService;
import com.lxm.esaydataconnection.service.RedisController;
import com.lxm.esaydataconnection.utils.ReadResource;
import com.lxm.esaydataconnection.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.*;

/**
 * ProjectName: esay-data-connection-com.lxm.esaydataconnection
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 12:00 2020/5/15
 * @Description:
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    public static Map<String, SqlProperties> mysql = new HashMap<>();
    public static Map<String, SqlProperties> oracle = new HashMap<>();
    public static Map<String, SqlProperties> redis = new HashMap<>();
    public static Map<String, SqlProperties> pgsql = new HashMap<>();
    @Autowired
    private MysqlService mysqlService;
    @Autowired
    private OracleService oracleService;
    @Autowired
    private PgsqlService pgsqlService;
    @Autowired
    private RedisController redisController;
    @Value("${export.limit.max}")
    private Integer max;
    @PostConstruct
    public void init(){
        try {
            Map<String, String> mysql = ReadResource.read("/config/mysql.ini");
            for (String name : mysql.keySet()) {
                String cons = mysql.get(name);
                String[] split = cons.split("\\|");
                this.mysql.put(name,new SqlProperties(split[0],split[1],split[2],split[3]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        try {
            Map<String, String> oracle = ReadResource.read("/config/oracle.ini");
            for (String name : oracle.keySet()) {
                String cons = oracle.get(name);
                String[] split = cons.split("\\|");
                this.oracle.put(name,new SqlProperties(split[0],split[1],split[2],split[3]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        try {
            Map<String, String> pgsql = ReadResource.read("/config/pgsql.ini");
            for (String name : pgsql.keySet()) {
                String cons = pgsql.get(name);
                String[] split = cons.split("\\|");
                this.pgsql.put(name,new SqlProperties(split[0],split[1],split[2],split[3]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        try {
            Map<String, String> redis = ReadResource.read("/config/redis.ini");
            for (String name : redis.keySet()) {
                String cons = redis.get(name);
                String[] split = cons.split("\\|");
                this.redis.put(name,new SqlProperties(split[0],split[1],split[2],split[3]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @RequestMapping("/getTitleList")
    public ResponseUtils getTitleList(){
        Map<String,Collection<String>> result = new LinkedHashMap<>();
        result.put("mysql",mysql.keySet());
        result.put("oracle",oracle.keySet());
        result.put("redis",redis.keySet());
        result.put("pgsql",pgsql.keySet());
        return ResponseUtils.success(getSize(),result,null);
    }
    @RequestMapping("/query")
    public ResponseUtils query(@RequestParam("sql") String sql
            ,@RequestParam("type")String type
            ,@RequestParam(value = "page",defaultValue = "1")Integer page
            ,@RequestParam(value = "limit",defaultValue = "10")Integer limit) throws SQLException {
        if (StringUtils.isEmpty(sql)){
            return ResponseUtils.fail("");
        }
        String[] split = type.split(" ");
        try {
            if ("mysql".equals(split[0])){
                return mysqlService.get(page,limit,sql,split[1],mysql.get(split[1]));
            }else if ("oracle".equals((split[0]))){
                return oracleService.get(sql,split[1],oracle.get(split[1]));
            }else if ("pgsql".equals((split[0]))){
                return pgsqlService.get(sql,split[1],pgsql.get(split[1]));
            }else if ("redis".equals((split[0]))){
                redisController.get(page,limit,sql,split[1],oracle.get(split[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtils.fail("错误\r\n" + e.getMessage());
        }
        return ResponseUtils.fail("类型错误");
    }

    @RequestMapping("/export")
    public ResponseUtils export(@RequestParam("sql") String sql
            ,@RequestParam("type")String type) throws SQLException {
        return this.query(sql,type,1,max);
    }

    private Integer getSize(){
        return mysql.size()+oracle.size()+redis.size()+pgsql.size();
    }

}
