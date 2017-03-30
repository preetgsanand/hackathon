package vince.jobtracking.InitializationFragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import vince.jobtracking.Database.User;
import vince.jobtracking.InitializationActivity;
import vince.jobtracking.R;
import vince.jobtracking.Utils.Utils;


public class IntroDetailInputFragment extends Fragment implements View.OnClickListener{

    private View view;
    private EditText name,email,dob;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static String phoneNumber;
    private Spinner departmentSpinner;
    private ProgressDialog progress;
    private User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_intro_detail_input, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        name = (EditText) view.findViewById(R.id.name);
        email = (EditText) view.findViewById(R.id.email);
        dob = (EditText) view.findViewById(R.id.dob);
        departmentSpinner = (Spinner) view.findViewById(R.id.departmentSpinner);
        Button selectDob = (Button) view.findViewById(R.id.selectDob);
        Button submit = (Button) view.findViewById(R.id.submit);


        String[] departments = getResources().getStringArray(R.array.departments);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1,
                departments);
        departmentSpinner.setAdapter(arrayAdapter);


        selectDob.setOnClickListener(this);
        submit.setOnClickListener(this);
    }


    public static IntroDetailInputFragment newInstance(String phone) {
        IntroDetailInputFragment introDetailInputFragment = new IntroDetailInputFragment();
        phoneNumber = phone;
        return introDetailInputFragment;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectDob:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
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

            case R.id.submit:
                progress = new ProgressDialog(getContext());
                progress.setMessage("Registering");
                progress.show();
                constructUser();

        }
    }




    private void constructUser() {
        User user = new User();
        user.setName(name.getText().toString());
        user.setDob(Utils.DateToLong(dob.getText().toString()+"T00:00:00Z"));
        user.setDepartment(departmentSpinner.getSelectedItemPosition());
        user.setEmail(email.getText().toString());
        user.setPhone(phoneNumber);
        this.user = user;
        callUserAddApi(user);
    }


    private void callUserAddApi(User user) {
        ((InitializationActivity)getActivity()).callAPI(10,null,user);
    }

    public void setApiResult(int result) {
        progress.dismiss();
        switch (result) {
            case -1:Toast.makeText(getContext(),
                    "Registration Failed",
                    Toast.LENGTH_SHORT).show();
                break;
            case 1:Toast.makeText(getContext(),
                    "Registration Successful",
                    Toast.LENGTH_SHORT).show();
                user.save();
                ((InitializationActivity)getActivity()).setMainActivity();
                break;
        }
    }

}
