package flashbox.tracck.home;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import flashbox.tracck.R;
import flashbox.tracck.base.TKBaseActivity;

public class TKPreviewActivity extends TKBaseActivity {

    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    TKSliderPagerAdapter sliderPagerAdapter;
    ArrayList<Integer> slider_image_list;
    private TextView[] dots;
    int page_position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_fragment);
    }

    @Override
    protected void initUI() {
        super.initUI();

        //        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().hide();

//        setFinishOnTouchOutside(false);

        init();

        addBottomDots(0);

        ImageView imgClose = (ImageView) findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Handler handler = new Handler();

        final Runnable update =new Runnable() {
            @Override
            public void run() {
                if (page_position==slider_image_list.size()){
                    page_position=0;
                }else{
                    page_position=page_position+1;
                }
                vp_slider.setCurrentItem(page_position,true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 50, 5000);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int dialogWidth = displayMetrics.widthPixels * 1;
        int dialogHeight = (int)(displayMetrics.heightPixels * 0.85);
        getWindow().setLayout(dialogWidth, dialogHeight);
    }

    @Override
    public boolean supportOffline() {
        return true;
    }

    private void init() {
        vp_slider=(ViewPager)findViewById(R.id.vp_slider);
        vp_slider.setAdapter(sliderPagerAdapter);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);

        slider_image_list = new ArrayList<>();

        //Add few items to slider_image_list ,this should contain url of images which should be displayed in slider
        // here i am adding few sample image links, you can add your own

        slider_image_list.add(R.mipmap.ic_purchaseimg);
        slider_image_list.add(R.mipmap.ic_purchaseimgone);
        slider_image_list.add(R.mipmap.ic_purchaseimgtwo);
        sliderPagerAdapter = new TKSliderPagerAdapter(TKPreviewActivity.this, slider_image_list);
        vp_slider.setAdapter(sliderPagerAdapter);
        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#f2f4f4"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#29b2ef"));
    }
}
