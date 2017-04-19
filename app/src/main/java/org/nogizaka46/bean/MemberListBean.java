package org.nogizaka46.bean;

import java.io.Serializable;

/**
 * Created by acer on 2017/4/17.
 */

public class MemberListBean implements Serializable{


    /**
     * name : 秋元真夏
     * rome : akimoto-manatsu
     * portrait : http://img.nogizaka46.com/www/smph/member/img/akimotomanatsu_prof.jpg
     */

    private String name;
    private String rome;
    private String portrait;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRome() {
        return rome;
    }

    public void setRome(String rome) {
        this.rome = rome;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
