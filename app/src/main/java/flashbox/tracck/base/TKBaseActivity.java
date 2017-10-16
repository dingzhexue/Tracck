package flashbox.tracck.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.gpit.android.logger.RemoteLogger;
import com.gpit.android.ui.common.BaseActivity;

import butterknife.ButterKnife;
import flashbox.tracck.R;

/**
 * Created by administrator on 8/3/17.
 */

public abstract class TKBaseActivity extends BaseActivity {
    protected Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                // NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    @CallSuper
    protected void initUI() {
        ButterKnife.bind(this);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);

            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer != null) {
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.setDrawerListener(toggle);
                toggle.syncState();
            }
        }
    }

    @CallSuper
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void toast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();

        RemoteLogger.i(TAG, getString(R.string.invalid_login_info));
    }
}
