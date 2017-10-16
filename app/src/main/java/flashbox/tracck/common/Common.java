package flashbox.tracck.common;

public class Common {
    private static String strSelectedTimePeriod;

    public static String getSelectedTimePeriod() {
        return strSelectedTimePeriod;
    }

    public static void setSelectedTimePeriod(String strSelectedTimePeriod) {
        Common.strSelectedTimePeriod = strSelectedTimePeriod;
    }
}
