package org.nogizaka46.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.umeng.message.common.Const;

import org.nogizaka46.R;
import org.nogizaka46.base.BaseActivity;
import org.nogizaka46.bean.ComFloorBean;
import org.nogizaka46.bean.LzyResponse;
import org.nogizaka46.config.Constant;
import org.nogizaka46.http.HttpUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommentDetailActivity extends BaseActivity {
    int cid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);

         cid = getIntent().getIntExtra(Constant.COMMENT_FATHER_ID, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        getCommentByFloor();

    }

    void getCommentByFloor(){

        HttpUtils.getInstance().getRetrofitInterface().getCommentByFloor(String.valueOf(cid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LzyResponse<ComFloorBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LzyResponse<ComFloorBean> comFloorBeanLzyResponse) {
                        int cid1 = comFloorBeanLzyResponse.data.getFloor().getCid();
                        Toast.makeText(CommentDetailActivity.this,cid1+"",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


}
