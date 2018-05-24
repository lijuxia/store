package org.ljx.entity;

import java.io.Serializable;

/**
 * 店铺
 * 包含总配中心、门店
 * Created by ljx on 2018/5/14.
 */
public class Store implements Serializable {
    /**
     * id
     */
    private int id;
    /**
     * 名称
     */
    private String name;
    /**
     * 登录名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 类型（总配中心、门店）
     */
    private byte type;
    /**
     * 状态
     */
    private byte status;
    public static final byte TYPE_DISTRIBUTION_CENTRE = 1; //总配中心
    public static final byte TYPE_STORE = 2;  //门店
    public static final byte STATUS_ON = 1;//正常
    public static final byte STATUS_OFF = 2; //关闭

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }
}
