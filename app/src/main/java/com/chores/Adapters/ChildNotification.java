package com.chores.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chores.Models.Child;
import com.chores.Models.Notification;
import com.chores.R;

import java.util.List;

public class ChildNotification  extends BaseAdapter {
    List<Notification> child;
    Context context;


    public ChildNotification(List<Notification> parent, Context context) {
        this.context=context;
        this.child=parent;
    }

    @Override
    public int getCount() {
        return child.size();
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
        LayoutInflater obj1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View obj2 = obj1.inflate(R.layout.child_notification, null);

        TextView tvname = (TextView) obj2.findViewById(R.id.tvname);
        tvname.setText(child.get(position).getMsg());

        TextView tvdate = (TextView) obj2.findViewById(R.id.tvdate);
        tvdate.setText(child.get(position).getDat());

        return obj2;
    }
}