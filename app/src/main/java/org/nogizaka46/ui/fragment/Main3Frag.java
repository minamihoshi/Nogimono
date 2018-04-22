package org.nogizaka46.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.nogizaka46.R;
import org.nogizaka46.bean.LzyResponse;
import org.nogizaka46.bean.UserInfoBean;
import org.nogizaka46.config.Constant;
import org.nogizaka46.db.PreUtils;
import org.nogizaka46.http.HttpUtils;
import org.nogizaka46.ui.LoginActivity;
import org.nogizaka46.ui.SettingActivity;
import org.nogizaka46.ui.UnreadActivity;
import org.nogizaka46.ui.activity.AboutActivity;
import org.nogizaka46.utils.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class Main3Frag extends Fragment {
    View view;
    Button btn;
    @BindView(R.id.settings)
    TextView settings;
    @BindView(R.id.about)
    TextView about;
    @BindView(R.id.exit_btn)
    Button exitBtn;
    @BindView(R.id.layout1)
    LinearLayout layout1;
    @BindView(R.id.layout2)
    LinearLayout layout2;
    @BindView(R.id.layout3)
    LinearLayout layout3;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_denglu)
    TextView tvDenglu;
    @BindView(R.id.imagesel)
    TextView imagesel;
    Unbinder bind;
    @BindView(R.id.tv_unread)
    TextView tvUnread;
    @BindView(R.id.layout_unread)
    LinearLayout layoutUnread;
    @BindView(R.id.iv_denglu)
    ImageView ivDenglu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main3_frag, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            return;
        initView();


    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();

    }

    void getUserInfo() {
        String userid = PreUtils.readStrting(getActivity(), Constant.USER_ID);
        String usertoken = PreUtils.readStrting(getActivity(), Constant.USER_TOKEN);
        HttpUtils.getInstance().getRetrofitInterface().getUserInfo(userid, usertoken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LzyResponse<UserInfoBean>>() {


                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LzyResponse<UserInfoBean> userInfoBeanLzyResponse) {

                        if (userInfoBeanLzyResponse.code == 200) {
                            UserInfoBean data = userInfoBeanLzyResponse.data;
                            String nickname = data.getNickname();
                            tvDenglu.setText(nickname);
                            new ImageLoader.Builder(getActivity()).setImageUrl(data.getAvatar()).setImageView(ivHead).show();
                        } else {
                            Toast.makeText(getActivity(), userInfoBeanLzyResponse.message, Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }


    private void initView() {

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);

            }
        });
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  gotoPhotoPickActivity();
            }
        });

        tvDenglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        layoutUnread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UnreadActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        bind.unbind();
    }


//    //跳转图片选择页面
//    @AfterPermissionGranted(0)
//    private void gotoPhotoPickActivity() {
//        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
//            BLPickerParam.startActivity(getActivity());
//        } else {
//            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问读写权限", 0, perms);
//        }
//    }

    //获取返回结果数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == BLPickerParam.REQUEST_CODE_PHOTO_PICKER) {
//            BLResultParam param = data.getParcelableExtra(BLResultParam.KEY);
//            List<String> imageList = param.getImageList();
//            StringBuilder sb = new StringBuilder();
//            for (String path : imageList) {
//                sb.append(path);
//                sb.append("\n");
//            }
//            ToastUtils.toast(getActivity(), sb.toString());
//        }

    }


}
