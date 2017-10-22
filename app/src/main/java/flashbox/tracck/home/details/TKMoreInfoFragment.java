package flashbox.tracck.home.details;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import flashbox.tracck.R;
import flashbox.tracck.base.TKBaseFragment;

public class TKMoreInfoFragment extends TKBaseFragment {
    private TKMoreInfoViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TKPurchaseDetailsFragment fragmentPurchaseDetails;
    private TKProductDetailsFragment fragmentProductDetails;
    private FragmentActivity listener;

    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (FragmentActivity) context;
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_details;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        // Required empty public constructor
        fragmentPurchaseDetails = new TKPurchaseDetailsFragment();
        fragmentProductDetails = new TKProductDetailsFragment();

        adapter = new TKMoreInfoViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(fragmentPurchaseDetails, "Purchase Details");
        adapter.addFragment(fragmentProductDetails, "Product Details");
    }

    @Override
    protected void initUI() {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        viewPager = (ViewPager) mContentView.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) mContentView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void reload(Bundle bundle) {

    }

    // This method is called when the fragment is no longer connected to the Activity
    // Any references saved in onAttach should be nulled out here to prevent memory leaks.
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;

        viewPager = null;
        tabLayout = null;
    }

    // This method is called after the parent Activity's onCreate() method has completed.
    // Accessing the view hierarchy of the parent activity must be done in the onActivityCreated.
    // At this point, it is safe to search for activity View objects by their ID, for example.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
