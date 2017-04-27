package org.nogizaka46.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.nogizaka46.R;
import org.nogizaka46.ui.activity.AboutActivity;
import org.nogizaka46.utils.ToastHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class Main3Frag extends Fragment {
    View view;
    Button btn;
    @InjectView(R.id.settings)
    TextView settings;
    @InjectView(R.id.about)
    TextView about;
    @InjectView(R.id.exit_btn)
    Button exitBtn;
    @InjectView(R.id.layout1)
    LinearLayout layout1;
    @InjectView(R.id.layout2)
    LinearLayout layout2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main3_frag, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            return;
        initView();

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
                ToastHelper.showToast(getActivity(), "暂未开发");
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
