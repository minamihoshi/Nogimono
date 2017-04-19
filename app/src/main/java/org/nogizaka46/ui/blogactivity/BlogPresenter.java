package org.nogizaka46.ui.blogactivity;

import org.nogizaka46.bean.BlogBean;
import org.nogizaka46.contract.Contract;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by acer on 2017/4/19.
 */

public class BlogPresenter {

    private Contract.IBlogModel model ;
    private Contract.IBlogView view ;

    public BlogPresenter(Contract.IBlogView view) {
        model = new BlogModelImpl();
        this.view = view;
    }

    void getData(String name , int page , int size){
        view.onLoading();
        Observable<List<BlogBean>> observable = model.getData(name, page, size);
        observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BlogBean>>() {
                    @Override
                    public void onCompleted() {
                        view.onLoaded();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onLoadFailed("");
                    }

                    @Override
                    public void onNext(List<BlogBean> blogBeen) {
                        view.getData(blogBeen);
                    }
                });


    }
}
