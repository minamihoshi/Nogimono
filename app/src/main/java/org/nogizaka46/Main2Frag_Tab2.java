package org.nogizaka46;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import adapter.EuclidListAdapter;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import utils.Constants;
import utils.EuclidState;


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
    private TextView mTextViewProfileDescription;
    private EuclidListAdapter adapter;
    EuclidState mState = EuclidState.Closed;
    private View mOverlayListItemView;
    private FrameLayout mToolbar;
    private AnimatorSet mOpenProfileAnimatorSet;
    private AnimatorSet mCloseProfileAnimatorSet;
    float mInitialProfileButtonX;
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
    }

    private void initView() {
        lin1= (LinearLayout) view.findViewById(R.id.group_layout1);
        lin2= (LinearLayout) view.findViewById(R.id.group_layout2);
        group= (RadioGroup) view.findViewById(R.id.blog_group);
        ec_list= (ListView) view.findViewById(R.id.list_view);
        mToolbarProfile = (RelativeLayout) view.findViewById(R.id.toolbar_profile);
        mProfileDetails = (LinearLayout)view. findViewById(R.id.wrapper_profile_details);
        mTextViewProfileName = (TextView) view.findViewById(R.id.text_view_profile_name);
        mTextViewProfileDescription = (TextView)view.findViewById(R.id.text_view_profile_description);
        mWrapper = (RelativeLayout) view.findViewById(R.id.wrapper);
        mToolbar = (FrameLayout) view.findViewById(R.id.toolbar_list);
        mButtonProfile = view.findViewById(R.id.button_profile);
    }

    private void initData() {
        mSelfData = new ArrayList<Map<String, Object>>();
        group.setOnCheckedChangeListener(new Main2Group());
        sScreenWidth = getResources().getDisplayMetrics().widthPixels;
        sProfileImageHeight = getResources().getDimensionPixelSize(R.dimen.height_profile_image);
        sOverlayShape=buildAvatarCircleOverlay();
        initTestData();
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
    }

    private void showProfileDetails(Map<String, Object> item, final View view) {
        //防止出现滚动
        ec_list.setEnabled(false);
        //根据view的Top算出相对动画时间
        int profileDetailsAnimationDelay = Constants.MAX_DELAY_SHOW_DETAILS_ANIMATION * Math.abs(view.getTop()) / sScreenWidth;
        //一个和当前点击Item元素一样布局的view,覆盖在当前item元素上，让你以为是当前item元素进行了移动
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
        Picasso.with(getActivity()).load((Integer) item.get(EuclidListAdapter.KEY_AVATAR))
                .resize(sScreenWidth, sProfileImageHeight).centerCrop()
                .placeholder(R.color.blue)
                .into((ImageView) mOverlayListItemView.findViewById(R.id.image_view_reveal_avatar));

        mOverlayListItemView.findViewById(R.id.view_avatar_overlay).setBackground(sOverlayShape);
        ((TextView) mOverlayListItemView.findViewById(R.id.text_view_name)).setText((String) item.get(EuclidListAdapter.KEY_NAME));
        ((TextView) mOverlayListItemView.findViewById(R.id.text_view_description)).setText((String) item.get(EuclidListAdapter.KEY_DESCRIPTION_SHORT));
        mTextViewProfileName.setText((String) item.get(EuclidListAdapter.KEY_NAME));
        mTextViewProfileDescription.setText((String) item.get(EuclidListAdapter.KEY_DESCRIPTION_FULL));
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
                getAvatarRevealAnimator().start();
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
        final SupportAnimator mRevealAnimator = ViewAnimationUtils.createCircularReveal(mWrapperListItemReveal, sScreenWidth / 2, sProfileImageHeight / 2, dpToPx(Constants.CIRCLE_RADIUS_DP * 2), finalRadius);
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
    private void initTestData() {
        Map<String, Object> profileMap;
        List<Map<String, Object>> profilesList = new ArrayList<>();

        int[] avatars = {
                R.drawable.anastasia,
                R.drawable.andriy,
                R.drawable.dmitriy,
                R.drawable.dmitry_96,
                R.drawable.ed,
                R.drawable.illya,
                R.drawable.kirill,
                R.drawable.konstantin,
                R.drawable.oleksii,
                R.drawable.pavel,
                R.drawable.vadim};
        String[] names = getResources().getStringArray(R.array.array_names);

        for (int i = 0; i < avatars.length; i++) {
            profileMap = new HashMap<>();
            profileMap.put(EuclidListAdapter.KEY_AVATAR, avatars[i]);
            profileMap.put(EuclidListAdapter.KEY_NAME, names[i]);
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_SHORT, getString(R.string.lorem_ipsum_short));
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_FULL, getString(R.string.lorem_ipsum_long));
            profilesList.add(profileMap);
        }
        adapter=new EuclidListAdapter(getActivity(),R.layout.list_item,profilesList);
        ec_list.setAdapter(adapter);
    }


    //用ShapeDrawable画个圆
    private ShapeDrawable buildAvatarCircleOverlay() {
        //获取半径
        int radius = 666;
        ShapeDrawable overlay = new ShapeDrawable(new RoundRectShape(null, new RectF(
                        sScreenWidth / 2 - dpToPx(Constants.CIRCLE_RADIUS_DP * 2),
                        sProfileImageHeight / 2 - dpToPx(Constants.CIRCLE_RADIUS_DP* 2),
                        sScreenWidth / 2 - dpToPx(Constants.CIRCLE_RADIUS_DP * 2),
                        sProfileImageHeight / 2 - dpToPx(Constants.CIRCLE_RADIUS_DP * 2)),
                new float[]{radius, radius, radius, radius, radius, radius, radius, radius}));
        overlay.getPaint().setColor(getResources().getColor(R.color.gray));
        return overlay;
    }
    public int dpToPx(int dp) {
        return Math.round((float) dp * getResources().getDisplayMetrics().density);
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



    /**
     * This method creates and setups animator which shows profile toolbar.
     *
     * @return - animator object.
     */
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
                mButtonProfile.setX(mInitialProfileButtonX);
                mButtonProfile.bringToFront();
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

    /**
     * This method creates animator which shows profile details.
     */
    private Animator getOpenProfileDetailsAnimator() {
        Animator mOpenProfileDetailsAnimator = ObjectAnimator.ofFloat(mProfileDetails, View.Y,
                getResources().getDisplayMetrics().heightPixels,
                getResources().getDimensionPixelSize(R.dimen.height_profile_picture_with_toolbar));
        return mOpenProfileDetailsAnimator;
    }

    /**
     * This method starts set of transition animations, which hides profile toolbar, profile avatar
     * and profile details views.
     */
    private void animateCloseProfileDetails() {
        mState = EuclidState.Closing;
        getCloseProfileAnimatorSet().start();
    }

    /**
     * This method creates if needed the set of transition animations, which hides profile toolbar, profile avatar
     * and profile details views. Also it calls notifyDataSetChanged() on the ListView's adapter,
     * so it starts slide-in left animation on list items.
     *
     * @return - animator set that starts transition animations.
     */
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
