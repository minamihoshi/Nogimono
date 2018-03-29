package org.nogizaka46.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.nogizaka46.R;
import org.nogizaka46.base.BaseActivity;
import org.nogizaka46.bean.LzyResponse;
import org.nogizaka46.bean.UserInfoBean;
import org.nogizaka46.config.Constant;
import org.nogizaka46.db.PreUtils;
import org.nogizaka46.http.HttpUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SettingActivity extends BaseActivity {


    @InjectView(R.id.login_back)
    ImageView loginBack;
    @InjectView(R.id.main_header)
    TextView mainHeader;
    @InjectView(R.id.register)
    TextView register;
    @InjectView(R.id.iv_avater)
    ImageView ivAvater;
    @InjectView(R.id.linear_avater)
    LinearLayout linearAvater;
    @InjectView(R.id.tv_nickname)
    TextView tvNickname;
    @InjectView(R.id.linear_nickname)
    LinearLayout linearNickname;
    @InjectView(R.id.tv_gender)
    TextView tvGender;
    @InjectView(R.id.imageView2)
    ImageView imageView2;
    @InjectView(R.id.linear_gender)
    LinearLayout linearGender;
    @InjectView(R.id.tv_born)
    TextView tvBorn;
    @InjectView(R.id.linear_3)
    LinearLayout linear3;
    @InjectView(R.id.tv_loca)
    TextView tvLoca;
    @InjectView(R.id.linear_location)
    LinearLayout linearLocation;
    @InjectView(R.id.tv_introduce)
    TextView tvIntroduce;
    @InjectView(R.id.linear_introduce)
    LinearLayout linearIntroduce;
    @InjectView(R.id.tv_phone)
    TextView tvPhone;
    @InjectView(R.id.linear_phone)
    LinearLayout linearPhone;
    @InjectView(R.id.tv_email)
    TextView tvEmail;
    @InjectView(R.id.linear_email)
    LinearLayout linearEmail;
    @InjectView(R.id.tv_pingfen)
    TextView tvPingfen;
    @InjectView(R.id.linear_pingfen)
    LinearLayout linearPingfen;
    @InjectView(R.id.linearexit)
    LinearLayout linearexit;
    @InjectView(R.id.linear_content)
    LinearLayout linearContent;
    private PopupWindow popupwindow_sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.inject(this);
        mainHeader.setText("设置");
        loginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendUserInfo();
            }
        });
        initPopup();

        getUserInfo();
    }




    void getUserInfo(){
        String userid = PreUtils.readStrting(SettingActivity.this, Constant.USER_ID);
        String usertoken = PreUtils.readStrting(SettingActivity.this, Constant.USER_TOKEN);
        HttpUtils.getInstance().getRetrofitInterface().getUserInfo(userid,usertoken)
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Subscriber<LzyResponse<UserInfoBean>>() {
                     @Override
                     public void onCompleted() {

                     }

                     @Override
                     public void onError(Throwable e) {

                     }

                     @Override
                     public void onNext(LzyResponse<UserInfoBean> userInfoBeanLzyResponse) {

                         if(userInfoBeanLzyResponse.code == 200){
                             UserInfoBean data = userInfoBeanLzyResponse.data;
                             tvNickname.setText(data.getNickname());
                             tvPhone.setText(data.getPhone());
                             tvEmail.setText(data.getEmail());
                             tvIntroduce.setText(data.getIntroduction());

                         }else{
                             Toast.makeText(SettingActivity.this ,userInfoBeanLzyResponse.message,Toast.LENGTH_SHORT).show();
                         }


                     }
                 });
    }

    private void sendUserInfo() {
        String userid = PreUtils.readStrting(SettingActivity.this, Constant.USER_ID);
        String usertoken = PreUtils.readStrting(SettingActivity.this, Constant.USER_TOKEN);
        Log.e("TAG", "sendUserInfo: " + userid   + "-----" +usertoken);
        String nickname = tvNickname.getText().toString();
        String phone = tvPhone.getText().toString();
        String email = tvEmail.getText().toString();
        String intro = tvIntroduce.getText().toString();

        HttpUtils.getInstance().getRetrofitInterface()
                .UserSet(userid,usertoken,nickname,phone,email,intro)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LzyResponse<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                       tvNickname.setText("");
                       tvPhone.setText("");
                       tvEmail.setText("");
                       tvIntroduce.setText("");
                    }

                    @Override
                    public void onNext(LzyResponse<String> stringLzyResponse) {

                        Toast.makeText(SettingActivity.this , stringLzyResponse.message ,Toast.LENGTH_SHORT).show();

                    }
                });


    }

    void initPopup() {
        popupwindow_sex = new PopupWindow(SettingActivity.this);
        popupwindow_sex.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupwindow_sex.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(SettingActivity.this).inflate(R.layout.popup_sex, null);
        popupwindow_sex.setContentView(view);
        popupwindow_sex.setAnimationStyle(android.R.style.Animation_InputMethod);   // 设置窗口显示的动画效果
        TextView tv_male = (TextView) view.findViewById(R.id.male);
        TextView tv_female = (TextView) view.findViewById(R.id.female);
        TextView tv_cancel = (TextView) view.findViewById(R.id.cancel_pop);
        tv_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                tvGender.setText("男");
                popupwindow_sex.dismiss();
                // fieldkey = "gender";
                //fieldvalue = tvGender.getText().toString();
                //setUserInfo();
            }
        });

        tv_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvGender.setText("女");
                popupwindow_sex.dismiss();
                //fieldkey = "gender";
                //fieldvalue = tvGender.getText().toString();
                //setUserInfo();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupwindow_sex.dismiss();
            }
        });

        popupwindow_sex.setOutsideTouchable(false);
        popupwindow_sex.setFocusable(true);
        popupwindow_sex.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupwindow_sex.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    @OnClick({R.id.linear_avater, R.id.linear_nickname, R.id.linear_gender, R.id.linear_3, R.id.linear_location, R.id.linear_introduce, R.id.linear_pingfen, R.id.linear_phone, R.id.linear_email, R.id.linearexit})
    public void onViewClicked(View view) {
        Intent intent = new Intent(SettingActivity.this, EditUserInfoNeedActivity.class);
        switch (view.getId()) {

            case R.id.linear_avater:
                openPhoto();
                break;
            case R.id.linear_nickname:
                intent.putExtra("title", "设置昵称");
                intent.putExtra("hint", "请输入昵称");
                intent.putExtra("maxLength", 15);
                startActivityForResult(intent, 0);
                break;
            case R.id.linear_gender:

                popupwindow_sex.showAtLocation(findViewById(R.id.linear_content), Gravity.CENTER | Gravity.BOTTOM, 0, 0);
                setBackgroundAlpha(0.3f);
                break;
            case R.id.linear_3:
                // showTimePicker(tvBorn);
                break;
            case R.id.linear_location:
                // onAddressPicker();
                break;
            case R.id.linear_introduce:
                intent.putExtra("title", "自我介绍");
                intent.putExtra("hint", "请输入简介");
                intent.putExtra("maxLength", 200);
                startActivityForResult(intent, 1);
                break;
            case R.id.linear_pingfen:
                try {
                    Uri uri = Uri.parse("market://details?id=" + "org.nogizaka46");
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                } catch (Exception e) {
                    Toast.makeText(SettingActivity.this, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            case R.id.linear_phone:
                intent.putExtra("title", "自我介绍");
                intent.putExtra("hint", "请输入简介");
                intent.putExtra("maxLength", 200);
                startActivityForResult(intent, 2);
                break;
            case R.id.linear_email:

                intent.putExtra("title", "自我介绍");
                intent.putExtra("hint", "请输入简介");
                intent.putExtra("maxLength", 200);
                startActivityForResult(intent, 3);
                break;
            case R.id.linearexit:
                logout();
                break;
        }
    }

    private void logout() {
        //退出登录

    }


    private void openPhoto() {

//        PictureSelector.create(this)
//                .openGallery(PictureMimeType.ofImage())
//                .maxSelectNum(1)
//                .imageSpanCount(4)
//                .selectionMode(PictureConfig.SINGLE)
//                .previewImage(true)
//                .isCamera(true)
//                // .imageFormat(PictureMimeType.PNG)
//                .enableCrop(true)
//                .compress(true)
//                .withAspectRatio(1, 1)
//                .hideBottomControls(false)
//                .freeStyleCropEnabled(true)
//                .rotateEnabled(true)
//                .scaleEnabled(true)
//                .forResult(PictureConfig.CHOOSE_REQUEST);

    }

//    public void onAddressPicker() {
//        AddressPickTask task = new AddressPickTask(SettingActivity.this);
//        task.setHideProvince(false);
//        task.setHideCounty(false);
//        task.setCallback(new AddressPickTask.Callback() {
//            @Override
//            public void onAddressInitFailed() {
//                Toast.makeText(SettingActivity.this, "数据初始化失败", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAddressPicked(Province province, City city, County county) {
//                String res;
////                if (county == null) {
////                    res = province.getAreaName() + " " + city.getAreaName();
////                    showToast(province.getAreaName() + city.getAreaName());
////                } else {
////                    res = province.getAreaName() + " " + city.getAreaName() + " " + county.getAreaName();
////                    showToast(province.getAreaName() + city.getAreaName() + county.getAreaName());
////                }
//                tvLoca.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
//                //  fieldkey = "loca";
//                //   fieldvalue = tvLoca.getText().toString();
//                // setUserInfo();
//            }
//        });
//        task.execute("北京", "北京", "东城区");
//    }


//    private void showTimePicker(final TextView tv) {
//
//        String datatimeMin = StringUtil.getDatatimeMin();
//        CustomDatePicker customDatePicker = new CustomDatePicker(SettingActivity.this, new CustomDatePicker.ResultHandler() {
//            @Override
//            public void handle(String time) { // 回调接口，获得选中的时间
//                tv.setText(time);
//
//                //  fieldkey = "born";
//                //  fieldvalue = tvBorn.getText().toString();
//                //  setUserInfo();
//
//            }
//        }, "2010-01-01 00:00", "3000-12-31 23:59"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
//
//        customDatePicker.show(datatimeMin);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String text = data.getStringExtra("text");
            switch (requestCode) {
                case 0:
                    tvNickname.setText(text);
                    // fieldkey = "nickname";
                    //fieldvalue = text;
                    //setUserInfo();
                    break;
                case 1:
                    tvIntroduce.setText(text);
                    break;
                case 2:
                    tvPhone.setText(text);
                    break;
                case 3:
                    tvEmail.setText(text);
                    break;
//                case PictureConfig.CHOOSE_REQUEST:
//                    // 图片选择结果回调
//                    // selectList =
//                    Log.e("TAG", "onActivityResult: " + "aaaaaaaaaa");
//                    List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
//                    // 例如 LocalMedia 里面返回三种path
//                    // 1.media.getPath(); 为原图path
//                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
//                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
//                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
//                    LocalMedia localMedia = localMedias.get(0);
//                    Log.e("TAG", "onActivityResult: " + "1" + localMedia.getCompressPath() + "2" + localMedia.getCutPath() + "3" + localMedia.getPath());
//                    if (localMedia.isCompressed()) {
//                        String compressPath = localMedia.getCompressPath();
//                        uploadAvater(compressPath);
//
//                    } else {
//                        String cutPath = localMedia.getCutPath();
//                        uploadAvater(cutPath);
//                    }


                // break;
            }
        }
    }


    private void setUserData(String avatar, String nickname, String sex, String birth, String location) {

    }

    private void uploadAvater(String compressPath) {


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}