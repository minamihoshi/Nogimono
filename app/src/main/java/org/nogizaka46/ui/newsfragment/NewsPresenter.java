package org.nogizaka46.ui.newsfragment;

import android.util.Log;

import org.nogizaka46.bean.NewBean;
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
 * Created by acer on 2017/4/5.
 */

public class NewsPresenter {

     private Contract.INewsModel model ;
    private Contract.INewsView view ;

    public NewsPresenter(Contract.INewsView view) {
        this.view =view ;
        model  = new NewsModelImpl();
    }

//    void getData(String string){
//
//        view.onLoading();
//        Observable<NewsBean> observable = model.getData(string);
//        observable.subscribeOn(Schedulers.io())
//                   .observeOn(AndroidSchedulers.mainThread())
//                   .subscribe(new Subscriber<NewsBean>() {
//                       @Override
//                       public void onCompleted() {
//                          view.onLoaded();
//                       }
//
//                       @Override
//                       public void onError(Throwable e) {
//
//                       }
//
//
//                       @Override
//                       public void onNext(NewsBean newsBean) {
//                            view.getData(newsBean);
//                       }
//                   });
//
//    }

    void getData(String type ,int page ,int size){
        Log.e("TAG", "getData: " +type +page +size);
        view.onLoading();
        Observable<List<NewBean>> observable = model.getData(type, page, size);
        observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Subscriber<List<NewBean>>() {
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
                      public void onNext(List<NewBean> newBeen) {
                          view.getData(newBeen);
                      }
                  });

    }
}
