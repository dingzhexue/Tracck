package flashbox.tracck.home.archive;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;

import flashbox.tracck.R;
import flashbox.tracck.base.TKBaseActivity;
import flashbox.tracck.connect.amazon.ui.TKAmazonLoginActivity;
import flashbox.tracck.home.TKHomeActivity;
import flashbox.tracck.home.TKPurchaseListAdapter;
import flashbox.tracck.model.TKPurchase;

public class TKArchiveActivity extends TKBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView listView;
    private TKPurchaseListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void initUI() {
        super.initUI();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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
        listView = (ListView) findViewById(R.id.item_list);

        ArrayList items = new ArrayList();

        items.add(new TKPurchase("Moto G(Phone)","Amazon", "2 days ago", "En route",null, "Today"));
        items.add(new TKPurchase("Moto G(Phone)","Amazon", "2 days ago", "Delivered","1", "Today"));
        items.add(new TKPurchase("Moto G(Phone)","Amazon", "2 days ago", "Service","2", "Today"));
        items.add(new TKPurchase("Moto G(Phone)","Amazon", "2 days ago", "Packing",null, "Yesterday"));
        items.add(new TKPurchase("Moto G(Phone)","Amazon", "2 days ago", "Delivered","2", "Yesterday"));
        items.add(new TKPurchase("Moto G(Phone)","Amazon", "2 days ago", "Service","1", "Yesterday"));

        items = sortAndAddSections(items);
        listAdapter = new TKPurchaseListAdapter(this, items);
        listView.setAdapter(listAdapter);
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

        } else if (id == R.id.nav_signout) {
            Intent intent = new Intent(TKArchiveActivity.this, TKAmazonLoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private ArrayList sortAndAddSections(ArrayList itemList) {

        ArrayList tempList = new ArrayList();
        //First we sort the array
        Collections.sort(itemList);

        //Loops thorugh the list and add a section before each sectioncell start
        String header = "";
        for (int i = 0; i < itemList.size(); i++) {
            //If it is the start of a new section we create a new TKPurchase and add it to our array
            if (header != ((TKPurchase) itemList.get(i)).getStrGroup()) {
                TKPurchase sectionCell = new TKPurchase(null, null, null, null, null, ((TKPurchase) itemList.get(i)).getStrGroup());
                sectionCell.setToSectionHeader();
                tempList.add(sectionCell);
                header = ((TKPurchase) itemList.get(i)).getStrGroup();
            }
            tempList.add(itemList.get(i));
        }

        return tempList;
    }
}
