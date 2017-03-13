package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nogizaka46.Main2Frag_Tab2;
import org.nogizaka46.R;

import java.util.List;
import java.util.Map;



public class EuclidListAdapter extends ArrayAdapter<Map<String, Object>> {
     List<Map<String, Object>> mSelfData;
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION_SHORT = "description_short";
    public static final String KEY_DESCRIPTION_FULL = "description_full";
    LayoutInflater mInflater;

    public EuclidListAdapter(Context context, int layoutResourceId, List<Map<String, Object>> data) {
        super(context, layoutResourceId, data);
        mSelfData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mSelfData.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final Map<String,?>item=mSelfData.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mViewOverlay = convertView.findViewById(R.id.view_avatar_overlay);
            viewHolder.mListItemAvatar = (ImageView) convertView.findViewById(R.id.image_view_avatar);
            viewHolder.mListItemName = (TextView) convertView.findViewById(R.id.text_view_name);
            viewHolder.mListItemDescription = (TextView) convertView.findViewById(R.id.text_view_description);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(getContext()).load(item.get("avatar").toString())
                .resize(Main2Frag_Tab2.sScreenWidth, Main2Frag_Tab2.sProfileImageHeight).centerCrop()
                .placeholder(R.color.white)
                .into(viewHolder.mListItemAvatar);

        viewHolder.mListItemName.setText(item.get("name_kanji").toString().toUpperCase());
        //viewHolder.mViewOverlay.setBackground(Main2Frag_Tab2.sOverlayShape);
        return convertView;
    }
    static class ViewHolder {
        View mViewOverlay;
        ImageView mListItemAvatar;
        TextView mListItemName;
        TextView mListItemDescription;
    }
}
