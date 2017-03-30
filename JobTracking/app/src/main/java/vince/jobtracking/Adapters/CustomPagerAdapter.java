package vince.jobtracking.Adapters;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;

import vince.jobtracking.Database.Job;
import vince.jobtracking.MainFragments.JobListFragment;

/**
 * Created by vince on 3/19/17.
 */
public class CustomPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private TabLayout tabLayout;
    private ViewPager pager;
    private String[] titles = {"\uD83D\uDCA4 Abandoned","✓ Completed","⌛ Ongoing"};
    public CustomPagerAdapter(FragmentManager fm, ViewPager pager, TabLayout tabLayout,
                              List<Fragment> fragments) {
        super(fm);
        this.pager = pager;
        this.tabLayout = tabLayout;
        this.fragments = fragments;
        tabLayout.setupWithViewPager(pager);
    }
    @Override
    public Fragment getItem(int position) {
        JobListFragment jobListFragment = (JobListFragment)fragments.get(position);
        switch (position) {
            case 0:jobListFragment.jobs = Job.find(Job.class,"status=?",1+"");
                break;
            case 1:jobListFragment.jobs = Job.find(Job.class,"status=?",2+"");
                break;
            case 2:jobListFragment.jobs = Job.find(Job.class,"status=?",3+"");
                break;
        }
        return jobListFragment;
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
