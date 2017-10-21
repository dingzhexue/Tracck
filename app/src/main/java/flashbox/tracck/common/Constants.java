package flashbox.tracck.common;

public class Constants {
    /**
     * TKChat Message Type
     */
    public static final int MSG_RECEIVED = 0;
    public static final int MSG_SENT = 1;
    public static final int MSG_NOTIFY = 2;
    public static final int MSG_FEEDBACK = 3;
    public static final int MSG_SCHEDULED_CALL = 4;
    public static final int MSG_VIEW_RECOMMEND = 5;

    /**
     * TKPurchase Type
     */
    public static final int PURCHASE_HEADER = 0;
    public static final int PURCHASE_ITEM = 1;

    /**
     * Time Period Values
     */
    public static final String[] arrTimePeriods = new String[]{
            "0 AM-2 AM", "2 AM-4 AM", "4 AM-6 AM", "6 AM-8 AM", "8 AM-10 AM", "10 AM-12 PM", "12 PM-14 PM",
            "14 PM-16 PM", "16 PM-18 PM", "18 PM-20 PM", "20 PM-22 PM", "22 PM-0 AM"
    };
}
