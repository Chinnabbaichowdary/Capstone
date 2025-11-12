package com.chores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chores.Api.ApiService;
import com.chores.Models.ResponseData;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateTaskActivity extends AppCompatActivity  implements EasyPermissions.PermissionCallbacks{

    TextView tvname,tvdes,tvpoints,tvdate,tvstatus,tvid;
    Spinner spin_status;
    Button btn_upload,btn_updatestatus;

    private static final String TAG = UpdateTaskActivity.class.getSimpleName();
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private static final String SERVER_PATH = "http://myprojectsworks.com/";
    private Uri uri;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);
        getSupportActionBar().setTitle("Tasks Details");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvid=(TextView)findViewById(R.id.tvid);
        tvname=(TextView) findViewById(R.id.tvname);
        tvdes=(TextView) findViewById(R.id.tvdes);
        tvpoints=(TextView) findViewById(R.id.tvpoints);
        tvdate=(TextView) findViewById(R.id.tvdate);
        tvstatus=(TextView) findViewById(R.id.tvstatus);

        tvid.setText("Task Id :" +getIntent().getStringExtra("id"));
        tvname.setText(getIntent().getStringExtra("name"));
        tvdes.setText(getIntent().getStringExtra("des"));
        tvpoints.setText(getIntent().getStringExtra("points"));
        tvdate.setText(getIntent().getStringExtra("dat"));
        tvstatus.setText(getIntent().getStringExtra("status"));

        spin_status=(Spinner) findViewById(R.id.spin_status);

        btn_upload=(Button) findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                openGalleryIntent.setType("image/*");
                startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
            }
        });


        btn_updatestatus=(Button) findViewById(R.id.btn_updatestatus);
        btn_updatestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageToServer(getIntent().getStringExtra("id"));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, UpdateTaskActivity.this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK){
            uri = data.getData();
            if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String filePath = getRealPathFromURIPath(uri, UpdateTaskActivity.this);
                file = new File(filePath);

            }else{
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }
    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
    File file;
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(uri != null){
            String filePath = getRealPathFromURIPath(uri, UpdateTaskActivity.this);
            file = new File(filePath);
            // uploadImageToServer();
        }
    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");

    }

    private void uploadImageToServer(final String id) {
        pd = new ProgressDialog(UpdateTaskActivity.this);
        pd.setTitle("Loading");
        pd.show();
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("status", spin_status.getSelectedItem().toString());
        Toast.makeText(UpdateTaskActivity.this, id+spin_status.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService uploadImage = retrofit.create(ApiService.class);
        Call<ResponseData> fileUpload = uploadImage.updateTaskStautsnew(fileToUpload, map);

        fileUpload.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                pd.dismiss();
                Toast.makeText(UpdateTaskActivity.this, "Uploaded successfully. ", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(UpdateTaskActivity.this, "Error" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}