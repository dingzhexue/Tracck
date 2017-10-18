package flashbox.tracck.common;

import java.util.ArrayList;
import java.util.List;

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
}
