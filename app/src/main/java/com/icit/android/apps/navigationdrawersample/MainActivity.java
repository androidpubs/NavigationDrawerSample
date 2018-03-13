package com.icit.android.apps.navigationdrawersample;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.icit.android.apps.navigationdrawersample.base.BaseActivity;
import com.icit.android.apps.navigationdrawersample.base.FlexibleSpaceWithImageBaseFragment;
import com.icit.android.apps.navigationdrawersample.dialog.ColorPickerDialog;
import com.icit.android.apps.navigationdrawersample.fragment.FlexibleSpaceWithImageGridViewFragment;
import com.icit.android.apps.navigationdrawersample.fragment.FlexibleSpaceWithImageListViewFragment;
import com.icit.android.apps.navigationdrawersample.fragment.FlexibleSpaceWithImageRecyclerViewFragment;
import com.icit.android.apps.navigationdrawersample.fragment.FlexibleSpaceWithImageScrollViewFragment;
import com.icit.android.apps.navigationdrawersample.fragment.QuickContactFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener, ColorPickerDialog.OnColorChangedListener {

    // DrawerLayout
    private DrawerLayout drawer;
    // FlexibleSpace or Normal
    private boolean isFlexibleSpace = true;
    private boolean isLibTabStrip = true;
    // BottomNavigationView
    BottomNavigationView bottomNavigationView;
    private MenuItem prevBottomNavigation;
    // TabLayout
    private View mSlidingTabLayout;
    private Drawable oldBackground = null;
    private int currentColor = 0xFF666666;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(isFlexibleSpace ? R.layout.activity_flexible_space : R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Drawer Layout
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation View
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(isFlexibleSpace) {
            // Pager Adapter
            mPagerAdapter = new NestedScrollingPagerAdapter(getSupportFragmentManager());
            mViewPager = (ViewPager) findViewById(R.id.view_pager);
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.addOnPageChangeListener(this);

            // Sliding Tab Layout
            if(isLibTabStrip) {
                mSlidingTabLayout = (PagerSlidingTabStrip) findViewById(R.id.sliding_tabs);
                ((PagerSlidingTabStrip)mSlidingTabLayout).setViewPager(mViewPager);
                currentColor = changeColor(currentColor);
            }
            else {
                mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
                ((SlidingTabLayout)mSlidingTabLayout).setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
                ((SlidingTabLayout)mSlidingTabLayout).setSelectedIndicatorColors(getResources().getColor(R.color.accent));
                ((SlidingTabLayout)mSlidingTabLayout).setDistributeEvenly(true);
                ((SlidingTabLayout)mSlidingTabLayout).setViewPager(mViewPager);
            }
        }

        // BottomNavigationView
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        prevBottomNavigation = bottomNavigationView.getMenu().getItem(0);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_one:
                        mViewPager.setCurrentItem(0);
                        return true;
                    case R.id.action_two:
                        mViewPager.setCurrentItem(1);
                        return true;
                    case R.id.action_three:
                        mViewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });
//        // App Bar
//        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
//        appBarLayout.setMinimumHeight(mSlidingTabLayout.getHeight() << 1);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentColor", currentColor);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentColor = savedInstanceState.getInt("currentColor");
        currentColor = changeColor(currentColor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_contact:
                QuickContactFragment dialog = new QuickContactFragment();
                dialog.show(getSupportFragmentManager(), "QuickContactFragment");
                return true;
            case  R.id.action_settings:
                new ColorPickerDialog(this, this, currentColor).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void colorChanged(int newColor) {
        currentColor = changeColor(newColor);
    }
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (toolbar != null) {
//            int size = UIUtils.calculateActionBarSize(this);
//            toolbar.setTitleMarginBottom(mSlidingTabLayout.getHeight() << 1);
//            toolbar.setMinimumHeight(mSlidingTabLayout.getHeight());
//            ViewGroup.LayoutParams lp = toolbar.getLayoutParams();
//            lp.height = size;
//            toolbar.setLayoutParams(lp);
//        }
//    }

    /**
     * NavigationView.OnNavigationItemSelectedListener
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * ViewPager.OnPageChangeListener
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int bottomNavigationViewSize = bottomNavigationView.getMenu().size();
        if(position >= bottomNavigationViewSize) {
            bottomNavigationView.setVisibility(View.GONE);
        }
        else {
            bottomNavigationView.setVisibility(View.VISIBLE);
            if (prevBottomNavigation != null) {
                prevBottomNavigation.setChecked(false);
            }
            prevBottomNavigation = bottomNavigationView.getMenu().getItem(position % bottomNavigationViewSize);
            prevBottomNavigation.setChecked(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * Apply flexible space
     */
//    protected static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private ViewPager mViewPager;
    private NestedScrollingPagerAdapter mPagerAdapter;

    /**
     * This adapter provides three types of fragments as an example.
     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
     */
    private static class NestedScrollingPagerAdapter extends CacheFragmentStatePagerAdapter {

        private static final String[] TITLES = new String[]{"Applepie", "Butter Cookie", "Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb", "Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop"};

        public NestedScrollingPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected Fragment createItem(int position) {
            FlexibleSpaceWithImageBaseFragment f;
            final int pattern = position % 4;
            switch (pattern) {
                case 0: {
                    f = new FlexibleSpaceWithImageScrollViewFragment();
                    break;
                }
                case 1: {
                    f = new FlexibleSpaceWithImageListViewFragment();
                    break;
                }
                case 2: {
                    f = new FlexibleSpaceWithImageRecyclerViewFragment();
                    break;
                }
                case 3:
                default: {
                    f = new FlexibleSpaceWithImageGridViewFragment();
                    break;
                }
            }

            return f;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }

    /**
     * TabLayout
     */


    private int changeColor(int newColor) {

        ((PagerSlidingTabStrip)mSlidingTabLayout).setIndicatorColor(newColor);

        // change ActionBar color just if an ActionBar is available
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//
//            Drawable colorDrawable = new ColorDrawable(newColor);
//            Drawable bottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
//            LayerDrawable ld = new LayerDrawable(new Drawable[] { colorDrawable, bottomDrawable });
//
//            if (oldBackground == null) {
//
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                    ld.setCallback(drawableCallback);
//                } else {
//                    getSupportActionBar().setBackgroundDrawable(ld);
//                }
//            } else {
//                TransitionDrawable td = new TransitionDrawable(new Drawable[] { oldBackground, ld });
//
//                // workaround for broken ActionBarContainer drawable handling on
//                // pre-API 17 builds
//                // https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                    td.setCallback(drawableCallback);
//                } else {
//                    getActionBar().setBackgroundDrawable(td);
//                }
//
//                td.startTransition(200);
//            }
//
//            oldBackground = ld;
//
//            // http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//            getSupportActionBar().setDisplayShowTitleEnabled(true);
//        }

        //currentColor = newColor;
        return newColor;
    }

    private Drawable.Callback drawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                who.setCallback(drawableCallback);
            } else {
                getActionBar().setBackgroundDrawable(who);
            }
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            handler.postAtTime(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
            handler.removeCallbacks(what);
        }
    };
}
