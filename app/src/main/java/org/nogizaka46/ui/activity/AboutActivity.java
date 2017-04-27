package org.nogizaka46.ui.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.nogizaka46.R;
import org.nogizaka46.base.BaseActivity;

public class AboutActivity extends BaseActivity {
	   private TextView show_version;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		ImageButton left_layout=(ImageButton) findViewById(R.id.top_button_back);
	    left_layout.setVisibility(View.VISIBLE);
		TextView textView=(TextView) findViewById(R.id.title);
		textView.setText(R.string.about);
		show_version=(TextView) findViewById(R.id.show_version);
		show_version.setText("V"+getVersionCode(AboutActivity.this));
		
	}

	public void back(View view){
		this.finish();
	}
	public void doActionRight(View view){
		
	}
	public static int getVersionCode(Context context) {
		int version = 0;
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = pi.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e("TAG", "Package name not found", e);
		};
		return version;
	}
}
