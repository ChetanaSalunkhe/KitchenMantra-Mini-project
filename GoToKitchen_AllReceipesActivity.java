package com.example.chetana.kitchenmantra;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.chetana.kitchenmantra.Fragments.AllReceipesFragment;
import com.example.chetana.kitchenmantra.Fragments.NonVegFragment;
import com.example.chetana.kitchenmantra.Fragments.VegFragment;

import java.util.ArrayList;
import java.util.List;

public class GoToKitchen_AllReceipesActivity extends AppCompatActivity {
    private Context parent;
    TabLayout tabLayout;
    ViewPager viewpager;
    String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_kitchen__all_receipes);

        init();

        setListener();
    }

    public void init(){
        parent = GoToKitchen_AllReceipesActivity.this;

        viewpager = (ViewPager)findViewById(R.id.viewpager);
        setupViewPager(viewpager);

        if(position!=null){
            viewpager.setCurrentItem(Integer.parseInt(position));
        }

        tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewpager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllReceipesFragment(),"All receipes");
        adapter.addFragment(new VegFragment(),"Veg");
        adapter.addFragment(new NonVegFragment(),"Non-veg");
       // adapter.addFragment(new AllReceipesFragment(),"Favourites");
        viewPager.setAdapter(adapter);
    }

    public void setListener(){

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
