package org.nogizaka46.ui.newsfragment;

import org.nogizaka46.bean.NewsBean;
import org.nogizaka46.contract.Contract;
import org.nogizaka46.http.HttpUtils;

import rx.Observable;

/**
 * Created by acer on 2017/4/5.
 */

public class NewsModelImpl implements Contract.INewsModel{


    @Override
    public void onFailed(String error) {

    }


    @Override
    public Observable<NewsBean> getData(String string) {
        Observable<NewsBean> observable = HttpUtils.getInstance().getRetrofitInterface().getNews(string);

        return observable;
    }


}
