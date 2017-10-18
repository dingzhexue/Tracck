package flashbox.tracck.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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
    protected Toolbar mToolBarA;

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

//            getSupportActionBar().setTitle(R.string.home);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_back);
            mToolBar.setNavigationIcon(R.mipmap.ic_back);
            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            }

//        mToolBarA = (Toolbar) findViewById(R.id.toolbarArchive);
//        if (mToolBarA != null) {
//            setSupportActionBar(mToolBarA);
//
//            getSupportActionBar().setTitle(R.string.archive);
//            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_back);
//            mToolBarA.setNavigationIcon(R.mipmap.ic_back);
//            mToolBarA.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//        }
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
