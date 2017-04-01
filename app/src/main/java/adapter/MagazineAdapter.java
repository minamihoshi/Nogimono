package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.nogizaka46.R;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import utils.MyUtil;


public class MagazineAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> mSelfData;

    public MagazineAdapter(Context context, List<Map<String, Object>> data) {
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
        Map<String,Object>item=mSelfData.get(position);
        if (convertView!=null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.member_grid_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.title.setText(item.get("title").toString());
        holder.delivery.setText(MyUtil.timeToDate(item.get("delivery").toString()));
        holder.summary.setText(item.get("summary").toString());


        return convertView;
    }


     class ViewHolder {
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.delivery)
        TextView delivery;
        @InjectView(R.id.summary)
        TextView summary;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
