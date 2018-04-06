package org.nogizaka46.bean;

/**
 * Created by lnx on 2018/4/6.
 */

public class UnreadComBean {


    /**
     * fromuser : {"id":31,"nickname":"测试","avatar":"地址"}
     * cid : 16
     * fid : 373
     * time : 10分钟前
     * message : ttttttttttt
     */

    private FromuserBean fromuser;
    private int cid;
    private int fid;
    private int fathercid ;
    private String time;
    private String message;

    public int getFathercid() {
        return fathercid;
    }

    public void setFathercid(int fathercid) {
        this.fathercid = fathercid;
    }

    public FromuserBean getFromuser() {
        return fromuser;
    }

    public void setFromuser(FromuserBean fromuser) {
        this.fromuser = fromuser;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class FromuserBean {
        /**
         * id : 31
         * nickname : 测试
         * avatar : 地址
         */

        private int id;
        private String nickname;
        private String avatar;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
