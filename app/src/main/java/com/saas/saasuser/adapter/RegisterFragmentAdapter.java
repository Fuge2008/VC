package com.saas.saasuser.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.saas.saasuser.fragment.EnFragment;
import com.saas.saasuser.fragment.PeFragment;

/**
 * Created by tyh on 17/9/7.
 */
public class RegisterFragmentAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{"个人注册","企业注册"};

    public RegisterFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new PeFragment();
        } else if (position == 1) {
            return new EnFragment();

        }
        return new EnFragment();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
