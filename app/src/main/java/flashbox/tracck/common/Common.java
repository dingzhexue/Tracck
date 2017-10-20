package flashbox.tracck.common;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import flashbox.tracck.R;
import flashbox.tracck.model.TKPurchase;

public class Common {
    private static String strSelectedTimePeriod;

    private static ArrayList<TKPurchase> archived;

    private static TKPurchase purchase;

    public static String getSelectedTimePeriod() {
        return strSelectedTimePeriod;
    }

    public static void setSelectedTimePeriod(String strSelectedTimePeriod) {
        Common.strSelectedTimePeriod = strSelectedTimePeriod;
    }

    public static void setPurchase(TKPurchase purchase) {
        Common.purchase = purchase;
    }

    public static TKPurchase getPurchase() {
        return Common.purchase;
    }

    public static ArrayList<TKPurchase> getArchived(){
        if(Common.archived == null)
        {
            Common.archived = new ArrayList<TKPurchase>();
        }
        return archived;
    }

    public static void addArchived(TKPurchase item){
        if(Common.archived == null)
        {
            Common.archived = new ArrayList<TKPurchase>();
        }
            Common.archived.add(item);
    }

    public static void showTermsDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_terms_and_conditions);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        ImageView imgClose = (ImageView) dialog.findViewById(R.id.imgClose);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        /**
         * if you want the dialog to be specific size, do the following
         * this will cover 85% of the screen (85% width and 85% height)
         */
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dialogWidth = (int)(displayMetrics.widthPixels * 1);
        int dialogHeight = (int)(displayMetrics.heightPixels * 0.95);
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);

        dialog.show();
    }
}
