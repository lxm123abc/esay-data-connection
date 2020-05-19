package com.lxm.esaydataconnection.service;

import com.lxm.esaydataconnection.model.SqlProperties;
import com.lxm.esaydataconnection.utils.ResponseUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProjectName: esay-data-connection-com.lxm.esaydataconnection.mysql
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 10:13 2020/5/15
 * @Description:
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @RequestMapping("/create")
    public ResponseUtils create(@RequestBody SqlProperties properties){
        return ResponseUtils.success("创建成功");
    }
    @RequestMapping("/del")
    public ResponseUtils del(@RequestParam("id")Integer id){
        return ResponseUtils.success("删除成功");
    }
    @RequestMapping("/update")
    public ResponseUtils update(@RequestParam("id")Integer id,@RequestBody SqlProperties properties){
        return ResponseUtils.success("修改成功");
    }
    @RequestMapping("/get")
    public ResponseUtils get(@RequestParam("page")Integer page
            ,@RequestParam("limit")Integer limit
            ,@RequestParam("sql")String sql,String beanName
            ,@RequestParam("properties") SqlProperties properties){
        return ResponseUtils.success(4,null,null);
    }

}
