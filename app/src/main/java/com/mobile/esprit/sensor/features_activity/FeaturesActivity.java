package com.mobile.esprit.sensor.features_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobile.esprit.sensor.LoginActivity;
import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.features_activity.fragments.IntroOneFragment;
import com.mobile.esprit.sensor.features_activity.fragments.IntroThreeFragment;
import com.mobile.esprit.sensor.features_activity.fragments.IntroTwoFragment;

import me.relex.circleindicator.CircleIndicator;

public class FeaturesActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    static TextView textView;
    Button btn_next;
    Button btn_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        indicator.setViewPager(mViewPager);


        //
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_skip = (Button) findViewById(R.id.btn_skip);


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = mViewPager.getCurrentItem();

                if (currentItem == 2) {
                    startActivity(new Intent(FeaturesActivity.this, LoginActivity.class));

                } else {
                    mViewPager.setCurrentItem(currentItem + 1, true);

                }


            }
        });

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplication(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {

            final int pos = position;
            switch (position) {
                case 0:
                    return IntroOneFragment.newInstance(null, null);
                case 1:
                    return IntroTwoFragment.newInstance(null, null);

                case 2:
                    return IntroThreeFragment.newInstance(null, null);

                default:
                    return IntroOneFragment.newInstance(null, null);
            }


        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:

                    textView.setText("test 1");
                    System.out.println("test1");

                    return "SECTION 1";
                case 1:

                    textView.setText("test 2");
                    System.out.println("test2");

                    return "SECTION 2";
                case 2:
                    textView.setText("test 3");
                    System.out.println("test3");

                    return "SECTION 3";
            }
            return null;
        }
    }
}
