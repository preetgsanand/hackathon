package vince.jobtracking.JobDetailFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import vince.jobtracking.Database.Job;
import vince.jobtracking.Database.User;
import vince.jobtracking.JobDetailActivity;
import vince.jobtracking.MainActivity;
import vince.jobtracking.R;
import vince.jobtracking.Utils.Utils;


public class JobDetailFragment extends Fragment implements View.OnClickListener{


    public static long id;
    private View view;
    private Job job;
    private Button submitrequest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        (getActivity()).setTitle("DETAIL");

        view = inflater.inflate(R.layout.fragment_job_detail, container, false);
        if(Utils.MainAdminActivity == 1) {
            setHasOptionsMenu(true);
        }
        else {
            setHasOptionsMenu(false);
        }
        initializeViews(view);
        return view;
    }


    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.job_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit: ((JobDetailActivity)getActivity()).setFragment(2);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public static JobDetailFragment newInstance(long position) {

        id = position;
        JobDetailFragment fragment = new JobDetailFragment();
        return fragment;
    }

    private void initializeViews(View view) {
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView deadline = (TextView) view.findViewById(R.id.deadline);
        TextView added = (TextView) view.findViewById(R.id.added);
        TextView assignedTo = (TextView) view.findViewById(R.id.assignedTo);
        submitrequest = (Button) view.findViewById(R.id.submitrequest);

        submitrequest.setOnClickListener(this);

        job = Job.findById(Job.class,id);

        name.setText(job.getName());
        description.setText(job.getDescription());
        deadline.setText(Utils.LongToDate(job.getDeadline()));
        added.setText(Utils.LongToDate(job.getAdded()));
        assignedTo.setText(User.find(User.class,"webid=?",""+job.getUserId()).get(0).getName());

        if(job.getStatus() == 1 || job.getStatus() == 2) {
            ViewGroup.LayoutParams submitlp = (ViewGroup.LayoutParams)submitrequest.getLayoutParams();
            submitlp.height = 0;
            submitrequest.setLayoutParams(submitlp);
        }

        if(Utils.MainAdminActivity == 0) {
            if(job.getSubmitRequest() == 1) {
                submitrequest.setText("Submission Requested");
            }
        }
        else if(Utils.MainAdminActivity == 1) {
            if(job.getSubmitRequest() == 1) {
                submitrequest.setText("Accept Submission");
            }
            else if(job.getStatus() == 3 && job.getSubmitRequest() == 2) {
                ViewGroup.LayoutParams submitlp = (ViewGroup.LayoutParams)submitrequest.getLayoutParams();
                submitlp.height = 0;
                submitrequest.setLayoutParams(submitlp);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitrequest:
               if(Utils.MainAdminActivity == 0) {
                   if(job.getSubmitRequest() == 2) {
                       submitMainRequest(0);
                   }
               }
                else {
                   if(job.getSubmitRequest() == 1) {
                       submitMainRequest(1);
                   }
               }
                break;
        }
    }

    private void submitMainRequest(int code) {
        if (code == 0) {
            job.setSubmitRequest(1);
        } else {
            job.setSubmitRequest(2);
            job.setStatus(2);

        }

        ((JobDetailActivity)getActivity()).callAPI(4,job,1);
    }

    public void setResult(int code) {
        Log.e("Inside","Job Detil Fragment Result");
        if(code == -1) {
            Toast.makeText(getContext(),
                    "Sync Failed",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            job.save();
            if (Utils.MainAdminActivity == 0) {
                submitrequest.setText("Submission Requested");
            } else if (Utils.MainAdminActivity == 1) {
                submitrequest.setText("Accepted");
            }
        }
    }

}
