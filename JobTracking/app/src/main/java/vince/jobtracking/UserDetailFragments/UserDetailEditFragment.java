package vince.jobtracking.UserDetailFragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import vince.jobtracking.AddActivity;
import vince.jobtracking.Database.User;
import vince.jobtracking.JobDetailActivity;
import vince.jobtracking.R;
import vince.jobtracking.UserDetailActivity;
import vince.jobtracking.Utils.Utils;


public class UserDetailEditFragment extends Fragment implements View.OnClickListener{

    private View view;
    private static long id;
    private EditText name,email,dob,phone;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Spinner departmentSpinner;

    private User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_detail_edit, container, false);
        setHasOptionsMenu(true);
        initializeViews();
        return view;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.job_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                editUser();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public static UserDetailEditFragment newInstance(long ID) {
        UserDetailEditFragment userDetailEditFragment = new UserDetailEditFragment();
        id = ID;
        return userDetailEditFragment;
    }

    private void initializeViews() {
        user = User.findById(User.class,id);
        name = (EditText) view.findViewById(R.id.name);
        email = (EditText) view.findViewById(R.id.email);
        dob = (EditText) view.findViewById(R.id.dob);
        departmentSpinner = (Spinner) view.findViewById(R.id.departmentSpinner);
        phone = (EditText) view.findViewById(R.id.phone);
        Button selectDob = (Button) view.findViewById(R.id.selectDob);


        String[] departments = getResources().getStringArray(R.array.departments);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1,
                departments);
        departmentSpinner.setAdapter(arrayAdapter);


        selectDob.setOnClickListener(this);
        if(id != 0) {
            name.setText(user.getName());
            email.setText(user.getEmail());
            dob.setText(Utils.LongToDate(user.getDob()));
            departmentSpinner.setSelection((int)user.getDepartment()-1);
            phone.setText(user.getPhone());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectDob:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH)+1;
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dob.setText(year + "-" + String.format("%02d", monthOfYear) + "-" +
                                        String.format("%02d", dayOfMonth));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;

        }
    }

    public void editUser() {
        if(user == null) {
            user = new User();
        }
        user.setPhone(phone.getText().toString());
        user.setName(name.getText().toString());
        user.setEmail(email.getText().toString());
        user.setDepartment(departmentSpinner.getSelectedItemPosition()+1);
        user.setDob(Utils.DateToLong(dob.getText().toString()));

        Log.e("User Id",user.getWebid()+"");


        if(Utils.activity == 4) {
            ((UserDetailActivity)getActivity()).callAPI(1,user);
        }
        else if(Utils.activity == 5) {
            ((AddActivity)getActivity()).callAPI(10,null,user);
        }


    }

    public void setResult(int result) {
        switch (result) {
            case -1:
                Toast.makeText(getContext(),
                        "Submit Failed",
                        Toast.LENGTH_SHORT).show();
                break;
            case 1:Toast.makeText(getContext(),
                    "Submission Successful",
                    Toast.LENGTH_SHORT).show();
                user.save();


                if (Utils.activity == 3) {
                    ((UserDetailActivity)getActivity()).setFragment(1);
                }
                else if(Utils.activity == 5) {
                    ((AddActivity)getActivity()).setFragment(4,null,user);
                }
        }
    }
}
