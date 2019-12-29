package com.github.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author oldhand
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authorization {

    private String profileid;

    private String userName;

    private String job;

    private String browser;

    private String ip;

    private String address;

    private String key;

    private Date loginTime;

    private String publickey;

    private String privatekey;


}
