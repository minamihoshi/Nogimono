package org.nogizaka46.bean;

import java.util.List;

/**
 * Created by acer on 2017/4/12.
 */

public class NewBean {


    /**
     * id : 646480
     * delivery : 1491577500
     * type : news
     * title : 「Love music」
     * subtitle : miwaスタッフtwitter
     * provider : 日不懂语翻不会译的团长
     * summary : miwaスタッフ【公式】@miwastaff 今晚23時在富士台的「Love music」中、将会和播放乃木坂46的各位合唱「結-ゆい-」！
     * detail : /data/646480
     * view : /preview/article/646480
     * withpic : [{"image":"https://platform.idolx46.top/photo/646480/587454e923f04c96ed61cf5bfba280d5.jpg"}]
     */

    private String id;
    private int delivery;
    private String type;
    private String title;
    private String subtitle;
    private String provider;
    private String summary;
    private String detail;
    private String view;
    private List<WithpicBean> withpic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
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

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public List<WithpicBean> getWithpic() {
        return withpic;
    }

    public void setWithpic(List<WithpicBean> withpic) {
        this.withpic = withpic;
    }

    public static class WithpicBean {
        /**
         * image : https://platform.idolx46.top/photo/646480/587454e923f04c96ed61cf5bfba280d5.jpg
         */

        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
