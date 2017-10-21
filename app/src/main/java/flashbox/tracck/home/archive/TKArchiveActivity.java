package flashbox.tracck.home.archive;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;

import flashbox.tracck.R;
import flashbox.tracck.base.TKBaseActivity;
import flashbox.tracck.common.Common;
import flashbox.tracck.connect.amazon.ui.TKAmazonLoginActivity;
import flashbox.tracck.home.TKHomeActivity;
import flashbox.tracck.home.TKPurchaseListAdapter;
import flashbox.tracck.model.TKPurchase;

public class TKArchiveActivity extends TKBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,SearchView.OnQueryTextListener {
    private ListView listView;
    private TKPurchaseListAdapter listAdapter;
    private ArrayList<TKPurchase> items;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.archive);
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
        ((ViewGroup) staticSpinner.getParent()).removeView(staticSpinner);

        // Setting List
        mSearchView=(SearchView)findViewById(R.id.search);
        listView = (ListView) findViewById(R.id.item_list);

        items = Common.getArchived();

//        items = sortAndAddSections(items);
        listAdapter = new TKPurchaseListAdapter(this, items);
        listView.setAdapter(listAdapter);
        setupSearchView();
    }
    private void setupSearchView()
    {
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        listAdapter.getFilter().filter(newText);
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
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the home action
            Intent intent = new Intent(TKArchiveActivity.this, TKHomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_archive) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_settings) {
            Common.showTermsDialog(this);
        } else if (id == R.id.nav_signout) {
            Intent intent = new Intent(TKArchiveActivity.this, TKAmazonLoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
