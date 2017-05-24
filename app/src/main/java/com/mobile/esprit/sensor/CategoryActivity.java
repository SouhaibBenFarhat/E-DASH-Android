package com.mobile.esprit.sensor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mobile.esprit.sensor.Adapters.AromaAdapter;
import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.Entities.Category;
import com.mobile.esprit.sensor.Entities.Manufacturer;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.URL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

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

    private TabLayout tabLayout;

    public static final String CATEGORY_AROMA = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        this.setTitle("Categories");




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        createTabIcons();


    }

    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.classic, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.mint, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.fruity, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.gourmet, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);

        TextView tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.beverage, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tabFive);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_category, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            final TextView tvCategory = (TextView) rootView.findViewById(R.id.tv_category);
            final TextView tvCategoryDescription = (TextView) rootView.findViewById(R.id.tv_category_description);
            final ImageView ivCategory = (ImageView) rootView.findViewById(R.id.iv_category);

            final ListView lvCategoryAroma = (ListView) rootView.findViewById(R.id.lv_category_aroma);


            final ArrayList<Category> categories = new ArrayList<>();
            final ArrayList<Aroma> aromas = new ArrayList<>();


            //fetching all categories
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.LOAD_ALL_CATEGORIES, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jcategory = response.getJSONObject(i);
                                    Category category = new Category();

                                    category.setIdCategory(jcategory.getInt("id"));
                                    category.setName(jcategory.getString("category"));
                                    category.setDescription(jcategory.getString("description"));
                                    category.setImageName(jcategory.getString("imageName"));
                                    category.setImage(jcategory.getString("imageUrl"));
                                    category.setDate(jcategory.getString("date"));

                                    categories.add(category);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            tvCategory.setText(categories.get((getArguments().getInt(ARG_SECTION_NUMBER)) - 1).getName());
                            tvCategoryDescription.setText(categories.get((getArguments().getInt(ARG_SECTION_NUMBER)) - 1).getDescription());
                            Picasso.with(getActivity()).load(categories.get((getArguments().getInt(ARG_SECTION_NUMBER)) - 1).getImage()).resize(150, 150).into(
                                    ivCategory
                            );


                            //fetching aromas by category
                            JsonArrayRequest reqAroma = new JsonArrayRequest(Request.Method.GET,
                                    URL.LOAD_ALL_AROMAS_BY_CATEGORY + categories.get((getArguments().getInt(ARG_SECTION_NUMBER)) - 1).getIdCategory(),
                                    null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        for (int i = 0; i < response.length(); i++) {
                                            JSONObject jaroma = response.getJSONObject(i);
                                            Aroma aroma = new Aroma();
                                            Manufacturer manufacturer = new Manufacturer();
                                            Category category = new Category();

                                            aroma.setIdAroma(jaroma.getInt("id"));
                                            aroma.setName(jaroma.getString("arome"));
                                            aroma.setImgArome(jaroma.getString("imageUrl"));
                                            aroma.setDescription(jaroma.getString("description"));
                                            aroma.setDate(jaroma.getString("date"));

                                            JSONObject jmanufacturer = jaroma.getJSONObject("manufacture");
                                            manufacturer.setIdManufacturer(jmanufacturer.getInt("id"));
                                            manufacturer.setName(jmanufacturer.getString("name"));
                                            manufacturer.setDescription(jmanufacturer.getString("description"));
                                            manufacturer.setImage(jmanufacturer.getString("imageUrl"));
                                            manufacturer.setAddress(jmanufacturer.getString("address"));
                                            manufacturer.setDate(jmanufacturer.getString("date"));
                                            aroma.setManufacturer(manufacturer);

                                            JSONObject jcategory = jaroma.getJSONObject("category");
                                            category.setIdCategory(jcategory.getInt("id"));
                                            category.setName(jcategory.getString("category"));
                                            category.setImageName(jcategory.getString("imageName"));
                                            category.setDescription(jcategory.getString("description"));
                                            category.setImage(jcategory.getString("imageUrl"));
                                            category.setDate(jcategory.getString("date"));
                                            aroma.setCategory(category);

                                            aromas.add(aroma);
                                        }

                                        AromaAdapter adapter = new AromaAdapter(getActivity(), R.layout.item_aroma, aromas);
                                        lvCategoryAroma.setAdapter(adapter);
                                        lvCategoryAroma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                            }
                                        });
                                        lvCategoryAroma.setOnTouchListener(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                                int action = motionEvent.getAction();
                                                switch (action) {
                                                    case MotionEvent.ACTION_DOWN:
                                                        // Disallow ScrollView to intercept touch events.
                                                        view.getParent().requestDisallowInterceptTouchEvent(true);
                                                        break;

                                                    case MotionEvent.ACTION_UP:
                                                        // Allow ScrollView to intercept touch events.
                                                        view.getParent().requestDisallowInterceptTouchEvent(false);
                                                        break;
                                                }

                                                // Handle ListView touch events.
                                                view.onTouchEvent(motionEvent);
                                                return true;
                                            }
                                        });
                                        lvCategoryAroma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                Intent intent = new Intent(getActivity(), AromaDetailActivity.class);
                                                intent.putExtra(CATEGORY_AROMA, aromas.get(i));
                                                startActivity(intent);
                                            }
                                        });


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });

                            ConnectionSingleton.getInstance(getActivity()).addToRequestque(reqAroma);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Error while parsing...", Toast.LENGTH_SHORT).show();

                }
            });


            ConnectionSingleton.getInstance(getActivity()).addToRequestque(req);


            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Classic";
                case 1:
                    return "Mint";
                case 2:
                    return "Fruity";
                case 3:
                    return "Gourmet";
                case 4:
                    return "Beverage";
            }
            return null;
        }
    }
}
