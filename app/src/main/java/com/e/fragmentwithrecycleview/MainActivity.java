 package com.e.fragmentwithrecycleview;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.e.fragmentwithrecycleview.Adapter.ViewPagerAdapter;
import com.e.fragmentwithrecycleview.Fragment.CallFragment;
import com.e.fragmentwithrecycleview.Fragment.ContectFragment;
import com.e.fragmentwithrecycleview.Fragment.FavFragment;
import com.google.android.material.tabs.TabLayout;

 public class MainActivity extends AppCompatActivity {
TabLayout tabLayout;
ViewPager viewPager;
ViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new CallFragment(),"");
        adapter.AddFragment(new ContectFragment(),"");
        adapter.AddFragment(new FavFragment(),"");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_call);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_people);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_favorite);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

    }
}
