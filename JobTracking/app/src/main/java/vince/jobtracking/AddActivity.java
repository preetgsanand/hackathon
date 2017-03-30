package vince.jobtracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
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

public class AddActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private UserDetailEditFragment userDetailEditFragment;
    private JobDetailEditFragment jobDetailEditFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.activity = 5;
        setContentView(R.layout.activity_add);
        progressDialog = new ProgressDialog(AddActivity.this);
        progressDialog.setMessage("Syncing");
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",1);
        setFragment(id,null,null);
    }


    public void setFragment(int code,Job job,User user) {
        switch (code) {
            case 1:jobDetailEditFragment = JobDetailEditFragment.newInstance(0);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        jobDetailEditFragment).commit();
                break;
            case 2:userDetailEditFragment = UserDetailEditFragment.newInstance(0);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        userDetailEditFragment).commit();
                break;
            case 3:
                JobDetailFragment jobDetailFragment = JobDetailFragment.newInstance(job.getId());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        jobDetailFragment).commit();
                break;
            case 4:
                UserDetailFragment userDetailFragment = UserDetailFragment.newInstance(user.getId());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        userDetailFragment).commit();
                break;
        }
    }
    public void callAPI(int code, Job job, User user) {
        API api = new API(code,AddActivity.this);
        if (job != null) {
            api.setJob(job);
        }
        if (user != null) {
            api.setUser(user);
        }
        progressDialog.show();
        api.callAPI();
    }


    public void apiResult(int code,int result) {
        progressDialog.dismiss();
        switch (code) {
            case 11:jobDetailEditFragment.setResult(result);
                break;
            case 10:userDetailEditFragment.setResult(result);
                break;
        }
    }


}
