package vince.jobtracking.InitializationFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import vince.jobtracking.InitializationActivity;
import vince.jobtracking.R;
import vince.jobtracking.Utils.Utils;

public class IntroSyncFragment extends Fragment implements View.OnClickListener{


    private static String phoneNumber;
    private ProgressDialog progress;
    private View view;
    private Button retry,cont;
    private TextView title,subtitle;
    private ViewGroup.LayoutParams buttonCont,buttonRetry;
    private static long userRole;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_intro_sync, container, false);
        initializeViews();
        return view;
    }

    private void initializeViews() {
        retry = (Button) view.findViewById(R.id.retry);
        cont = (Button) view.findViewById(R.id.continueOtp);
        title = (TextView) view.findViewById(R.id.checkTitle);
        subtitle = (TextView) view.findViewById(R.id.checkSubtitle);

        retry.setOnClickListener(this);
        cont.setOnClickListener(this);

        buttonCont= (ViewGroup.LayoutParams)cont.getLayoutParams();
        buttonRetry= (ViewGroup.LayoutParams)retry.getLayoutParams();

        progress = new ProgressDialog(getContext());
        progress.setMessage("Syncing Data");
        progress.show();

        if(userRole == 2) {
            callJobAPI(phoneNumber);
        }
        else {
            callRefreshApi();
        }
    }

    private void setRetry() {
        buttonRetry.height = 100;
        retry.setLayoutParams(buttonRetry);
        subtitle.setText("Cannot connect to server properly");
        title.setText("Sync Failed");
        progress.dismiss();
    }

    private void setContinue(int code) {
        buttonCont.height = 100;
        cont.setLayoutParams(buttonCont);
        if (code == 0) {
            subtitle.setText("There are no jobs associated with this account");
        }
        progress.dismiss();
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continueOtp:
                if(userRole == 2) {
                    ((InitializationActivity) getActivity()).setMainActivity();
                }
                else {
                    ((InitializationActivity)getActivity()).setAdminActivity();
                }
                break;
            case R.id.retry:
                if(userRole == 2) {
                    callJobAPI(phoneNumber);
                }
                else {
                    callRefreshApi();
                }
                break;
        }
    }


    public static IntroSyncFragment newInstance(String phone,long userrole) {
        IntroSyncFragment introSyncFragment = new IntroSyncFragment();
        phoneNumber = phone;
        userRole = userrole;
        return introSyncFragment;
    }


    private void callJobAPI(String phoneNumber) {
        progress.show();
        ((InitializationActivity)getActivity()).callAPI(9,phoneNumber,null);
    }

    private void callRefreshApi() {
        progress.show();
        ((InitializationActivity)getActivity()).callAPI(13,null,null);
    }

    public void setApiResult(int result) {
        progress.dismiss();
        switch (result) {
            case -1:setRetry();
                break;
            case 0:
                setContinue(0);
                break;
            case 1:
                setContinue(1);
                break;
        }
    }
}
