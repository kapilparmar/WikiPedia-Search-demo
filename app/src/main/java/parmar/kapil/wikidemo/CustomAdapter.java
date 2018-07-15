package parmar.kapil.wikidemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by kapil on 7/15/2018.
 */
public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView desc;
        ImageView info;
        RelativeLayout rlListView;
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.list_layout, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;

        switch (v.getId())
        {
            case R.id.tv_name:

                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_layout, parent, false);
            viewHolder.txtName =  convertView.findViewById(R.id.tv_name);
            viewHolder.desc =  convertView.findViewById(R.id.tv_desc);
            viewHolder.info =  convertView.findViewById(R.id.list_image);
            viewHolder.rlListView =  convertView.findViewById(R.id.rl_list);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }/*
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);*/
        lastPosition = position;

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.desc.setText(dataModel.getDescription());
        if (!dataModel.getImage().equals("string")) {
            com.squareup.picasso.Picasso.with(mContext).
                    load(dataModel.getImage()).
                    placeholder(R.mipmap.ic_launcher).
                    into(viewHolder.info);
        }
        else {
            com.squareup.picasso.Picasso.with(mContext)
                    .load(R.mipmap.ic_launcher).
                    placeholder(R.mipmap.ic_launcher).
                    into(viewHolder.info);
        }
        /*viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);*/

        // Return the completed view to render on screen
        return convertView;
    }
}