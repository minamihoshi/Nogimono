package org.nogizaka46.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIPackageHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import org.nogizaka46.R;
import org.nogizaka46.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.about_list)
    QMUIGroupListView aboutList;
    @BindView(R.id.copyright)
    TextView copyright;
    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        ButterKnife.bind(this);


        initTopBar();

        version.setText(QMUIPackageHelper.getAppVersion(this));


        QMUICommonListItemView itemView  = aboutList.createItemView("联系我们");
        itemView.setDetailText("QQ:480242179");
        QMUIGroupListView.newSection(this)
                .addItemView(itemView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        String url = "http://qmuiteam.com/android/page/index.html";
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setData(Uri.parse(url));
//                        startActivity(intent);
                    }
                })
                .addItemView(aboutList.createItemView("Github"), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://github.com/minamihoshi/Nogimono";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                })
                .addTo(aboutList);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.CHINA);
        String currentYear = dateFormat.format(new Date());
        copyright.setText(String.format(getResources().getString(R.string.about_copyright), currentYear));

    }

    private void initTopBar() {
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        topbar.setTitle("关于");
    }
    public static String getVersionname(Context context) {
        String version = null;
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("TAG", "Package name not found", e);
        }
        ;
        return version;
    }
}
