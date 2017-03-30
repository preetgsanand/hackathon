package vince.jobtracking;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.digits.sdk.android.Digits;
import com.orm.SugarContext;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;
import vince.jobtracking.Database.User;
import vince.jobtracking.InitializationFragments.InitialProfileCheckFragment;
import vince.jobtracking.InitializationFragments.IntroDetailInputFragment;
import vince.jobtracking.InitializationFragments.IntroFragment;
import vince.jobtracking.InitializationFragments.IntroSyncFragment;
import vince.jobtracking.InitializationFragments.OTPFragment;
import vince.jobtracking.Utils.API;
import vince.jobtracking.Utils.Utils;

public class InitializationActivity extends AppCompatActivity {

    private static final String TWITTER_KEY = "nrdZchPnbE5YPZMK5HmWbk8vc";
    private static final String TWITTER_SECRET = "1PWRtLjlTVzCXGf1AEh0Oo3plJragD5bcSkDIRAbnghsvxNBGY";
    public static String phoneNumber;
    private IntroFragment introFragment;
    private OTPFragment otpFragment;
    private InitialProfileCheckFragment initialProfileCheckFragment;
    private IntroSyncFragment introSyncFragment;
    private IntroDetailInputFragment introDetailInputFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.activity = 2;
        setContentView(R.layout.activity_initialization);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
        SugarContext.init(this);
        setFragment(1);
    }


    public void setFragment(int code) {
        switch (code) {
            case 1:getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new IntroFragment()).commit();
                break;
            case 2:getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new OTPFragment()).addToBackStack(null).commit();
                break;
            case 4:
                introSyncFragment = IntroSyncFragment.newInstance(phoneNumber,
                        User.findById(User.class,1).getRole());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    introSyncFragment).addToBackStack(null).commit();
                break;
            case 5:
                introDetailInputFragment = IntroDetailInputFragment.newInstance(phoneNumber);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    introDetailInputFragment).addToBackStack("null").commit();
                break;
            case 3:
                initialProfileCheckFragment = InitialProfileCheckFragment.newInstance(phoneNumber);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    initialProfileCheckFragment).addToBackStack(null).commit();
                break;
        }
    }

    public void setMainActivity() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void askPermissions() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);

        }
    }
    public void setAdminActivity() {
        Intent intent = new Intent(getApplicationContext(),AdminActivity.class);
        startActivity(intent);
        finish();
    }


    public void callAPI(int code,String phoneNumber,User user) {
        API api = new API(code,InitializationActivity.this);
        if(phoneNumber != null) {
            api.setPhoneNumber(phoneNumber);
        }
        if(user != null) {
            api.setUser(user);
        }
        api.callAPI();
    }


    public void apiResult(int code,int result) {
        switch (code) {
            case 9:
                introSyncFragment.setApiResult(result);
                break;
            case 10:introDetailInputFragment.setApiResult(result);
                break;
            case 12:initialProfileCheckFragment.setApiResult(result);
                break;
            case 13:
                introSyncFragment.setApiResult(result);
                break;
        }
    }
}
