package com.apcpdcl.departmentapp.adapters;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.apcpdcl.departmentapp.fragments.LmDcSlabFragment;
import com.apcpdcl.departmentapp.models.LmDcListModel;
import com.apcpdcl.departmentapp.utils.Constants;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    private int tabCount;
    private ArrayList<LmDcListModel> lmDcListModels;
    private ArrayList<LmDcListModel> lmDcListModelNS;
    private String from;

    //Constructor to the class
    public ViewPagerAdapter(FragmentManager fm, int tabCount, ArrayList<LmDcListModel> lmDcListModels, ArrayList<LmDcListModel> lmDcListModelsNS, String from) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
        this.from = from;
        this.lmDcListModels = lmDcListModels;
        this.lmDcListModelNS = lmDcListModelsNS;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new LmDcSlabFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(LmDcListModel.class.getSimpleName(), lmDcListModels);
                bundle.putString(Constants.FROM, from);
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                fragment = new LmDcSlabFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constants.FROM, from);
                bundle1.putSerializable(LmDcListModel.class.getSimpleName(), lmDcListModelNS);
                fragment.setArguments(bundle1);
                return fragment;
        }

        return null;
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Slab";
            case 1:
                return "Non Slab";
        }
        return null;
    }
}