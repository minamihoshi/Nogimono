package org.nogizaka46.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.nogizaka46.R;
import org.nogizaka46.adapter.UnreadAdapter;
import org.nogizaka46.base.BaseActivity;
import org.nogizaka46.bean.LzyResponse;
import org.nogizaka46.bean.UnreadComBean;
import org.nogizaka46.config.Constant;
import org.nogizaka46.db.PreUtils;
import org.nogizaka46.http.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UnreadActivity extends BaseActivity {

    @BindView(R.id.rv_unread)
    RecyclerView rvUnread;
    @BindView(R.id.login_back)
    ImageView loginBack;
    @BindView(R.id.main_header)
    TextView mainHeader;
    @BindView(R.id.register)
    TextView register;
    private UnreadAdapter unreadAdapter;
    private List<UnreadComBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unread);
        ButterKnife.bind(this);

        initRv();
        mainHeader.setText("未读消息");
        loginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        register.setText("已读全部");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 setCommentRead("all");
            }
        });
    }

    private void initRv() {
        list = new ArrayList<>();
        unreadAdapter = new UnreadAdapter(list);
        rvUnread.setAdapter(unreadAdapter);
        rvUnread.setLayoutManager(new LinearLayoutManager(UnreadActivity.this, LinearLayoutManager.VERTICAL, false));

        unreadAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int fathercid = list.get(position).getFathercid();
                int fid = list.get(position).getFid(); //文章id

                Intent intent = new Intent(UnreadActivity.this, CommentDetailActivity.class);
                intent.putExtra(Constant.COMMENT_FATHER_ID, fathercid);
                intent.putExtra(Constant.COMMENT_FATHER_ARTICEL,fid);
                startActivity(intent);
            }
        });


        unreadAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

                setCommentRead(String.valueOf(list.get(position).getCid()));
                return true;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        getUnreadCom();
    }


    private void setCommentRead(String cid) {
        String uid = PreUtils.readStrting(this, Constant.USER_ID);
        String token = PreUtils.readStrting(this, Constant.USER_TOKEN);

        HttpUtils.getInstance().getRetrofitInterface().setCommentRead(uid, token, cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LzyResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LzyResponse<String> stringLzyResponse) {

                        Toast.makeText(UnreadActivity.this, "操作成功", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(UnreadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        onComplete();
                    }

                    @Override
                    public void onComplete() {
                        getUnreadCom();
                    }
                });

    }

    private void getUnreadCom() {

        String uid = PreUtils.readStrting(this, Constant.USER_ID);
        String token = PreUtils.readStrting(this, Constant.USER_TOKEN);
        HttpUtils.getInstance().getRetrofitInterface().getUnreadComment(uid, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LzyResponse<List<UnreadComBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LzyResponse<List<UnreadComBean>> listLzyResponse) {
                        list.clear();
                        if (listLzyResponse.data != null) {
                            list.addAll(listLzyResponse.data);
                        }

                        unreadAdapter.notifyDataSetChanged();
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
