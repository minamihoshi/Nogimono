package org.nogizaka46;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import adapter.EuclidListAdapter;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import utils.Constants;
import utils.EuclidState;
import utils.Httputil;
import utils.MyUtil;
import view.MyToast;


public class Main2Frag_Tab2 extends Fragment {
    private View view,mButtonProfile;
    private RadioGroup group;
    private  LinearLayout lin1,lin2;
     List<Map<String, Object>> mSelfData;
    public static int sScreenWidth;
    public static int sProfileImageHeight;
    public static ShapeDrawable sOverlayShape;
    private ListView ec_list;
    RelativeLayout mWrapper;
    private RelativeLayout mToolbarProfile;
    private LinearLayout mProfileDetails;
    private TextView mTextViewProfileName;
    private TextView mTextViewProfileBirthday;
    private TextView mTextViewProfileHeight;
    private EuclidListAdapter adapter;
    EuclidState mState = EuclidState.Closed;
    private View mOverlayListItemView;
    private FrameLayout mToolbar;
    private AnimatorSet mOpenProfileAnimatorSet;
    private AnimatorSet mCloseProfileAnimatorSet;
    float mInitialProfileButtonX;
    Handler handler;
    HashMap<String,Object>map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
           view=inflater.inflate(R.layout.main2frag_tab2,container,false);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            return;
        initView();
        initData();
        initHandler();
    }

    private void initHandler() {
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case  1:
                        SetListData();
                        break;
                    case  2:
                        MyToast.showText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT, false);
                        break;
                }
            }
        };
    }

    private void initView() {
        lin1= (LinearLayout) view.findViewById(R.id.group_layout1);
        lin2= (LinearLayout) view.findViewById(R.id.group_layout2);
        group= (RadioGroup) view.findViewById(R.id.blog_group);
        ec_list= (ListView) view.findViewById(R.id.list_view);
        mToolbarProfile = (RelativeLayout) view.findViewById(R.id.toolbar_profile);
        mProfileDetails = (LinearLayout)view. findViewById(R.id.wrapper_profile_details);
        mTextViewProfileName = (TextView) view.findViewById(R.id.text_view_profile_name);
        mTextViewProfileBirthday= (TextView)view.findViewById(R.id.birthday);
        mTextViewProfileHeight= (TextView) view.findViewById(R.id.height);
        mWrapper = (RelativeLayout) view.findViewById(R.id.wrapper);
        mToolbar = (FrameLayout) view.findViewById(R.id.toolbar_list);
        mButtonProfile = view.findViewById(R.id.button_profile);
    }

    private void initData() {
        mSelfData = new ArrayList<Map<String, Object>>();
        group.setOnCheckedChangeListener(new Main2Group());
        sScreenWidth = getResources().getDisplayMetrics().widthPixels;
        sProfileImageHeight = getResources().getDimensionPixelSize(R.dimen.height_profile_image);
        sOverlayShape= MyUtil.buildAvatarCircleOverlay(getActivity());
        ec_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mState = EuclidState.Opening;
                showProfileDetails((Map<String, Object>) parent.getItemAtPosition(position), view);

            }
        });
        view.findViewById(R.id.toolbar_profile_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCloseProfileDetails();
            }
        });
        doAction();
    }

    private void SetListData() {
      if(adapter==null){
          adapter=new EuclidListAdapter(getActivity(),R.layout.list_item,mSelfData);
          ec_list.setAdapter(adapter);
      }else {
          adapter.notifyDataSetChanged();
      }
    }

    private void doAction() {
        Httputil.httpGet( Constants.Base_Url+"member/getAll", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg=new Message();
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    if(obj.getString("code").equals("200")){
                            JSONArray responseData=obj.getJSONArray("responseData");
                        if (responseData!=null&& responseData.length() > 0) {
                            for (int i = 0; i <responseData.length() ; i++) {
                                map = new HashMap<String, Object>();
                                JSONObject itemobj = new JSONObject(responseData.get(i).toString());
                                map.put("name_kanji",itemobj.optString("name_kanji").toString()); //姓名-汉字
                                map.put("avatar",itemobj.optString("avatar").toString());//成员头像
                                map.put("birthday",itemobj.optString("birthday").toString());//生日
                                map.put("height",itemobj.optString("height").toString());//身高
                                mSelfData.add(map);
                            }
                        }
                        msg.what=1;
                    }
                } catch (Exception e) {
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Message msg=new Message();
                msg.what=2;
                msg.obj=getResources().getString(R.string.nodata);
                handler.sendMessage(msg);
            }
        });
    }
    private void showProfileDetails(Map<String, Object> item, final View view) {
        //防止出现滚动
        ec_list.setEnabled(false);
        //根据view的Top算出相对动画时间
        int profileDetailsAnimationDelay = Constants.MAX_DELAY_SHOW_DETAILS_ANIMATION * Math.abs(view.getTop()) / sScreenWidth;
        //当前点击Item元素一样布局的view,覆盖在当前item元素上，让你以为是当前item元素进行了移动
        addOverlayListItem(item, view);
        //执行Reveal动画和把覆盖在Item元素上的布局进行从当前位置移动到toolbar的Bottom位置
        startRevealAnimation(profileDetailsAnimationDelay);
        //执行简介里的button缩放动画和ToolBar、以及简介内容的位移动画
        animateOpenProfileDetails(profileDetailsAnimationDelay);
    }

    private void addOverlayListItem(Map<String, Object> item, View view) {
        if (mOverlayListItemView == null) {
            mOverlayListItemView = getActivity().getLayoutInflater().inflate(R.layout.overlay_list_item, mWrapper, false);
        } else {
            mWrapper.removeView(mOverlayListItemView);
        }
        Picasso.with(getActivity()).load(item.get("avatar").toString()).resize(sScreenWidth, sProfileImageHeight).centerCrop().placeholder(R.color.blue).into((ImageView) mOverlayListItemView.findViewById(R.id.image_view_reveal_avatar));

        ((TextView) mOverlayListItemView.findViewById(R.id.text_view_name)).setText(item.get("name_kanji").toString());

        mTextViewProfileName.setText(item.get("name_kanji").toString());
        mTextViewProfileBirthday.setText(getResources().getString(R.string.ec_txt1)+item.get("birthday").toString());
        mTextViewProfileHeight.setText(getResources().getString(R.string.ec_txt2)+item.get("height").toString());

        //动态的添加一个View到相对布局里，并且把这个View显示在你点击ListView的那个Item一样的位置。
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = view.getTop() + mToolbar.getHeight();
        params.bottomMargin = -(view.getBottom() - ec_list.getHeight());
        mWrapper.addView(mOverlayListItemView, params);
        mToolbar.bringToFront();
    }
    private void startRevealAnimation(final int profileDetailsAnimationDelay) {
        mOverlayListItemView.post(new Runnable() {
            @Override
            public void run() {
                getAvatarRevealAnimator().start();//开启一个自定义的动画
                getAvatarShowAnimator(profileDetailsAnimationDelay).start();
            }
        });
    }
    private Animator getAvatarShowAnimator(int profileDetailsAnimationDelay) {
        final Animator mAvatarShowAnimator = ObjectAnimator.ofFloat(mOverlayListItemView, View.Y, mOverlayListItemView.getTop(), mToolbarProfile.getBottom());
        mAvatarShowAnimator.setDuration(profileDetailsAnimationDelay + Constants.ANIMATION_DURATION_SHOW_PROFILE_DETAILS);
        mAvatarShowAnimator.setInterpolator(new DecelerateInterpolator());
        return mAvatarShowAnimator;
    }
    private SupportAnimator getAvatarRevealAnimator() {
        final LinearLayout mWrapperListItemReveal = (LinearLayout) mOverlayListItemView.findViewById(R.id.wrapper_list_item_reveal);
        int finalRadius = Math.max(mOverlayListItemView.getWidth(), mOverlayListItemView.getHeight());
        final SupportAnimator mRevealAnimator = ViewAnimationUtils.createCircularReveal(mWrapperListItemReveal, sScreenWidth / 2, sProfileImageHeight / 2, MyUtil.dpToPx(getActivity(),Constants.CIRCLE_RADIUS_DP * 2), finalRadius);
        mRevealAnimator.setDuration(Constants.REVEAL_ANIMATION_DURATION);
        mRevealAnimator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
                mWrapperListItemReveal.setVisibility(View.VISIBLE);
                mOverlayListItemView.setX(0);
            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationRepeat() {

            }
        });
        return mRevealAnimator;
    }

    private void animateOpenProfileDetails(int profileDetailsAnimationDelay) {
        getOpenProfileAnimatorSet(profileDetailsAnimationDelay).start();
    }

    private AnimatorSet getOpenProfileAnimatorSet(int profileDetailsAnimationDelay) {
        if (mOpenProfileAnimatorSet == null) {
            List<Animator> profileAnimators = new ArrayList<>();
            profileAnimators.add(getOpenProfileToolbarAnimator());
            profileAnimators.add(getOpenProfileDetailsAnimator());

            mOpenProfileAnimatorSet = new AnimatorSet();
            mOpenProfileAnimatorSet.playTogether(profileAnimators);
            mOpenProfileAnimatorSet.setDuration(Constants.ANIMATION_DURATION_SHOW_PROFILE_DETAILS);
        }
        mOpenProfileAnimatorSet.setStartDelay(profileDetailsAnimationDelay);
        mOpenProfileAnimatorSet.setInterpolator(new DecelerateInterpolator());
        return mOpenProfileAnimatorSet;
    }


    private Animator getOpenProfileToolbarAnimator() {
        Animator mOpenProfileToolbarAnimator = ObjectAnimator.ofFloat(mToolbarProfile, View.Y, -mToolbarProfile.getHeight(), 0);
        mOpenProfileToolbarAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mToolbarProfile.setX(0);
                mToolbarProfile.bringToFront();
                mToolbarProfile.setVisibility(View.VISIBLE);
                mProfileDetails.setX(0);
                mProfileDetails.bringToFront();
                mProfileDetails.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mState = EuclidState.Opened;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return mOpenProfileToolbarAnimator;
    }

    private Animator getOpenProfileDetailsAnimator() {
        Animator mOpenProfileDetailsAnimator = ObjectAnimator.ofFloat(mProfileDetails, View.Y, getResources().getDisplayMetrics().heightPixels, getResources().getDimensionPixelSize(R.dimen.height_profile_picture_with_toolbar));
        return mOpenProfileDetailsAnimator;
    }

    private void animateCloseProfileDetails() {
        mState = EuclidState.Closing;
        getCloseProfileAnimatorSet().start();
    }

    private AnimatorSet getCloseProfileAnimatorSet() {
        if (mCloseProfileAnimatorSet == null) {
            Animator profileToolbarAnimator = ObjectAnimator.ofFloat(mToolbarProfile, View.X,
                    0, mToolbarProfile.getWidth());

            Animator profilePhotoAnimator = ObjectAnimator.ofFloat(mOverlayListItemView, View.X,
                    0, mOverlayListItemView.getWidth());
            profilePhotoAnimator.setStartDelay(Constants.STEP_DELAY_HIDE_DETAILS_ANIMATION);

            Animator profileButtonAnimator = ObjectAnimator.ofFloat(mButtonProfile, View.X,
                    mInitialProfileButtonX, mOverlayListItemView.getWidth() + mInitialProfileButtonX);
            profileButtonAnimator.setStartDelay(Constants.STEP_DELAY_HIDE_DETAILS_ANIMATION* 2);

            Animator profileDetailsAnimator = ObjectAnimator.ofFloat(mProfileDetails, View.X,
                    0, mToolbarProfile.getWidth());
            profileDetailsAnimator.setStartDelay(Constants.STEP_DELAY_HIDE_DETAILS_ANIMATION* 2);

            List<Animator> profileAnimators = new ArrayList<>();
            profileAnimators.add(profileToolbarAnimator);
            profileAnimators.add(profilePhotoAnimator);
            profileAnimators.add(profileButtonAnimator);
            profileAnimators.add(profileDetailsAnimator);

            mCloseProfileAnimatorSet = new AnimatorSet();
            mCloseProfileAnimatorSet.playTogether(profileAnimators);
            mCloseProfileAnimatorSet.setDuration(Constants.ANIMATION_DURATION_CLOSE_PROFILE_DETAILS);
            mCloseProfileAnimatorSet.setInterpolator(new AccelerateInterpolator());
            mCloseProfileAnimatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }
                @Override
                public void onAnimationEnd(Animator animation) {
                    mToolbarProfile.setVisibility(View.INVISIBLE);
                    mButtonProfile.setVisibility(View.INVISIBLE);
                    mProfileDetails.setVisibility(View.INVISIBLE);
                    ec_list.setEnabled(true);
                    mState = EuclidState.Closed;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        return mCloseProfileAnimatorSet;
    }

   private  class Main2Group implements RadioGroup.OnCheckedChangeListener{
       @Override
       public void onCheckedChanged(RadioGroup group, int checkedId) {
           switch (checkedId){
               case  R.id.btn1:
                   lin1.setVisibility(View.VISIBLE);
                   lin2.setVisibility(View.GONE);
                   break;
               case  R.id.btn2:
                   lin1.setVisibility(View.GONE);
                   lin2.setVisibility(View.VISIBLE);
                   break;
           }
       }
   }
}
