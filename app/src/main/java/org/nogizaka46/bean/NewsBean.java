package org.nogizaka46.bean;

import java.util.List;

/**
 * Created by acer on 2017/4/5.
 */

public class NewsBean {


    /**
     * update : 2017/04/05 20:00
     * content : [{"id":"914091","delivery":"1491313740000","type":"blog","title":"【测试】多图","subtitle":"~","provider":"leave","summary":"图\n\n\n\n\n\n\n\n\n\n\n\n","detail":"/data/914091","preview":"/preview/article/914091"},{"id":"535734","delivery":"1491309480000","type":"blog","title":"图片测试","subtitle":"图片测试","provider":"群","summary":"图一\n\n图二\n图三\n\n图四\n\n","detail":"/data/535734","preview":"/preview/article/535734"},{"id":"981707","delivery":"1491308520000","type":"magazine","title":"面对新人类","subtitle":"21世纪出生的新加入的伙伴们","provider":"翻译by尚基（PosiPeace字幕组）×花舞（泪痣小八字幕组）","summary":"小実和花奈琳搭档，初次与三期生的中学生成员们进行对谈。一开始二人就在21世纪出生的新人面前毫不掩饰自己的震惊。对着这两个天真烂漫的人才，天生就喜欢偶像的二人也变","detail":"/data/981707","preview":"/preview/article/981707"},{"id":"193517","delivery":"1491241260000","type":"magazine","title":"可以很强[二哈]","subtitle":"コンフィデンス","provider":"日不懂语翻不会译的团长","summary":"　乃木坂46的17th single 「influencer」以首周的87.5万的销量刷新了自身的最高记录、在最新的4/3榜单上依然位居首位。2月迎来了出道5周","detail":"/data/193517","preview":"/preview/article/193517"},{"id":"137604","delivery":"1491240840000","type":"blog","title":"四月手书更新","subtitle":"#铃本美愉#","provider":"欅坂46表情管理部","summary":"四月啦！！\n说到四月\u2026是我们出道的月份！！\n四月六日是很重要的日子\n已经过去了一年了\n一年前，是充满不安的时期\n但是现在，拥有自信的地方正在逐渐增加\n今后也会尽","detail":"/data/137604","preview":"/preview/article/137604"},{"id":"621010","delivery":"1491240360000","type":"news","title":"「欅坂46 POP UP STORE」","subtitle":"#欅坂46#","provider":"欅坂46应援团","summary":"明日4/4(火)10:00からSHIBUYA109 8Fにて\n「欅坂46 POP UP STORE」がOPEN致します❗️\n是非足を運んでみてください \n\n","detail":"/data/621010","preview":"/preview/article/621010"},{"id":"818423","delivery":"1490796540000","type":"news","title":"斗鱼爱乃团测试","subtitle":"斗鱼爱乃团","provider":"lalala","summary":"\n斗鱼爱乃团字幕组\n\n\n\n","detail":"/data/818423","preview":"/preview/article/818423"}]
     */

    private String update;
    private List<ContentBean> content;

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * id : 914091
         * delivery : 1491313740000
         * type : blog
         * title : 【测试】多图
         * subtitle : ~
         * provider : leave
         * summary : 图



         * detail : /data/914091
         * preview : /preview/article/914091
         */

        private String id;
        private String delivery;
        private String type;
        private String title;
        private String subtitle;
        private String provider;
        private String summary;
        private String detail;
        private String preview;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getPreview() {
            return preview;
        }

        public void setPreview(String preview) {
            this.preview = preview;
        }
    }
}
