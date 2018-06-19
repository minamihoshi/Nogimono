package org.nogizaka46.ui.fragment;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.ui.camera.CameraActivity;
import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;


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
import org.nogizaka46.utils.FileUtil;
import org.nogizaka46.utils.ImageLoader;
import org.nogizaka46.utils.RecognizeService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


import static android.app.Activity.RESULT_OK;
import static com.tencent.bugly.beta.tinker.TinkerManager.getApplication;


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
    @BindView(R.id.layout4)
    LinearLayout layout4;
    private String userid;
    private String usertoken;
    private QMUITipDialog   tipDialog;
    private static final int REQUEST_CODE_GENERAL_BASIC = 106;
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

        userid = PreUtils.readStrting(getActivity(), Constant.USER_ID);
        usertoken = PreUtils.readStrting(getActivity(), Constant.USER_TOKEN);

        if (TextUtils.isEmpty(userid)) {
            //Toast.makeText(getActivity(), "您还没有登录账号", Toast.LENGTH_SHORT).show();
            tvDenglu.setText("未登录");
            ivHead.setImageResource(R.drawable.morenhead);
            return;
        }

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
                            new ImageLoader.Builder(getActivity()).setImageUrl(data.getAvatar()).setLoadResourceId(R.drawable.morenhead).setImageView(ivHead).show();
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
                Log.e("sp", "onClick: " + userid + usertoken);

                Intent intent = new Intent();
                if (TextUtils.isEmpty(userid)) {
                    intent.setClass(getActivity(), LoginActivity.class);
                } else {
                    intent.setClass(getActivity(), SettingActivity.class);
                }

                startActivity(intent);

            }
        });
        layout3.setVisibility(View.GONE);
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // gotoPhotoPickActivity();
            }
        });

        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_GENERAL_BASIC);
            }
        });

        tvDenglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(userid)) {
                    return;
                }
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        layoutUnread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                if (TextUtils.isEmpty(userid)) {

                    intent.setClass(getActivity(), LoginActivity.class);
                } else {
                    intent.setClass(getActivity(), UnreadActivity.class);
                }
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


        // 识别成功回调，通用文字识别
        if (requestCode == REQUEST_CODE_GENERAL_BASIC && resultCode == RESULT_OK) {

            tipDialog = new QMUITipDialog.Builder(getContext())
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord("识别中")
                    .create();
            tipDialog.show();
            RecognizeService.recGeneralBasic(getActivity(), FileUtil.getSaveFile(getActivity()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(final String result) {

                           // Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                            tipDialog.dismiss();
                            new QMUIDialog.MessageDialogBuilder(getActivity())
                                    .setTitle("结果")
                                    .setMessage(result)
                                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                                        @Override
                                        public void onClick(QMUIDialog dialog, int index) {
                                                 dialog.dismiss();
                                        }
                                    })
                                    .addAction("复制内容", new QMUIDialogAction.ActionListener() {
                                        @Override
                                        public void onClick(QMUIDialog dialog, int index) {
                                           //获取剪贴板管理器：
                                            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                           // 创建普通字符型ClipData
                                            ClipData mClipData = ClipData.newPlainText("识别结果", result);
                                           // 将ClipData内容放到系统剪贴板里。
                                            cm.setPrimaryClip(mClipData);
                                            Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_SHORT).show();
                                        }
                                    })

                                    .show();

                        }
                    });
        }

    }


}
