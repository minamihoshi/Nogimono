package org.nogizaka46.bean;

/**
 * Created by lnx on 2018/3/28.
 */

public class UserInfoBean {


    /**
     * email : 圈
     * phone : 135
     * nickname : ななせ
     * introduction : 更改后的自我介绍2
     * status : 0
     */

    private String email;
    private String phone;
    private String nickname;
    private String introduction;
    private int status;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
