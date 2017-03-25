package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.nogizaka46.R;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ShowMemberAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> mSelfData;

    public ShowMemberAdapter(Context context, List<Map<String, Object>> data) {
        this.context = context;
        this.mSelfData = data;
    }

    @Override
    public int getCount() {
        return mSelfData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Map<String, ?> item = mSelfData.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.member_grid_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(item.get("avatar").toString()).override(290, 350).centerCrop().error(R.drawable.noimg).into(holder.imageview);
        holder.text.setText(item.get("name_kanji").toString());
        return convertView;
    }


    class ViewHolder {
        @InjectView(R.id.imageview)
        ImageView imageview;
        @InjectView(R.id.text)
        TextView text;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
