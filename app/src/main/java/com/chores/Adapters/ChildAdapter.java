package com.chores.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chores.Models.Child;
import com.chores.R;

import java.util.List;

public class ChildAdapter extends BaseAdapter {
    List<Child> child;
    Context context;


    public ChildAdapter(List<Child> parent, Context context) {
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
        View obj2 = obj1.inflate(R.layout.child_list, null);

        TextView tvname = (TextView) obj2.findViewById(R.id.tvname);
        tvname.setText(child.get(position).getName());

        TextView tvemail = (TextView) obj2.findViewById(R.id.tvemail);
        tvemail.setText(child.get(position).getEmail());

        TextView tvphone = (TextView) obj2.findViewById(R.id.tvphone);
        tvphone.setText(child.get(position).getPhone());
        return obj2;
    }
}