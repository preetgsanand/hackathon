package vince.jobtracking.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vince.jobtracking.Database.User;
import vince.jobtracking.R;
import vince.jobtracking.Utils.Utils;

/**
 * Created by vince on 3/20/17.
 */
public class CustomUserListAdapter extends RecyclerView.Adapter<CustomUserListAdapter.MyViewHolder> {

    private List<User> users;
    private Context context;
    public CustomUserListAdapter(Context context,List<User> users) {
        this.users = users;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.custom_user_list_row,
                parent,
                false
        );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.userName.setText(users.get(position).getName());
        holder.userEmail.setText(users.get(position).getEmail());
        holder.userDob.setText(Utils.LongToDate(users.get(position).getDob()));
        holder.userIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.user));
        holder.userDept.setText(context.getResources().getStringArray(R.array.departments)[(int)users.get(position).getDepartment()-1]);
        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView userName;
        public ImageView userIcon;
        public TextView userEmail;
        public TextView userDob,userDept;

        public MyViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.userName);
            userIcon = (ImageView) itemView.findViewById(R.id.userIcon);
            userEmail = (TextView) itemView.findViewById(R.id.userEmail);
            userDob = (TextView) itemView.findViewById(R.id.userDob);
            userDept = (TextView) itemView.findViewById(R.id.userDept);

        }
    }
}
