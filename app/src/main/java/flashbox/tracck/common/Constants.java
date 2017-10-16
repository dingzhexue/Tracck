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
     * Time Period Values
     */
    public static final String[] arrTimePeriods = new String[]{
            "0-2 AM", "2-4 AM", "4-6 AM", "6-8 AM", "8-10 AM", "10-12 PM", "12-14 PM",
            "14-16 PM", "16-18 PM", "18-20 PM", "20-22 PM", "22-0 AM"
    };
}
