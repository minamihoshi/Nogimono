package org.nogizaka46.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.nogizaka46.R;
import org.nogizaka46.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


//修改界面
public class EditUserInfoNeedActivity extends BaseActivity {


    @InjectView(R.id.login_back)
    ImageView loginBack;
    @InjectView(R.id.main_header)
    TextView mainHeader;
    @InjectView(R.id.register)
    TextView register;
    @InjectView(R.id.editText)
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info_need);
        ButterKnife.inject(this);


        init();
    }

    private int index = 0;

    private void init() {
        Intent getIntent = getIntent();
        String title = getIntent.getStringExtra("title");
        String hint = getIntent.getStringExtra("hint");
        index = getIntent.getIntExtra("index", 1);
        int maxLength = getIntent.getIntExtra("maxLength", 20);
        editText.setHint(hint);
        //editText.setMaxEms(maxLength);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        mainHeader.setText(title);

//        editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//        editText.setGravity(Gravity.TOP);
//改变默认的单行模式
//水平滚动设置为False
//        editText.setHorizontallyScrolling(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setHint(null);
            }
        });

    }

    public void SendMessageValue(String res) {
//        Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
        Intent rIntent = new Intent();
        rIntent.putExtra("res", res);
        rIntent.putExtra("index", index);
        setResult(120, rIntent);
        finish();
    }


    @OnClick({R.id.login_back, R.id.register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_back:
                finish();
                break;
            case R.id.register:
                Intent intent = new Intent();
                intent.putExtra("text", editText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }


}
