package org.bigearpig.common;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tableId;

    private String token;

    private String userName;

    private String passWord;

    private String shortName;

    private String type;

    private String email;

    private String phone;

    private List<String> roleCodeList = new ArrayList<>();

    private List<String> permissionCodeList = new ArrayList<>();


    public static UserInfo getNoAuthUser(){
        UserInfo user = new UserInfo();

        user.setTableId(-1L);
        return user;
    }
}