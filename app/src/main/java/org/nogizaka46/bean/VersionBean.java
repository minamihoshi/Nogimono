package org.nogizaka46.bean;

/**
 * Created by acer on 2017/4/15.
 */

public class VersionBean {

    /**
     * versionCode : 2
     * versionName : 1.0
     * msg : fixed some bugs
     * download : https://platform.idolx46.top/resource/app-debug.apk
     */

    private int versionCode;
    private String versionName;
    private String msg;
    private String download;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }
}
