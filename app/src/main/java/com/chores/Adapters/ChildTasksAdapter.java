package com.chores.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.chores.Api.ApiService;
import com.chores.Api.RetroClient;
import com.chores.AssignTaskActivity;
import com.chores.ChildDashboardActivity;
import com.chores.Models.ResponseData;
import com.chores.Models.Task;
import com.chores.R;
import com.chores.UpdateTaskActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildTasksAdapter extends BaseAdapter {
    List<Task> task;
    Context context;


    public ChildTasksAdapter(List<Task> parent, Context context) {
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
        View obj2 = obj1.inflate(R.layout.child_assign_tasks, null);

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

        CardView cdtask=(CardView) obj2.findViewById(R.id.cdtask);
        cdtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UpdateTaskActivity.class);
                intent.putExtra("id",task.get(position).getId());
                intent.putExtra("name",task.get(position).getTaskname());
                intent.putExtra("des",task.get(position).getTaskdescription());
                intent.putExtra("points",task.get(position).getTaskpoints());
                intent.putExtra("dat",task.get(position).getDat());
                intent.putExtra("status",task.get(position).getStatus());
                context.startActivity(intent);
            }
        });

//        if(task.get(position).getStatus().equals("Assign"))
//        {
//            btnAssign.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            btnAssign.setVisibility(View.GONE);
//        }
//
//        btnAssign.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // assignTaskToChild(task.get(position).getId(),spin_status.getSelectedItem().toString());
//
//                Intent intent=new Intent(context, AssignTaskActivity.class);
//                intent.putExtra("id",task.get(position).getId());
//                intent.putExtra("name",task.get(position).getTaskname());
//                context.startActivity(intent);
//
//
//            }
//        });
        Spinner spin_status=(Spinner)obj2.findViewById(R.id.spin_status);
        spin_status.setVisibility(View.GONE);
        Button btn_updatestatus=(Button)obj2.findViewById(R.id.btn_updatestatus);
        btn_updatestatus.setVisibility(View.GONE);


        btn_updatestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // updateRequestStauts(task.get(position).getId(),spin_status.getSelectedItem().toString());
            }
        });


        return obj2;
    }

    ProgressDialog progressDialog;
//    public void updateRequestStauts(String id,String status){
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("Loading....");
//        progressDialog.show();
//
//        ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
//        Call<ResponseData> call = service.updateTaskStauts(id,status);
//        call.enqueue(new Callback<ResponseData>() {
//            @Override
//            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
//                progressDialog.dismiss();
//                if(response.body()==null){
//                    Toast.makeText(context,"Server issue",Toast.LENGTH_SHORT).show();
//                }else {
//                    Intent intent=new Intent(context, ChildDashboardActivity.class);
//                    context.startActivity(intent);
//                    Toast.makeText(context,"Updated successfully",Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseData> call, Throwable t) {
//                progressDialog.dismiss();
//                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

}