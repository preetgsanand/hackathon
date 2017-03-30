package vince.jobtracking.InitializationFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;

import vince.jobtracking.InitializationActivity;
import vince.jobtracking.R;


public class OTPFragment extends Fragment implements View.OnClickListener{
    private View view;


    private DigitsAuthButton authButton;
    private Button continueOtp;
    private TextView otpTitle,subText;
    private ViewGroup.LayoutParams buttonlp,buttonAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ot, container, false);
        initializeViews();
        return view;
    }

    private void initializeViews() {
        authButton = (DigitsAuthButton) view.findViewById(R.id.auth_button);
        authButton.setBackgroundResource(R.drawable.transparent_button);
        otpTitle = (TextView) view.findViewById(R.id.otpTitle);
        continueOtp = (Button) view.findViewById(R.id.continueOtp);
        subText = (TextView) view.findViewById(R.id.textView4);

        continueOtp.setOnClickListener(this);

        buttonlp= (ViewGroup.LayoutParams)continueOtp.getLayoutParams();
        buttonAuth= (ViewGroup.LayoutParams)authButton.getLayoutParams();


        authButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // TODO: associate the session userID with your user model
                InitializationActivity.phoneNumber = phoneNumber;

                buttonlp.height = 100;
                continueOtp.setLayoutParams(buttonlp);
                buttonAuth.height = 0;
                authButton.setLayoutParams(buttonAuth);
                otpTitle.setText("Verification Successful");
                subText.setText("Your phone number has been verified");


            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continueOtp: ((InitializationActivity)getActivity()).setFragment(3);
                break;

        }
    }




}
