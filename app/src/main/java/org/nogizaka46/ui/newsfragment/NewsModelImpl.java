package org.nogizaka46.ui.newsfragment;

import org.nogizaka46.bean.NewBean;
import org.nogizaka46.config.Constant;
import org.nogizaka46.contract.Contract;
import org.nogizaka46.contract.IApiService;
import org.nogizaka46.http.HttpUtils;

import java.util.List;

import rx.Observable;

/**
 * Created by acer on 2017/4/5.
 */

public class NewsModelImpl implements Contract.INewsModel{


    @Override
    public void onFailed(String error) {

    }



    @Override
    public Observable<List<NewBean>> getData(String type, int page, int size) {
        IApiService retrofitInterface = HttpUtils.getInstance().getRetrofitInterface();
        if (type == Constant.TYPE_ALL){
          return retrofitInterface.getNewsBean(page,size);
        }else {
            return retrofitInterface.getNewsBean(type,page,size);
        }


    }


}
