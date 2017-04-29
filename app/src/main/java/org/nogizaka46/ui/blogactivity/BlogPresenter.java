package org.nogizaka46.ui.blogactivity;

import org.nogizaka46.bean.BlogBean;
import org.nogizaka46.contract.Contract;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
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
                        if (e instanceof HttpException) {
                            HttpException httpException = (HttpException) e;
                            //httpException.response().errorBody().string()
                            int code = httpException.code();
                            if (code == 500 ) {
                                view.onLoadFailed("服务器出错");
                            }
                            else if(code ==404){
                                view.onLoadFailed("没有更多了");
                            }
                        } else if (e instanceof ConnectException) {
                            view.onLoadFailed("网络断开,请打开网络!");
                        } else if (e instanceof SocketTimeoutException) {
                            view.onLoadFailed("网络连接超时!!");
                        } else {
                            view.onLoadFailed("网络断开" + e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(List<BlogBean> blogBeen) {
                        view.getData(blogBeen);
                    }
                });


    }
}
