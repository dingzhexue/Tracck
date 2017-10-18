package flashbox.tracck.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

import flashbox.tracck.R;
import flashbox.tracck.base.TKBaseActivity;
import flashbox.tracck.common.Common;
import flashbox.tracck.common.Constants;
import flashbox.tracck.connect.amazon.ui.TKAmazonLoginActivity;
import flashbox.tracck.home.archive.TKArchiveActivity;
import flashbox.tracck.model.TKPurchase;

/**
 * Created by administrator on 8/3/17.
 */

public class TKHomeActivity extends TKBaseActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private SwipeMenuListView mListView;
    private TKPurchaseListAdapter mListAdapter;
    private ArrayList<TKPurchase> items;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    protected void initUI() {
        super.initUI();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Setting spinner
        Spinner staticSpinner = (Spinner) findViewById(R.id.spinnerview);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.brew_array,
                android.R.layout.simple_spinner_dropdown_item);

        // Specify the layout to use when the list of choices sppears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);

        // Setting List
        mSearchView=(SearchView)findViewById(R.id.search);
        mListView = (SwipeMenuListView) findViewById(R.id.item_list);
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        if (items == null) {
            items = new ArrayList<TKPurchase>();
            mListAdapter = new TKPurchaseListAdapter(this, items);

            // To show all of purcharse type
            insertDummyData();
        }
        mListView.setAdapter(mListAdapter);

        setupSearchView();
//        mListView.setTextFilterEnabled(true);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(R.color.colorBlue);
                // set item width
                openItem.setWidth(dp2px(120));
                // set item title
                openItem.setTitle("ARCHIVE");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
            }
        };

        mListView.setMenuCreator(creator);

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0:
                        TKPurchase temp = items.get(position);

                        items.remove(position);

                        Common.addArchived(temp);

                        Toast.makeText(TKHomeActivity.this,"Archived",Toast.LENGTH_SHORT).show();

                        mListAdapter.notifyDataSetChanged();

                        break;
                }

                return true;

            }

        });

        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {

            @Override

            public void onMenuOpen(int position) {

            }

            @Override

            public void onMenuClose(int position) {

            }

        });

        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override

            public void onSwipeStart(int position) {

            }

            @Override

            public void onSwipeEnd(int position) {

            }

        });
    }

    private void setupSearchView()
    {
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        mListAdapter.getFilter().filter(newText);
//        if (TextUtils.isEmpty(newText)) {
//            mListView.clearTextFilter();
//        } else {
//            mListView.setFilterText(newText);
//        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

    private void insertDummyData() {
        items.add(new TKPurchase(null, null, null, null, null, "Today", Constants.PURCHASE_HEADER));
        items.add(new TKPurchase("Moto G(Phone)","Amazon", "5 mins ago", "En route", null, "Today", Constants.PURCHASE_ITEM));
        items.add(new TKPurchase("Philips Hair Dryer","Amazon", "2 days ago", "Delivered","2", "Today", Constants.PURCHASE_ITEM));
        items.add(new TKPurchase("Samsung Refrigirant","Amazon", "1 years ago", "Service","1", "Today", Constants.PURCHASE_ITEM));
        items.add(new TKPurchase(null, null, null, null, null, "Yesterday", Constants.PURCHASE_HEADER));
        items.add(new TKPurchase("Moto G(Phone)","Amazon", "5 mins ago", "Packing",null, "Yesterday", Constants.PURCHASE_ITEM));
        items.add(new TKPurchase("Moto G(Phone)","Amazon", "2 days ago", "Delivered","2", "Yesterday", Constants.PURCHASE_ITEM));
        items.add(new TKPurchase("Moto G(Phone)","Amazon", "2 days ago", "Service","1", "Yesterday", Constants.PURCHASE_ITEM));

        mListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add) {

            //TODO add item to list from here

//            items.add(new TKPurchase("Moto G(Phone)","Amazon", "5 mins ago", "En route",null, "Today"));

            mListAdapter.notifyDataSetChanged();

        }

        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean supportOffline() {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the home action
        } else if (id == R.id.nav_archive) {
            Intent intent = new Intent(TKHomeActivity.this, TKArchiveActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_signout) {
            Intent intent = new Intent(TKHomeActivity.this, TKAmazonLoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,

                getResources().getDisplayMetrics());
    }
}
