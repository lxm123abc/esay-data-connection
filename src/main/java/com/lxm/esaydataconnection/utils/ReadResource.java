package com.lxm.esaydataconnection.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * ProjectName: esay-data-connection-com.lxm.esaydataconnection.utils
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 10:30 2020/5/15
 * @Description:
 */
public class ReadResource {

    private ReadResource(){}

    public static Map<String,String> read(String path){
        Map<String,String> map = new HashMap<>();
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            Resource resource = new ClassPathResource(path);
            if (!resource.exists()){
                return map;
            }
            is = resource.getInputStream();
            isr= new InputStreamReader(is);
            br= new BufferedReader(isr);
            String data = null;
            while((data = br.readLine()) != null) {
                String[] s = data.split(" ");
                map.put(s[0],s[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is!=null){
                    is.close();
                }
                if (isr != null){
                    isr.close();
                }
                if (br != null){
                    br.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
