package org.nogizaka46.ui.newsfragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.nogizaka46.R;
import org.nogizaka46.adapter.NewsAdapter;
import org.nogizaka46.bean.NewsBean;
import org.nogizaka46.config.Constant;
import org.nogizaka46.contract.Contract;
import org.nogizaka46.ui.WebPageActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements Contract.INewsView, NewsAdapter.onNewsClickListener {

    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<NewsBean.ContentBean> list;
    private Context mContext;
    private NewsPresenter presenter;
    private NewsAdapter adapter;
    private NewsBean.ContentBean bean;
    private String category;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        category = bundle.getString(Constant.NEWS_CATEGORY);
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list = new ArrayList<>();
        adapter = new NewsAdapter(mContext, list, this);
        presenter = new NewsPresenter(this);
        presenter.getData(category);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void getData(NewsBean newsBean) {
        List<NewsBean.ContentBean> content = newsBean.getContent();
        adapter.reloadRecyclerView(content,true);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaded() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoadFailed(String errormsg) {
        Toast.makeText(mContext,errormsg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNewsClick(int position) {
        bean = list.get(position);
        String preview = bean.getPreview();
        Intent intent = new Intent(getActivity(), WebPageActivity.class);
        intent.putExtra("preview", preview);
        startActivity(intent);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
