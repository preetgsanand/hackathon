package vince.jobtracking;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vince.jobtracking.Adapters.CustomPagerAdapter;
import vince.jobtracking.Database.Job;
import vince.jobtracking.Database.User;
import vince.jobtracking.MainFragments.JobListFragment;
import vince.jobtracking.Utils.API;
import vince.jobtracking.Utils.Utils;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    ProgressDialog progressDialog;
    private DrawerLayout drawerLayout;
    private ActionBar mActionBar;
    private ViewPager pager;
    private TabLayout tabLayout;
    private CustomPagerAdapter pagerAdapter;
    private List<Fragment> fragments;
    private List<Job> jobs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.activity = 0;
        Utils.MainAdminActivity = 0;
        setContentView(R.layout.activity_main);
        mActionBar = getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.navigation);

        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        checkIntitalSetup();
    }


    private void closeKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)
                MenuItemCompat.getActionView(menu.findItem(R.id.search));

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        Utils.activity = 0;
        Utils.MainAdminActivity = 0;
        refreshPager();

    }

    private void setUI() {
        //-----------Drawer Setting-----------
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) findViewById(R.id.left_drawer);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                new String[] {});
        drawerList.setAdapter(adapter);
        View view = getLayoutInflater().inflate(R.layout.navigation_header,null);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView phone = (TextView) view.findViewById(R.id.phone);
        TextView email = (TextView) view.findViewById(R.id.email);
        TextView dob = (TextView) view.findViewById(R.id.dob);
        TextView dept = (TextView) view.findViewById(R.id.dept);
        TextView effeciency = (TextView) view.findViewById(R.id.effeciency);

        User user = User.findById(User.class,1);
        name.setText(user.getName());
        phone.setText(user.getPhone());
        email.setText(user.getEmail());
        dob.setText(Utils.LongToDate(user.getDob()));
        dept.setText(getResources().getStringArray(R.array.departments)[(int)(user.getDepartment()-1)]);
        int abandoned = Job.find(Job.class,"status=?",1+"").size();
        int completed = Job.find(Job.class,"status=?",2+"").size();
        if(completed != 0) {
            effeciency.setText((completed * 100 / (abandoned + completed)) + " %");
        }
        else {
            effeciency.setText(0+" %");
        }

        drawerList.addHeaderView(view);

        //------------Pager Setting--------------
        fragments = new ArrayList<>();

        fragments.add(new JobListFragment());
        fragments.add(new JobListFragment());
        fragments.add(new JobListFragment());
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.viewpager);

        pagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(),
                pager,tabLayout,fragments);
        pager.setAdapter(pagerAdapter);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                String phone = User.findById(User.class,1).getPhone();
                callAPI(9,phone);
                return true;
            case R.id.sort:
                setSortOptions();
                return true;
            case android.R.id.home:
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void checkIntitalSetup() {
       User user = User.findById(User.class,1);
        if(user == null) {
            callInitializationActivity();
        }
        else if (user.getRole() == 1) {
            setAdminActivity();
        }
        else {
            jobs = Job.listAll(Job.class);
            setUI();
            closeKeyboard();

        }
    }

    private void callInitializationActivity() {
        Intent intent = new Intent(getApplicationContext(),InitializationActivity.class);
        startActivity(intent);
        finish();
    }


    private void setAdminActivity() {
        Intent intent = new Intent(getApplicationContext(),AdminActivity.class);
        startActivity(intent);
        finish();
    }



    public void setDetailActivity(long id) {
        Intent intent = new Intent(getApplicationContext(),JobDetailActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);

    }




    private void setSortOptions() {
        final Dialog alertDialog = new Dialog(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.display_options_listview, null);
        alertDialog.setContentView(convertView);
        ListView lv = (ListView) convertView.findViewById(R.id.displayOptionsList);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialog.dismiss();
                JobListFragment jobListFragment = (JobListFragment) fragments.get(pager.getCurrentItem());
                int pagerPos = pager.getCurrentItem();
                List<Job> jobs = jobListFragment.jobs;
                switch (position) {
                    case 0:
                        Collections.sort(jobs, new Comparator<Job>() {
                            @Override
                            public int compare(Job j1, Job j2) {
                                if (j1.getAdded() > j2.getAdded())
                                    return 1;
                                if (j1.getAdded() < j2.getAdded())
                                    return -1;
                                return 0;
                            }
                        });
                        break;
                    case 1:
                        Collections.sort(jobs, new Comparator<Job>() {
                            @Override
                            public int compare(Job j1, Job j2) {
                                if (j1.getDeadline() > j2.getDeadline())
                                    return 1;
                                if (j1.getDeadline() < j2.getDeadline())
                                    return -1;
                                return 0;
                            }
                        });
                        break;
                    case 2:
                        Collections.sort(jobs, new Comparator<Job>() {
                            @Override
                            public int compare(Job j1, Job j2) {
                                return j1.getName().compareTo(j2.getName());

                            }
                        });
                        break;


                }
                pager.setAdapter(pagerAdapter);
                pager.setCurrentItem(pagerPos);
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.sort_choices));
        lv.setAdapter(adapter);
        alertDialog.show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        int position = tabLayout.getSelectedTabPosition();
        List<Job> fragmentJob = new ArrayList<>();
        JobListFragment jobListFragment;
        switch (position) {
            case 0:

                for(int i = 0 ; i < jobs.size() ; i++) {
                if(jobs.get(i).getStatus() == 1 && jobs.get(i).getName().toLowerCase().contains(newText.toLowerCase())) {
                        fragmentJob.add(jobs.get(i));
                    }
                }
                jobListFragment = (JobListFragment) fragments.get(position);
                jobListFragment.jobs = fragmentJob;
                pager.setAdapter(pagerAdapter);
                pager.setCurrentItem(position);
                break;
            case 1:
                for(int i = 0 ; i < jobs.size() ; i++) {
                    if(jobs.get(i).getStatus() == 2 && jobs.get(i).getName().toLowerCase().contains(newText.toLowerCase())) {
                        fragmentJob.add(jobs.get(i));
                    }
                }
                jobListFragment = (JobListFragment) fragments.get(position);
                jobListFragment.jobs = fragmentJob;
                pager.setAdapter(pagerAdapter);
                pager.setCurrentItem(position);
                break;
            case 2:
                for(int i = 0 ; i < jobs.size() ; i++) {
                    if(jobs.get(i).getStatus() == 3 && jobs.get(i).getName().toLowerCase().contains(newText.toLowerCase())) {
                        fragmentJob.add(jobs.get(i));
                    }
                }
                jobListFragment = (JobListFragment) fragments.get(position);
                jobListFragment.jobs = fragmentJob;
                pager.setAdapter(pagerAdapter);
                pager.setCurrentItem(position);
                break;
        }
        return true;
    }

    public void refreshPager() {
        JobListFragment jobListFragment;
        int pos = pager.getCurrentItem();

        jobListFragment = (JobListFragment) fragments.get(0);
        jobListFragment.jobs = Job.find(Job.class,"status=?",1+"");

        jobListFragment = (JobListFragment) fragments.get(1);
        jobListFragment.jobs = Job.find(Job.class,"status=?",2+"");

        jobListFragment = (JobListFragment) fragments.get(2);
        jobListFragment.jobs = Job.find(Job.class,"status=?",3+"");

        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(pos);
    }


    public void callAPI(int code,String phone) {
        API api = new API(code,MainActivity.this);
        if(phone != null) {
            api.setPhoneNumber(phone);
        }
        progressDialog.show();
        api.callAPI();
    }


    public void apiResult(int code,int result) {
        progressDialog.dismiss();
        switch (code) {
            case 9:if(result == 1) {
                refreshPager();
            }
                else if(result == -1) {
                Toast.makeText(getApplicationContext(),
                        "Refresh Failed",
                        Toast.LENGTH_SHORT).show();
            }
                break;
        }
    }
}
