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

import utils.MyUtil;


public class BlogListAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> mSelfData;

    public BlogListAdapter(Context context, List<Map<String, Object>> data) {
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
        ViewHolder holder;
        Map<String,?>item=mSelfData.get(position);
        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.blog_list_item,null);
            holder.author= (TextView) convertView.findViewById(R.id.author);
            holder.title= (TextView) convertView.findViewById(R.id.title);
            holder.created_time= (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.author.setText(item.get("author").toString());
        holder.title.setText(item.get("title").toString());
        holder.created_time.setText(MyUtil.timeToDate(item.get("time").toString()));
        return convertView;
    }
    class ViewHolder{
        TextView author;
        TextView title;
        TextView created_time;

    }
}
