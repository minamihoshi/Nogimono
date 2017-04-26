package org.nogizaka46.ui.blogactivity;

import android.util.Log;

import org.nogizaka46.bean.BlogBean;
import org.nogizaka46.config.Constant;
import org.nogizaka46.contract.Contract;
import org.nogizaka46.contract.IApiService;
import org.nogizaka46.http.HttpUtils;

import java.util.List;

import rx.Observable;

/**
 * Created by acer on 2017/4/19.
 */

public class BlogModelImpl implements Contract.IBlogModel{


    @Override
    public Observable<List<BlogBean>> getData(String name, int page, int size) {
        IApiService retrofitInterface = HttpUtils.getInstance().getRetrofitInterface();
        if(name.equals(Constant.ALLBLOGS)){
            return  retrofitInterface.getBlogBean(page, size);

        }else {
            return  retrofitInterface.getBlogBean(name,page,size);
        }


    }

    @Override
    public void onFailed(String error) {

    }
}
