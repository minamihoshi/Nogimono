package org.nogizaka46.ui.fragment.MemberFrag;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Main1Frag extends BaseFragment implements MemberAdapter.onMemberClickListener {
    View view;
    @InjectView(R.id.recyclerview_member)
    RecyclerView recyclerview;

    private GridLayoutManager manager ;
    private MemberAdapter adapter ;
    private List<MemberListBean> list ;
    private Subscription subscription ;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main1_frag, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list = new ArrayList<>();

        manager = new GridLayoutManager(mContext,3,GridLayoutManager.VERTICAL,false);
        adapter = new MemberAdapter(mContext,list,this);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adapter);
        //recyclerview.addItemDecoration(new DividerGridItemDecoration(mContext));
        initData();
    }

    private void initData() {
         subscription = HttpUtils.getInstance().getRetrofitInterface().getMemberBean()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<MemberListBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastHelper.showToast(mContext,getString(R.string.wangluowenti));
                    }

                    @Override
                    public void onNext(List<MemberListBean> memberBeanList) {
                        list.addAll(memberBeanList);
                        adapter.notifyDataSetChanged();

                    }
                });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if(!subscription.isUnsubscribed()&&subscription!=null){
            subscription.unsubscribe();
        }
    }

    @Override
    public void onMemberClick(int position) {
        Log.e("TAG", "onMemberClick: "+list.size() );
        if(list !=null){
            MemberListBean memberBean = list.get(position);
            Intent intent  = new Intent(mContext, BlogActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.STARTBLOG,list.get(position));
            intent.putExtras(bundle);
            startActivity(intent);

        }

    }

    @Override
    public void onMemberLongClick(int position) {

    }
}
