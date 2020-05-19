package com.lxm.esaydataconnection.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ProjectName: esay-data-connection-com.lxm.esaydataconnection.model
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 10:47 2020/5/15
 * @Description:
 */
@Getter
@AllArgsConstructor
public class SqlProperties {
    private String username;
    private String password;
    private String url;
    private String driver;
}
