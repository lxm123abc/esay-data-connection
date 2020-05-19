package com.lxm.esaydataconnection.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ProjectName: esay-data-connection-com.lxm.esaydataconnection.res
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 18:11 2020/5/14
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUtils {

    private String code = "0";
    private String msg = "";
    private Integer count = 0;
    private Object data;
    private List<String> fields;

    public static ResponseUtils success(String msg){
        return new ResponseUtils("0",msg,0,null,null);
    }
    public static ResponseUtils success(Integer count, Object data,List<String> fields){
        return new ResponseUtils("0","",count,data,fields);
    }
    public static ResponseUtils success(String msg, Integer count, Object data,List<String> fields){
        return new ResponseUtils("0",msg,count,data,fields);
    }
    public static ResponseUtils fail(String msg){
        return new ResponseUtils("1",msg,0,null,null);
    }
}
