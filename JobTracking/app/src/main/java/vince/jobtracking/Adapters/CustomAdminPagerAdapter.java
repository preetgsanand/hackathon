package vince.jobtracking.Adapters;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

import vince.jobtracking.AdminFragments.AdminJobListFragment;
import vince.jobtracking.AdminFragments.UserListFragment;
import vince.jobtracking.Database.Job;
import vince.jobtracking.Database.User;
import vince.jobtracking.MainFragments.JobListFragment;
import vince.jobtracking.Utils.Utils;

/**
 * Created by vince on 3/20/17.
 */
public class CustomAdminPagerAdapter extends FragmentPagerAdapter{

    private List<Fragment> fragments;
    private TabLayout tabLayout;
    private ViewPager pager;
    private Menu menu;
    private String[] titles = {"\u263A Users","\u270E  Jobs","⌛ Pending"};
    public CustomAdminPagerAdapter(FragmentManager fm, ViewPager pager, TabLayout tabLayout,
                                   List<Fragment> fragments) {
        super(fm);
        this.pager = pager;
        this.tabLayout = tabLayout;
        this.fragments = fragments;
        tabLayout.setupWithViewPager(pager);
    }
    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}

