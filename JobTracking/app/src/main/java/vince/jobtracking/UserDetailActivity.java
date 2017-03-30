package vince.jobtracking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import vince.jobtracking.Database.Job;
import vince.jobtracking.Database.User;
import vince.jobtracking.JobDetailFragments.JobDetailEditFragment;
import vince.jobtracking.JobDetailFragments.JobDetailFragment;
import vince.jobtracking.UserDetailFragments.UserDetailEditFragment;
import vince.jobtracking.UserDetailFragments.UserDetailFragment;
import vince.jobtracking.Utils.API;
import vince.jobtracking.Utils.Utils;

public class UserDetailActivity extends AppCompatActivity {


    private long id;
    private ProgressDialog progressDialog;
    private UserDetailFragment userDetailFragment;
    private UserDetailEditFragment userDetailEditFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.activity = 4;
        setContentView(R.layout.activity_user_detail);
        progressDialog = new ProgressDialog(UserDetailActivity.this);
        progressDialog.setMessage("Syncing");
        Intent intent = getIntent();
        id = intent.getLongExtra("id",0);
        setFragment(1);
    }


    public void setFragment(int code) {
        switch (code) {
            case 1:userDetailFragment = UserDetailFragment.newInstance(id);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        userDetailFragment).commit();
                break;
            case 2:userDetailEditFragment = UserDetailEditFragment.newInstance(id);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        userDetailEditFragment).commit();
                break;
        }
    }

    public void callAPI(int code, User user) {
        API api = new API(code,UserDetailActivity.this);
        if(user != null) {
            api.setUser(user);
        }
        progressDialog.show();
        api.callAPI();
    }


    public void apiResult(int code,int result) {
        progressDialog.dismiss();
        switch (code) {
            case 1:
                userDetailEditFragment.setResult(result);
                break;
        }

    }


}
