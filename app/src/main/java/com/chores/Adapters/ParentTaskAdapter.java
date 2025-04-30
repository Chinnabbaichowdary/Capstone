package com.chores.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chores.AssignTaskActivity;
import com.chores.Models.Child;
import com.chores.Models.Task;
import com.chores.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentTaskAdapter  extends BaseAdapter {
    List<Task> task;
    Context context;


    public ParentTaskAdapter(List<Task> parent, Context context) {
        this.context=context;
        this.task=parent;
    }

    @Override
    public int getCount() {
        return task.size();
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
        View obj2 = obj1.inflate(R.layout.child_task, null);

        Button btnAssign = (Button) obj2.findViewById(R.id.btnAssign);

        TextView tvname = (TextView) obj2.findViewById(R.id.tvname);
        tvname.setText(task.get(position).getTaskname());

        TextView tvdes = (TextView) obj2.findViewById(R.id.tvdes);
        tvdes.setText(task.get(position).getTaskdescription());

        TextView tvpoints = (TextView) obj2.findViewById(R.id.tvpoints);
        tvpoints.setText("Points : "+task.get(position).getTaskpoints());


        TextView tvdate = (TextView) obj2.findViewById(R.id.tvdate);
        tvdate.setText(task.get(position).getDat());

        TextView tvstatus = (TextView) obj2.findViewById(R.id.tvstatus);
        tvstatus.setText("Status : "+task.get(position).getStatus());

        if(task.get(position).getStatus().equals("Assign"))
        {
            btnAssign.setVisibility(View.VISIBLE);
        }
        else
        {
            btnAssign.setVisibility(View.GONE);
        }

        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // assignTaskToChild(task.get(position).getId(),spin_status.getSelectedItem().toString());

                Intent intent=new Intent(context, AssignTaskActivity.class);
                intent.putExtra("id",task.get(position).getId());
                intent.putExtra("name",task.get(position).getTaskname());
                context.startActivity(intent);


            }
        });

        return obj2;
    }

}