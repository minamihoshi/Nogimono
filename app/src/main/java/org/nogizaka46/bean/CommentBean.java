package org.nogizaka46.bean;

import java.util.List;

/**
 * Created by lnx on 2018/4/5.
 */

public class CommentBean {

    @Override
    public String toString() {
        return "CommentBean{" +
                "cid=" + cid +
                ", time='" + time + '\'' +
                ", floor=" + floor +
                ", msg='" + msg + '\'' +
                ", user=" + user +
                ", child=" + child +
                '}';
    }

    /**
     * cid : 4
     * time : 1分钟前
     * floor : 4
     * msg : 评论内容
     * user : {"id":31,"nickname":"测试"}
     * child : [{"user":{"id":31,"nickname":"测试"},"touser":null,"cid":5,"time":"1分钟前","floor":1,"msg":"评论内容"},{"user":{"id":31,"nickname":"测试"},"touser":null,"cid":6,"time":"1分钟前","floor":2,"msg":"评论内容"},{"user":{"id":31,"nickname":"测试"},"touser":null,"cid":7,"time":"1分钟前","floor":3,"msg":"评论内容"},{"user":{"id":31,"nickname":"测试"},"touser":{"id":31,"nickname":"测试"},"cid":8,"time":"21秒钟前","floor":4,"msg":"评论内容"},{"user":{"id":31,"nickname":"测试"},"touser":{"id":31,"nickname":"测试"},"cid":9,"time":"19秒钟前","floor":5,"msg":"评论内容"},{"user":{"id":31,"nickname":"测试"},"touser":{"id":31,"nickname":"测试"},"cid":10,"time":"17秒钟前","floor":6,"msg":"评论内容"}]
     */

    private int cid;
    private String time;
    private int floor;
    private String msg;
    private UserBean user;
    private List<ChildBean> child;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<ChildBean> getChild() {
        return child;
    }

    public void setChild(List<ChildBean> child) {
        this.child = child;
    }

    public static class UserBean {
        /**
         * id : 31
         * nickname : 测试
         */

        private int id;
        private String nickname;
        private String avatar ;

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

    public static class ChildBean {
        /**
         * user : {"id":31,"nickname":"测试"}
         * touser : null
         * cid : 5
         * time : 1分钟前
         * floor : 1
         * msg : 评论内容
         */

        private boolean isGoMore ;
        private UserBeanX user;
        private UserBeanX touser;
        private int cid;
        private String time;
        private int floor;
        private String msg;

        public boolean isGoMore() {
            return isGoMore;
        }

        public void setGoMore(boolean goMore) {
            isGoMore = goMore;
        }

        public UserBeanX getUser() {
            return user;
        }

        public void setUser(UserBeanX user) {
            this.user = user;
        }

        public UserBeanX getTouser() {
            return touser;
        }

        public void setTouser(UserBeanX touser) {
            this.touser = touser;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getFloor() {
            return floor;
        }

        public void setFloor(int floor) {
            this.floor = floor;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public static class UserBeanX {
            /**
             * id : 31
             * nickname : 测试
             */

            private int id;
            private String nickname;
            private String avatar ;
            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getNickname() {
                return nickname;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }
    }
}
