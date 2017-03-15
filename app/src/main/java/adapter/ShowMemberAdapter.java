package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.nogizaka46.R;
import java.util.List;
import java.util.Map;


public class ShowMemberAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> mSelfData;

    public ShowMemberAdapter(Context context, List<Map<String, Object>> data) {
        this.context=context;
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
        ViewHolder holder=new ViewHolder();
        Map<String,?>item=mSelfData.get(position);
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.member_grid_item,null);
            holder.img= (ImageView) convertView.findViewById(R.id.imageview);
            holder.txts= (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load(item.get("avatar").toString()).resize(290,350).centerCrop().error(R.drawable.noimg).into(holder.img);
        holder.txts.setText(item.get("name_kanji").toString());
        return convertView;
    }
    class ViewHolder{
        ImageView img;
        TextView txts;
    }
}
