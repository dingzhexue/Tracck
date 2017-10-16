package flashbox.tracck.home.details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import flashbox.tracck.R;
import flashbox.tracck.base.TKBaseActivity;
import flashbox.tracck.home.TKPreviewActivity;
import flashbox.tracck.home.chat.TKPurchaseChatFragment;

public class TKPurchaseDetailActivity extends TKBaseActivity {
    private boolean isLessInfo;
    private TKPurchaseChatFragment fragmentPurchaseChat;
    private TKMoreInfoFragment fragmentMoreInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_detail);
    }

    @Override
    protected void initUI() {
        super.initUI();

        // getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
        // getSupportActionBar().hide();

        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.llDetail);

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragmentPurchaseChat = new TKPurchaseChatFragment();
        fragmentPurchaseChat.setParentLayout(TKPurchaseDetailActivity.this, parentLayout);
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment, fragmentPurchaseChat);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();

        fragmentMoreInfo = new TKMoreInfoFragment();

        ImageView imgProduct = (ImageView) findViewById(R.id.imgProduct);
        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductPreview(TKPurchaseDetailActivity.this);
            }
        });

        final TextView txtMoreInfo = (TextView) findViewById(R.id.txtMoreInfo);
        txtMoreInfo.setPaintFlags(txtMoreInfo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLessInfo == false) {
                    txtMoreInfo.setText("Less Info");

                    // Create the transaction
                    FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
                    // Replace the content of the container
                    fts.replace(R.id.fragment, fragmentMoreInfo);
                    // Append this transaction to the backstack
//                    fts.addToBackStack(null);
                    // Commit the changes
                    fts.commit();

                    isLessInfo = true;
                } else {
                    txtMoreInfo.setText("More Info");

//                    FragmentManager fragmentManager = getSupportFragmentManager();
//                    if (fragmentManager.getBackStackEntryCount() > 0) {
//                        fragmentManager.popBackStack();
//                    }
                    // Create the transaction
                    FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
                    // Replace the content of the container
                    fts.replace(R.id.fragment, fragmentPurchaseChat);
                    // Append this transaction to the backstack
//                    fts.addToBackStack(null);
                    // Commit the changes
                    fts.commit();

                    isLessInfo = false;
                }
            }
        });

        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean supportOffline() {
        return true;
    }

    private void showProductPreview(Context context) {
        Intent mainIntent = new Intent(TKPurchaseDetailActivity.this,
                TKPreviewActivity.class);
        startActivity(mainIntent);
    }

    /**
     * Overriding onKeyDown for dismissing keyboard on key down
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return fragmentPurchaseChat.onKeyDown(keyCode, event);
    }
}
