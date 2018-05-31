package org.nogizaka46.ui;

import android.app.UiModeManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.nogizaka46.R;
import org.nogizaka46.adapter.ComDetailAdapter;
import org.nogizaka46.base.BaseActivity;
import org.nogizaka46.bean.ComFloorBean;
import org.nogizaka46.bean.LzyResponse;
import org.nogizaka46.bean.NewBean;
import org.nogizaka46.config.Constant;
import org.nogizaka46.db.PreUtils;
import org.nogizaka46.http.HttpUtils;
import org.nogizaka46.utils.ImageLoader;
import org.nogizaka46.view.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommentDetailActivity extends BaseActivity {
    int cid;
    @BindView(R.id.iv_avatar_comfather)
    ImageView ivAvatarComfather;
    @BindView(R.id.tv_nickname_comfather)
    TextView tvNicknameComfather;
    @BindView(R.id.tv_time_comfather)
    TextView tvTimeComfather;
    @BindView(R.id.tv_floor_comfather)
    TextView tvFloorComfather;
    @BindView(R.id.tv_msg_comfather)
    TextView tvMsgComfather;
    @BindView(R.id.rv_com_child)
    RecyclerView rvComChild;
    @BindView(R.id.tv_huifu)
    TextView tvHuifu;
    @BindView(R.id.edit_comment)
    EditText editComment;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.rela_content)
    RelativeLayout relaContent;
    @BindView(R.id.login_back)
    ImageView loginBack;
    @BindView(R.id.main_header)
    TextView mainHeader;
    @BindView(R.id.register)
    TextView register;
    private ComDetailAdapter comChildAdapter;
    private List<ComFloorBean.FloorBean.ChildBean> list;
    private ComFloorBean bean;
    private InputMethodManager inputMethodManager;
    private String father;
    private String touid;
    private String fid; //文章id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        ButterKnife.bind(this);

        cid = getIntent().getIntExtra(Constant.COMMENT_FATHER_ID, 0);
        fid = getIntent().getStringExtra(Constant.COMMENT_FATHER_ARTICEL);
        initView();
        initRv();
        initSoft();
    }

    private void openweb(){
        NewBean bean = new NewBean();
        bean.setView("/view/"+fid);
        bean.setId(fid);
        Intent intent = new Intent(this, WebPageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.STARTWEB,bean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initView() {
        mainHeader.setText("评论详情");
        register.setText("进入原文");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openweb();
            }
        });
        loginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendComment();
            }
        });

        relaContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击主评论
                String nickname = bean.getFloor().getUser().getNickname(); //楼主名
                int cid = bean.getFloor().getCid(); //评论cid
                father = String.valueOf(cid);
                touid = null;
                tvHuifu.setVisibility(View.GONE);
                tvHuifu.setText("");
                editComment.requestFocus();
                inputMethodManager.toggleSoftInput(0, 0);
            }
        });
        relaContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showCommetDelDialog(String.valueOf(bean.getFloor().getCid()));
                return true;
            }
        });

    }

    private void initSoft() {

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    private void initRv() {

        list = new ArrayList<>();
        rvComChild.setLayoutManager(new LinearLayoutManager(CommentDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        comChildAdapter = new ComDetailAdapter(list);
        rvComChild.setAdapter(comChildAdapter);
        rvComChild.addItemDecoration(new DividerItemDecoration(CommentDetailActivity.this, DividerItemDecoration.VERTICAL));
        comChildAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //点击子评论
                int cid = bean.getFloor().getCid(); //主评论cid
                String nickname = list.get(position).getUser().getNickname();//要回复的nickname
                int id = list.get(position).getUser().getId(); //要回复的id
                father = String.valueOf(cid);
                touid = String.valueOf(id);
                tvHuifu.setVisibility(View.VISIBLE);
                tvHuifu.setText("回复" + nickname + ":");
                editComment.requestFocus();
                inputMethodManager.toggleSoftInput(0, 0);
            }
        });
        comChildAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                showCommetDelDialog(String.valueOf(list.get(position).getCid()));
                return true;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getCommentByFloor();

    }

    void getCommentByFloor() {

        HttpUtils.getInstance().getRetrofitInterface().getCommentByFloor(String.valueOf(cid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LzyResponse<ComFloorBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LzyResponse<ComFloorBean> comFloorBeanLzyResponse) {

                        if (comFloorBeanLzyResponse.code == 200) {
                            bean = comFloorBeanLzyResponse.data;
                            ComFloorBean.FloorBean.UserBean user = bean.getFloor().getUser();
                            tvNicknameComfather.setText(user.getNickname());
                            tvFloorComfather.setText("#" + bean.getFloor().getFloor());
                            tvMsgComfather.setText(bean.getFloor().getMsg());
                            tvTimeComfather.setText(bean.getFloor().getTime());

                            new ImageLoader.Builder(CommentDetailActivity.this).setImageUrl(user.getAvatar()).setImageView(ivAvatarComfather)
                                    .setLoadResourceId(R.drawable.morenhead).show();

                            List<ComFloorBean.FloorBean.ChildBean> child = bean.getFloor().getChild();

                            list.clear();
                            list.addAll(child);
                            comChildAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(CommentDetailActivity.this, comFloorBeanLzyResponse.message, Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(CommentDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        onComplete();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void showCommetDelDialog(final String cid) {
        new SweetAlertDialog(CommentDetailActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText(getResources().getString(R.string.dialog_titles))
                .setContentText("是否删除这条评论").setConfirmText(getResources().getString(R.string.ok)).setCancelText(getResources().getString(R.string.cancel)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                deleteComment(cid);
                sweetAlertDialog.dismissWithAnimation();


            }
        }).show();

    }

    void deleteComment(String cid) {
        String userid = PreUtils.readStrting(CommentDetailActivity.this, Constant.USER_ID);

        if(TextUtils.isEmpty(userid)){

            Toast.makeText(this, "您还没有登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this ,LoginActivity.class);
            startActivity(intent);
            return;
        }

        String token = PreUtils.readStrting(CommentDetailActivity.this, Constant.USER_TOKEN);
        HttpUtils.getInstance().getRetrofitInterface().delComment(userid, token, cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LzyResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LzyResponse<String> stringLzyResponse) {
                        if (stringLzyResponse.code == 200) {
                            Toast.makeText(CommentDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CommentDetailActivity.this, stringLzyResponse.message, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(CommentDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        getCommentByFloor();
                    }
                });

    }

    private void sendComment() {
        String userid = PreUtils.readStrting(CommentDetailActivity.this, Constant.USER_ID);
        if(TextUtils.isEmpty(userid)){

            Toast.makeText(this, "您还没有登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this ,LoginActivity.class);
            startActivity(intent);
            return;
        }
        String token = PreUtils.readStrting(CommentDetailActivity.this, Constant.USER_TOKEN);
        String msg = editComment.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(CommentDetailActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        HttpUtils.getInstance().getRetrofitInterface().sendComment(String.valueOf(bean.getFid()), userid, token, msg, father, touid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LzyResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(LzyResponse<String> stringLzyResponse) {

                        Toast.makeText(CommentDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(CommentDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        editComment.setText("");
                        tvHuifu.setVisibility(View.GONE);
                        inputMethodManager.toggleSoftInput(0, 0);
                        getCommentByFloor();
                    }
                });
    }

}
