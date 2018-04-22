package org.nogizaka46.bean;

import java.util.List;

/**
 * Created by lnx on 2018/4/6.
 */

public class ComFloorBean {


    /**
     * fid : 370
     * floor : {"cid":42,"time":"19小时前","floor":5,"msg":"","user":{"id":37,"nickname":"星野南","avatar":""},"child":[{"user":{"id":37,"nickname":"星野南","avatar":""},"touser":{"id":37,"nickname":"星野南","avatar":""},"cid":53,"time":"2小时前","floor":2,"msg":"哈哈"},{"user":{"id":37,"nickname":"星野南","avatar":""},"touser":null,"cid":54,"time":"2小时前","floor":3,"msg":"嗷嗷嗷"},{"user":{"id":38,"nickname":"某位鸽骑","avatar":""},"touser":null,"cid":55,"time":"2小时前","floor":4,"msg":"我要试试"},{"user":{"id":38,"nickname":"某位鸽骑","avatar":""},"touser":{"id":38,"nickname":"某位鸽骑","avatar":""},"cid":56,"time":"2小时前","floor":5,"msg":"哦哦不错"},{"user":{"id":37,"nickname":"星野南","avatar":""},"touser":{"id":38,"nickname":"某位鸽骑","avatar":""},"cid":58,"time":"2小时前","floor":6,"msg":"回复"},{"user":{"id":38,"nickname":"某位鸽骑","avatar":""},"touser":{"id":37,"nickname":"星野南","avatar":""},"cid":59,"time":"2小时前","floor":7,"msg":"哦?"}]}
     */

    private int fid;
    private FloorBean floor;

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public FloorBean getFloor() {
        return floor;
    }

    public void setFloor(FloorBean floor) {
        this.floor = floor;
    }

    public static class FloorBean {
        /**
         * cid : 42
         * time : 19小时前
         * floor : 5
         * msg :
         * user : {"id":37,"nickname":"星野南","avatar":""}
         * child : [{"user":{"id":37,"nickname":"星野南","avatar":""},"touser":{"id":37,"nickname":"星野南","avatar":""},"cid":53,"time":"2小时前","floor":2,"msg":"哈哈"},{"user":{"id":37,"nickname":"星野南","avatar":""},"touser":null,"cid":54,"time":"2小时前","floor":3,"msg":"嗷嗷嗷"},{"user":{"id":38,"nickname":"某位鸽骑","avatar":""},"touser":null,"cid":55,"time":"2小时前","floor":4,"msg":"我要试试"},{"user":{"id":38,"nickname":"某位鸽骑","avatar":""},"touser":{"id":38,"nickname":"某位鸽骑","avatar":""},"cid":56,"time":"2小时前","floor":5,"msg":"哦哦不错"},{"user":{"id":37,"nickname":"星野南","avatar":""},"touser":{"id":38,"nickname":"某位鸽骑","avatar":""},"cid":58,"time":"2小时前","floor":6,"msg":"回复"},{"user":{"id":38,"nickname":"某位鸽骑","avatar":""},"touser":{"id":37,"nickname":"星野南","avatar":""},"cid":59,"time":"2小时前","floor":7,"msg":"哦?"}]
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
             * id : 37
             * nickname : 星野南
             * avatar :
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

        public static class ChildBean {
            /**
             * user : {"id":37,"nickname":"星野南","avatar":""}
             * touser : {"id":37,"nickname":"星野南","avatar":""}
             * cid : 53
             * time : 2小时前
             * floor : 2
             * msg : 哈哈
             */


            private UserBeanX user;
            private TouserBean touser;
            private int cid;
            private String time;
            private int floor;
            private String msg;



            public UserBeanX getUser() {
                return user;
            }

            public void setUser(UserBeanX user) {
                this.user = user;
            }

            public TouserBean getTouser() {
                return touser;
            }

            public void setTouser(TouserBean touser) {
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
                 * id : 37
                 * nickname : 星野南
                 * avatar :
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

            public static class TouserBean {
                /**
                 * id : 37
                 * nickname : 星野南
                 * avatar :
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
    }
}
