package vince.jobtracking.MainFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import vince.jobtracking.Adapters.CustomJobListAdapter;
import vince.jobtracking.Database.Job;
import vince.jobtracking.MainActivity;
import vince.jobtracking.R;
import vince.jobtracking.Utils.Utils;


public class JobListFragment extends Fragment {


    private View view;
    public List<Job> jobs;
    private RecyclerView recyclerView;
    public int activityCode;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_job_list, container, false);
        initializeRecycler();
        return view;
    }



    final GestureDetector mGestureDetector = new GestureDetector(getContext(),
            new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

    });


    private void initializeRecycler() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        setRecyclerView(jobs);
    }


    public void setRecyclerView(List<Job> job) {
        final List<Job> jobList = job;
        CustomJobListAdapter adapter = new CustomJobListAdapter(getContext(),jobList);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mGestureDetector.onTouchEvent(e)) {

                    int a=rv.getChildPosition(child);
                    Log.e("Listener Id",jobList.get(a).getId()+"");
                    if(Utils.activity == 0)
                        ((MainActivity)getActivity()).setDetailActivity(jobList.get(a).getId());



                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

}
