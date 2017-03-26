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
        return mSelfData.size()+1;
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
            holder = new ViewHolder();
            holder.imageview= (ImageView) convertView.findViewById(R.id.imageview);
            holder.text= (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position==0){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_group_item, null);
            holder.peroid_txt= (TextView) convertView.findViewById(R.id.group_name);
            holder.peroid_txt.setText(item.get("peroid").toString());
        }else{
            Glide.with(context).load(item.get("avatar").toString()).override(290, 350).centerCrop().error(R.drawable.noimg).into(holder.imageview);
            holder.text.setText(item.get("name_kanji").toString());
        }


        return convertView;
    }


    class ViewHolder {
        TextView peroid_txt;
        ImageView imageview;
        TextView text;

    }
}
