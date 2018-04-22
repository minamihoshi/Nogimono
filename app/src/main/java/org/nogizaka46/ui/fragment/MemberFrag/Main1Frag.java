package org.nogizaka46.ui.fragment.MemberFrag;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.nogizaka46.R;
import org.nogizaka46.adapter.MemberAdapter;
import org.nogizaka46.base.BaseFragment;
import org.nogizaka46.bean.MemberListBean;
import org.nogizaka46.config.Constant;
import org.nogizaka46.http.HttpUtils;
import org.nogizaka46.ui.blogactivity.BlogActivity;
import org.nogizaka46.utils.ToastHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class Main1Frag extends BaseFragment implements MemberAdapter.onMemberClickListener {
    View view;
    @BindView(R.id.recyclerview_member)
    RecyclerView recyclerview;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.iv_areaselect)
    ImageView ivAreaselect;
    @BindView(R.id.refresh_game)
    SmartRefreshLayout refreshGame;

    private GridLayoutManager manager;
    private MemberAdapter adapter;
    private List<MemberListBean> list;
    //  private Subscription subscription;
    private boolean isShowSelect;
    private String group;
    Unbinder bind;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main1_frag, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list = new ArrayList<>();
        group = "all";
        manager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
        adapter = new MemberAdapter(mContext, list, this);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adapter);
        refreshGame.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                initData();
            }
        });
        //recyclerview.addItemDecoration(new DividerGridItemDecoration(mContext));
        initData();
        initPopup();
    }

    private void initData() {
        //subscription =
        HttpUtils.getInstance().getRetrofitInterface().getMemberBean(group)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MemberListBean>>() {


                    @Override
                    public void onError(Throwable e) {
                        ToastHelper.showToast(mContext, getString(R.string.wangluowenti));
                        onComplete();
                    }

                    @Override
                    public void onComplete() {


                        if(refreshGame.isRefreshing()){
                            refreshGame.finishRefresh(2000);
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<MemberListBean> memberBeanList) {
                        list.clear();
                        list.addAll(memberBeanList);
                        adapter.notifyDataSetChanged();

                    }
                });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
//        if (!subscription.isUnsubscribed() && subscription != null) {
//            subscription.unsubscribe();
//        }
    }

    @Override
    public void onMemberClick(int position) {
        Log.e("TAG", "onMemberClick: " + list.size());
        if (list != null) {
            MemberListBean memberBean = list.get(position);
            Intent intent = new Intent(mContext, BlogActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.STARTBLOG, list.get(position));
            intent.putExtras(bundle);
            startActivity(intent);

        }

    }

    @Override
    public void onMemberLongClick(int position) {

    }

    void initPopup() {

        // areaRecyclerView  = new RecyclerView(getActivity());
        // areaRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        popupWindow = new PopupWindow(getActivity());
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_area, null);

        view.findViewById(R.id.tv_nogi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                group = "nogizaka";
                initData();

            }
        });

        view.findViewById(R.id.tv_keya).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                group = "keyakizaka";
                initData();
            }
        });

        view.findViewById(R.id.tv_sdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                group = ((TextView) v).getText().toString();
                initData();
            }
        });


        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        //popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);   // 设置窗口显示的动画效果
        popupWindow.setFocusable(false);                                        // 点击其他地方隐藏键盘 popupWindow
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tvArea.setSelected(false);
                ivAreaselect.setImageResource(R.drawable.area1);
                isShowSelect = true;
            }
        });
    }

    @OnClick({R.id.tv_area, R.id.iv_areaselect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_area:
                if (popupWindow != null) {
                    if (!isShowSelect) {
                        // tvArea.setTextColor(getResources().getColor(R.color.colorAccent));
                        tvArea.setSelected(true);
                        ivAreaselect.setImageResource(R.drawable.area2);
                        isShowSelect = true;
                        Log.e("TAG", "onViewClicked: show");
                        popupWindow.showAsDropDown(tvArea);
                        //  areaAdapter.notifyDataSetChanged();

                    } else {
                        tvArea.setSelected(false);
                        ivAreaselect.setImageResource(R.drawable.area1);
                        isShowSelect = false;
                        if (popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        Log.e("TAG", "onViewClicked: dismissssssss");

                    }

                }
                break;
            case R.id.iv_areaselect:
                break;
        }
    }
}
