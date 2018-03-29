package org.nogizaka46.ui;

import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.nogizaka46.R;
import org.nogizaka46.base.BaseActivity;
import org.nogizaka46.bean.LzyResponse;
import org.nogizaka46.bean.RegisterSuccessBean;
import org.nogizaka46.config.Constant;
import org.nogizaka46.contract.IApiService;
import org.nogizaka46.db.PreUtils;
import org.nogizaka46.http.HttpUtils;
import org.nogizaka46.view.DropBounceInterpolator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class LoginActivity extends BaseActivity {


    @InjectView(R.id.ed_username)
    EditText edUsername;
    @InjectView(R.id.ed_psw)
    EditText edPsw;
    @InjectView(R.id.tv_yanzhengma)
    TextView tvYanzhengma;
    @InjectView(R.id.btn_commit)
    Button btnCommit;
    @InjectView(R.id.tv_xieyi)
    TextView tvXieyi;
    @InjectView(R.id.iv_wechat)
    ImageView ivWechat;
    @InjectView(R.id.iv_qq)
    ImageView ivQq;
    @InjectView(R.id.iv_weibo)
    ImageView ivWeibo;
    @InjectView(R.id.top_button_back)
    ImageButton topButtonBack;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.ed_pswcommit)
    EditText edPswcommit;
    @InjectView(R.id.lin_pswcommit)
    LinearLayout linPswcommit;

    private boolean isLogin =true ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        init();

    }

    public void back(View view) {
        this.finish();
    }

    private void init() {
        topButtonBack.setVisibility(View.VISIBLE);

        title.setText("登录");
        edUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = edUsername.getText().length();
                if (length != 0) {
                    btnCommit.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                } else {
                    btnCommit.setBackground(getResources().getDrawable(R.drawable.btn_bggray));
                }
            }
        });


    }


    @OnClick({R.id.tv_yanzhengma, R.id.btn_commit, R.id.tv_xieyi, R.id.iv_wechat, R.id.iv_qq, R.id.iv_weibo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_yanzhengma:
                getYanzheng();
                break;
            case R.id.btn_commit:
                commit();
                break;
            case R.id.tv_xieyi:

                break;
            case R.id.iv_wechat:
                // shouquan(Wechat.NAME);
                break;
            case R.id.iv_qq:
                // shouquan(QQ.NAME);
                break;
            case R.id.iv_weibo:
                // shouquan(SinaWeibo.NAME);
                break;
        }
    }

    private void commit() {
        String username = edUsername.getText().toString();
        String userpsw = edPsw.getText().toString();
        String pswcommit = edPswcommit.getText().toString();


        if(isLogin){
            userlogin(username ,userpsw);
        }else {

            if(TextUtils.isEmpty(pswcommit)){
                Toast.makeText(LoginActivity.this,"请输入确认密码",Toast.LENGTH_SHORT).show();
            }else if(!pswcommit.equals(userpsw)){
                Toast.makeText(LoginActivity.this,"密码不一致",Toast.LENGTH_SHORT).show();
            }else{
                userRegister(username ,userpsw);
            }

        }

    }

    private void userRegister(String username ,String userpsw) {
        IApiService retrofitInterface = HttpUtils.getInstance().getRetrofitInterface();
        Subscription subscribe = retrofitInterface.UserRegiter(username, userpsw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LzyResponse<RegisterSuccessBean>>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(LzyResponse<RegisterSuccessBean> registerSuccessBeanLzyResponse) {
                      switch (registerSuccessBeanLzyResponse.code){
                          case 200:
                              String id = registerSuccessBeanLzyResponse.data.getId();
                              String token = registerSuccessBeanLzyResponse.data.getToken();
                              PreUtils.writeString(LoginActivity.this , Constant.USER_ID,id);
                              PreUtils.writeString(LoginActivity.this ,Constant.USER_TOKEN,token);
                              Toast.makeText(LoginActivity.this ,"注册成功",Toast.LENGTH_LONG).show();
                              break ;
                          case 1 :
                              Toast.makeText(LoginActivity.this ,registerSuccessBeanLzyResponse.message,Toast.LENGTH_LONG).show();
                              break;
                      }


                    }
                });


    }

    private void userlogin(String username ,String userpsw) {
        IApiService retrofitInterface = HttpUtils.getInstance().getRetrofitInterface();
     Subscription subscribe = retrofitInterface.UserLoginNickname(username, userpsw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LzyResponse<RegisterSuccessBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        String message = e.getMessage();
                        Log.e("TAG", "onError: " + message );

                    }



                    @Override
                    public void onNext(LzyResponse<RegisterSuccessBean> registerSuccessBeanLzyResponse) {


                        switch (registerSuccessBeanLzyResponse.code){
                            case 200:
                                String id = registerSuccessBeanLzyResponse.data.getId();
                                String token = registerSuccessBeanLzyResponse.data.getToken();

                                PreUtils.writeString(LoginActivity.this , Constant.USER_ID,id);
                                PreUtils.writeString(LoginActivity.this ,Constant.USER_TOKEN,token);
                                Toast.makeText(LoginActivity.this ,"登录成功",Toast.LENGTH_SHORT).show();
                                finish();
                                break ;
                            case 1 :

                                Toast.makeText(LoginActivity.this ,registerSuccessBeanLzyResponse.message,Toast.LENGTH_SHORT).show();
                                break;
                        }



                    }
                });


    }

    private void getYanzheng() {

        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
          mShowAction.setDuration(500);
        mShowAction.setInterpolator(new BounceInterpolator());

        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);


        mHiddenAction.setDuration(500);
        mHiddenAction.setInterpolator(new FastOutLinearInInterpolator());

        if(isLogin){
             tvYanzhengma.setText("我要登录");
             linPswcommit.setVisibility(View.VISIBLE);
            linPswcommit.startAnimation(mShowAction);
             btnCommit.setText("注册");
             isLogin  = false ;
         }else{

             tvYanzhengma.setText("点我注册哦~");
             linPswcommit.setVisibility(View.GONE);
            linPswcommit.startAnimation(mHiddenAction);
             btnCommit.setText("登录");
             isLogin = true;
         }
    }


//    void shouquan(String name) {
//
//
//        final Platform pl = ShareSDK.getPlatform(name);
//        String userId = pl.getDb().getUserId();
//        Log.e("TAG", "shouquan: " + userId);
//        //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
//        pl.setPlatformActionListener(new PlatformActionListener() {
//            @Override
//            public void onError(Platform arg0, int arg1, Throwable arg2) {
//                // TODO Auto-generated method stub
//                arg2.printStackTrace();
//                pl.removeAccount(true);
//            }
//
//            @Override
//            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
//                // TODO Auto-generated method stub
//                //输出所有授权信息
//                arg0.getDb().exportData();
//                arg0.getDb().getUserId();
//
//            }
//
//            @Override
//            public void onCancel(Platform arg0, int arg1) {
//                // TODO Auto-generated method stub
//
//            }
//        });
//        //authorize与showUser单独调用一个即可
//        // pl.authorize();//单独授权,OnComplete返回的hashmap是空的
//        pl.showUser(null);//授权并获取用户信息
//        //移除授权
//        //pl.removeAccount(true);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
