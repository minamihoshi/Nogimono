package org.nogizaka46.contract;

import org.nogizaka46.base.IBaseModel;
import org.nogizaka46.base.IBaseView;
import org.nogizaka46.bean.NewsBean;

import rx.Observable;

/**
 * Created by acer on 2017/4/5.
 */

public class Contract {

    public interface INewsModel extends IBaseModel{
        Observable<NewsBean> getData(String string);


    }


    public interface  INewsView extends IBaseView{
        void getData(NewsBean newsBean);
    }
}
