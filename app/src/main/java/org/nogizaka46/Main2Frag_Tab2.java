package org.nogizaka46;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;

import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.transition.Fade;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;


public class Main2Frag_Tab2 extends Fragment {
    View view;
    RecyclerView recyclerView;
    MembersAdapter adapter;
    RadioGroup group;
    LinearLayout lin1,lin2;
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
        lin1= (LinearLayout) view.findViewById(R.id.group_layout1);
        lin2= (LinearLayout) view.findViewById(R.id.group_layout2);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerview);
        group= (RadioGroup) view.findViewById(R.id.blog_group);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter=new MembersAdapter();
        recyclerView.setAdapter(adapter);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup group, int checkedId) {
               switch (checkedId){
                   case R.id.btn1:
                       lin1.setVisibility(View.VISIBLE);
                       lin2.setVisibility(View.GONE);
                       break;
                   case R.id.btn2:
                       lin1.setVisibility(View.GONE);
                       lin2.setVisibility(View.VISIBLE);
                       break;
               }
           }
       });
    }



   private class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder>{
       private Context mContext;

       @Override
       public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           if (mContext==null){
               mContext=parent.getContext();
           }
           View view=getActivity().getLayoutInflater().from(mContext).inflate(R.layout.main2frag_tab2_item,null,false);
           final  ViewHolder holder=new ViewHolder(view);
           holder.cardView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(final View view) {
                   Intent intent=new Intent(mContext,MemberInfoActivity.class);
                   intent.putExtra("img",(String.valueOf(getResources().getDrawable(R.drawable.logo))));
                   intent.putExtra("texts","炸鸡姐妹");
                   Fade fade = new Fade();
                   fade.setDuration(2000);
                   fade.setInterpolator(new AccelerateInterpolator());
                   getActivity().getWindow().setExitTransition(fade);
                   getActivity().getWindow().setEnterTransition(fade);
                   ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity());
                   mContext.startActivity(intent,optionsCompat.toBundle());
               }
           });
           return holder;
       }

       @Override
       public void onBindViewHolder(ViewHolder holder, int position) {
                      holder.texts.setText("炸鸡姐妹");
       }


       @Override
       public int getItemCount() {
           return 46;
       }

       public  class ViewHolder extends  RecyclerView.ViewHolder{
           CardView cardView;
           ImageView images;
           TextView texts;
           public ViewHolder(View view) {
               super(view);
               cardView=(CardView)view;
               images= (ImageView) view.findViewById(R.id.pic);
               texts= (TextView) view.findViewById(R.id.name);
           }
       }
   }

}
