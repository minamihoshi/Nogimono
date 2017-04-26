package org.nogizaka46.ui.newsfragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;

import org.nogizaka46.R;
import org.nogizaka46.adapter.MyNewsAdapter;
import org.nogizaka46.base.MyApplication;
import org.nogizaka46.bean.NewBean;
import org.nogizaka46.bean.WithpicBean;
import org.nogizaka46.config.Constant;
import org.nogizaka46.config.UrlConfig;
import org.nogizaka46.contract.Contract;
import org.nogizaka46.ui.WebPageActivity;
import org.nogizaka46.utils.DividerItemDecoration;
import org.nogizaka46.utils.SpacesItemDecoration;
import org.nogizaka46.utils.ToastHelper;
import org.nogizaka46.utils.UMShareUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements Contract.INewsView, MyNewsAdapter.onNewsClickListener {

    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private List<NewBean> list;
    private Context mContext;
    private NewsPresenter presenter;
    private MyNewsAdapter adapter;
    private NewBean bean;
    private String category;
    private int page = 1;
    private int size = 10;
    private int mLastVisibleItem;
    private LinearLayoutManager mLayoutManager ;
    private boolean NeadClear;
    private PopupWindow popupWindow;
    private Button mbtn_openweb,mbtn_share,mbtn_save;
    private int itemposition;
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

        NeadClear =true;
        list = new ArrayList<>();
        adapter = new MyNewsAdapter(mContext, list, this);
        presenter = new NewsPresenter(this);
        presenter.getData(category, page, size);
        mLayoutManager =new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerview.addItemDecoration(new SpacesItemDecoration(1));
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(adapter);
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //判断是否该加载更多数据（1.屏幕处于停止状态；2.屏幕已经滑动到了item的最底端）
                if (mLastVisibleItem == adapter.getItemCount() - 1 && newState == RecyclerView
                        .SCROLL_STATE_IDLE) {
                    NeadClear =false;
                    ++page;
                    presenter.getData(category,page,size);

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });

        //swipeRefresh.setProgressViewOffset(true,0,0);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_purple);
      //  swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.color_red);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                 page =1 ;
                 NeadClear =true;
                  presenter.getData(category,page,size);
            }
        });
    }


    @Override
    public void getData(List<NewBean> beanList) {


        adapter.reloadRecyclerView(beanList, NeadClear);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaded() {
        if(swipeRefresh !=null &&swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoadFailed(String errormsg) {
        ToastHelper.showToast(mContext,errormsg);

    }

    @Override
    public void onNewsClick(int position) {
       openweb(position);

    }

    @Override
    public void onNewsLongClick(int position) {
        itemposition =position;
        bean = list.get(position);
        if(popupWindow==null){
            initPopup();
        }

        popupWindow.showAtLocation(recyclerview, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void initPopup() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_popupwindow_main, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        mbtn_openweb = (Button) view.findViewById(R.id.btn_openweb_main);
        mbtn_openweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                openweb(itemposition);
            }
        });


        mbtn_share = (Button) view.findViewById(R.id.btn_share_main);
        mbtn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = UrlConfig.BASE_FORMATWEB + bean.getView();
                String title = bean.getTitle();
                String summary = bean.getSummary();
                String image = null;
                List<WithpicBean> withpic = bean.getWithpic();
                if(withpic!=null){
                    WithpicBean withpicBean = withpic.get(0);
                    image = withpicBean.getImage();
                }
                popupWindow.dismiss();
                UMShareUtil.shareUrl((Activity) mContext,url,title,summary,image,umShareListener);
//                new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.QQ)
//                        .withText("分享")
//                        .withMedia(getUmengWeb(url,title,summary))
//                        .setDisplayList(SHARE_MEDIA.QQ)
//                        .setCallback(umShareListener).open();
//                        .setCallback(umShareListener)
//                        .share();

            }
        });

        mbtn_save = (Button) view.findViewById(R.id.btn_save_main);
        mbtn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bean = list.get(itemposition);
                popupWindow.dismiss();
                long savecode = ((MyApplication) getActivity().getApplication()).liteOrm.cascade().insert(bean);
                if(savecode>0){
                    Toast.makeText(mContext,"收藏成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext,"已经收藏过了~",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void openweb(int position){
        bean = list.get(position);
        String preview = bean.getView();
        Intent intent = new Intent(getActivity(), WebPageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.STARTWEB,bean);
        intent.putExtras(bundle);
        //intent.putExtra("preview", preview);
        startActivity(intent);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }
        @Override
        public void onResult(SHARE_MEDIA platform) {

            Toast.makeText(getActivity(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity(),platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(),platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };


     UMWeb getUmengWeb(String url,String title ,String des){
         UMWeb  web = new UMWeb(url);
         web.setTitle(title);//标题
         //web.setThumb();  //缩略图
         web.setDescription(des);//描述
         return  web ;
     }

    UMImage getUMimage(){

        UMImage image = new UMImage(getActivity(),R.mipmap.ic_launcher);
        UMImage thumb =  new UMImage(getActivity(), R.mipmap.ic_launcher);
        image.setThumb(thumb);
     return image ;
    }
}
