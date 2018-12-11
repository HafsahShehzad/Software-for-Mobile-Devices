package com.example.apple.tabssample;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.apple.tabssample.Fragments.PostTabFragment1;
import com.example.apple.tabssample.Fragments.PostTabFragment2;
import com.example.apple.tabssample.Fragments.PostTabFragment3;


public class PostPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PostPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PostTabFragment1 tab1 = new PostTabFragment1();
                return tab1;
            case 1:
                PostTabFragment2 tab2 = new PostTabFragment2();
                return tab2;
            case 2:
                PostTabFragment3 tab3 = new PostTabFragment3();
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}