package vince.jobtracking.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import vince.jobtracking.Database.Job;
import vince.jobtracking.R;
import vince.jobtracking.Utils.Utils;

public class CustomJobListAdapter extends RecyclerView.Adapter<CustomJobListAdapter.MyViewHolder> {

    private List<Job> jobs;
    private Context context;
    public CustomJobListAdapter(Context context,List<Job> jobs) {
        this.jobs = jobs;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.custom_job_list_row,
                parent,
                false
        );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.jobName.setText(jobs.get(position).getName());
        holder.jobDescription.setText(jobs.get(position).getDescription());
        holder.jobAdded.setText(Utils.LongToDate(jobs.get(position).getAdded()));
        holder.jobIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.job));
        switch (jobs.get(position).getStatus()) {
            case 1:holder.jobStatus.setText("ABANDONED");
                holder.jobStatus.setTextColor(context.getResources().getColor(R.color.abandoned));
                break;
            case 2:holder.jobStatus.setText("COMPLETED");
                holder.jobStatus.setTextColor(context.getResources().getColor(R.color.completed));
                break;
            case 3:holder.jobStatus.setText("ONGOING");
                holder.jobStatus.setTextColor(context.getResources().getColor(R.color.ongoing));
                break;
        }

        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView jobName;
        public ImageView jobIcon;
        public TextView jobDescription;
        public TextView jobAdded,jobStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            jobName = (TextView) itemView.findViewById(R.id.jobTitle);
            jobIcon = (ImageView) itemView.findViewById(R.id.jobIcon);
            jobDescription = (TextView) itemView.findViewById(R.id.jobDescription);
            jobAdded = (TextView) itemView.findViewById(R.id.jobAdded);
            jobStatus = (TextView) itemView.findViewById(R.id.jobStatus);

        }
    }
}
