package vince.jobtracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import vince.jobtracking.Database.Job;
import vince.jobtracking.JobDetailFragments.JobDetailEditFragment;
import vince.jobtracking.JobDetailFragments.JobDetailFragment;
import vince.jobtracking.Utils.API;
import vince.jobtracking.Utils.Utils;

public class JobDetailActivity extends AppCompatActivity {

    private long id;
    private ProgressDialog progressDialog;
    private JobDetailFragment jobDetailFragment;
    private JobDetailEditFragment jobDetailEditFragment;
    private int fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.activity = 3;
        setContentView(R.layout.activity_job_detail);
        progressDialog = new ProgressDialog(JobDetailActivity.this);
        progressDialog.setMessage("Syncing");
        Intent intent = getIntent();
        id = intent.getLongExtra("id",1);
        setFragment(1);


    }

    public void setFragment(int code) {
        switch (code) {
            case 1:jobDetailFragment = JobDetailFragment.newInstance(id);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        jobDetailFragment).commit();
                break;
            case 2:jobDetailEditFragment = JobDetailEditFragment.newInstance(id);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        jobDetailEditFragment).commit();
                break;
        }
    }

    public void callAPI(int code,Job job,int fragment) {
        this.fragment = fragment;
        API api = new API(code,JobDetailActivity.this);
        if(job != null) {
            api.setJob(job);
        }
        progressDialog.show();
        api.callAPI();
    }


    public void apiResult(int code,int result) {
        progressDialog.dismiss();
        switch (code) {
            case 4:
                switch (fragment) {
                    case 1:jobDetailFragment.setResult(result);
                        break;
                    case 2:jobDetailEditFragment.setResult(result);
                        break;
                }

        }
    }

    public void onResume(){
        super.onResume();
        Utils.activity = 3;

    }
}
