package com.mobile.esprit.sensor.user_profile_activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.home_activity.fragments.RecipesFragment;
import com.mobile.esprit.sensor.user_profile_activity.fragments.UserDeviceConfigFragment;
import com.mobile.esprit.sensor.user_profile_activity.fragments.UserRecipesFragment;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {


    Toolbar toolbar;
    CircleImageView userProfileImage;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;


    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initCollapsingToolbar();


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mTabLayout.setupWithViewPager(mViewPager);
//        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_profile_recipe);
//        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_profile_device_config);
//        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_custom_recipe);


        userProfileImage = (CircleImageView) findViewById(R.id.user_profile_image);
        try{
            Picasso.with(this).load(Uri.parse(User.getInstance().getProfilePicture())).into(userProfileImage);
        }catch (NullPointerException e){


        }

    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        collapsingToolbar.setBackgroundColor(ContextCompat.getColor(UserProfileActivity.this, R.color.colorPrimary));
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                    collapsingToolbar.setBackgroundColor(ContextCompat.getColor(UserProfileActivity.this, R.color.colorPrimary));
                    System.out.println("1");
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("");
                    toolbar.setBackgroundColor(ContextCompat.getColor(UserProfileActivity.this, R.color.colorPrimary));
                    isShow = true;
                    System.out.println("2");
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                    System.out.println("3");
                    toolbar.setBackgroundColor(ContextCompat.getColor(UserProfileActivity.this, R.color.transparent));
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new UserRecipesFragment();
                case 1:
                    return new UserDeviceConfigFragment();
                case 2:
                    return new RecipesFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "My Recipes";
                case 1:
                    return "Configuration";
                case 2:
                    return "History";
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }
}
