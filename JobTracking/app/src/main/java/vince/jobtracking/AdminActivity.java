package vince.jobtracking;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vince.jobtracking.Adapters.CustomAdminPagerAdapter;
import vince.jobtracking.AdminFragments.AdminJobListFragment;
import vince.jobtracking.AdminFragments.UserListFragment;
import vince.jobtracking.Database.Job;
import vince.jobtracking.Database.User;
import vince.jobtracking.Utils.API;
import vince.jobtracking.Utils.Utils;

public class AdminActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ActionBar mActionBar;
    private DrawerLayout drawerLayout;
    private ViewPager pager;
    private TabLayout tabLayout;
    private List<Fragment> fragments;
    private CustomAdminPagerAdapter pagerAdapter;
    private ProgressDialog progressDialog;
    private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Utils.activity = 1;
        Utils.MainAdminActivity = 1;
        setContentView(R.layout.activity_admin);
        setUI();
    }

    private void setUI() {
        mActionBar = getSupportActionBar();

        mActionBar.setHomeAsUpIndicator(R.drawable.navigation);

        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //-----------Pager Setting------------------
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        fragments = new ArrayList<>();

        UserListFragment userListFragment = new UserListFragment();
        String phone = User.findById(User.class,1).getPhone();
        List<User> users = User.find(User.class,"phone!=?",phone);
        userListFragment.users = users;
        AdminJobListFragment adminJobListFragment = new AdminJobListFragment();
        adminJobListFragment.jobs = Job.listAll(Job.class);

        fragments.add(0,userListFragment);
        fragments.add(1,adminJobListFragment);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.viewpager);

        pagerAdapter = new CustomAdminPagerAdapter(getSupportFragmentManager(),
                pager,tabLayout,fragments);
        pager.setAdapter(pagerAdapter);

        //-------------Progress Dialog-----------------
        progressDialog = new ProgressDialog(AdminActivity.this);
        progressDialog.setMessage("Syncing");

        //---------Floating Button----------------------

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setJobUserAddDialog();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        this.menu = menu;
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh:
                callAPI(13);
                return true;
            case android.R.id.home:
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.display:setDisplayOptions();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        Utils.activity = 1;
        Utils.MainAdminActivity = 1;
        refreshPager();

    }
    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        int position = tabLayout.getSelectedTabPosition();
        switch (position) {
            case 0:
                List<User> users = new ArrayList<>();
                String name = User.findById(User.class,1).getName();
                List<User> allUsers = User.find(User.class,"name!=?",name);
                UserListFragment userListFragment;
                for(int i = 0 ; i < allUsers.size() ; i++) {
                    if(allUsers.get(i).getName().toLowerCase().contains(newText.toLowerCase())) {
                        users.add(allUsers.get(i));
                    }
                }
                userListFragment = (UserListFragment) fragments.get(position);
                userListFragment.users = users;
                pager.setAdapter(pagerAdapter);
                pager.setCurrentItem(position);
                break;
            case 1:
                List<Job> jobs = new ArrayList<>();
                List<Job> allJobs = Job.listAll(Job.class);
                AdminJobListFragment adminJobListFragment;
                for(int i = 0 ; i < allJobs.size() ; i++) {
                    if(allJobs.get(i).getName().toLowerCase().contains(newText.toLowerCase()) ||
                            getResources().getStringArray(R.array.job_status)[allJobs.get(i).getStatus()-1].toLowerCase().
                                    contains(newText.toLowerCase())) {
                        jobs.add(allJobs.get(i));
                    }
                }
                adminJobListFragment = (AdminJobListFragment) fragments.get(position);
                adminJobListFragment.jobs = jobs;
                pager.setAdapter(pagerAdapter);
                pager.setCurrentItem(position);
                break;
        }
        return true;
    }



    public void setDetailActivity(long id) {
        Intent intent = new Intent(getApplicationContext(),JobDetailActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);

    }

    public void setUserDetailActivity(long id) {
        Intent intent = new Intent(getApplicationContext(),UserDetailActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);

    }

    public void refreshPager() {
        AdminJobListFragment adminJobListFragment;
        UserListFragment userListFragment;
        int pos = pager.getCurrentItem();

        adminJobListFragment = (AdminJobListFragment) fragments.get(1);
        adminJobListFragment.jobs = Job.listAll(Job.class);

        userListFragment = (UserListFragment) fragments.get(0);
        String phone = User.findById(User.class,1).getPhone();
        userListFragment.users = User.find(User.class,"phone!=?",phone);


        pager.setAdapter(pagerAdapter);
        pager.destroyDrawingCache();
        pager.setCurrentItem(pos);
    }


    public void callAPI(int code) {
        API api = new API(code,AdminActivity.this);
        progressDialog.show();
        api.callAPI();
    }


    public void apiResult(int code,int result) {
        progressDialog.dismiss();
        switch (code) {
            case 13:if(result == -1) {
                Toast.makeText(getApplicationContext(),
                        "Refresh Failed",
                        Toast.LENGTH_SHORT).show();
            }
            else if(result == 1){
                Toast.makeText(getApplicationContext(),
                        "Refresh Successful",
                        Toast.LENGTH_SHORT).show();
                refreshPager();
            }
                break;
        }
    }

    private void setDisplayOptions() {
        int pagerPos = pager.getCurrentItem();
        final Dialog alertDialog = new Dialog(AdminActivity.this);
        final LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.display_options_listview, null);
        alertDialog.setContentView(convertView);
        ListView lv = (ListView) convertView.findViewById(R.id.displayOptionsList);
        switch (pagerPos) {
            case 0:
                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        getResources().getStringArray(R.array.departments));

                lv.setAdapter(adapter);
                break;
            case 1:
                ArrayAdapter adapter1 = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        getResources().getStringArray(R.array.departments));

                lv.setAdapter(adapter1);
                break;
        }

    }


    private void setJobUserAddDialog() {
        final Dialog alertDialog = new Dialog(AdminActivity.this);
        final LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.display_options_listview, null);
        alertDialog.setContentView(convertView);
        ListView lv = (ListView) convertView.findViewById(R.id.displayOptionsList);

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.fab_add_options));


        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(),AddActivity.class);
                switch (position) {
                    case 0:intent.putExtra("id",1);
                        break;
                    case 1:intent.putExtra("id",2);
                        break;

                }
                startActivity(intent);
            }
        });

        alertDialog.show();
    }
}
