package flashbox.tracck.model;

public class TKPurchase {
    private String strProductName;
    private String strShopName;
    private String strPeriod;
    private String strStatus;
    private String strWarning;
    private String strGroup;
    private int intItemType;

    public TKPurchase(String strProductName, String strShopName, String strPeriod, String strStatus, String strWarning, String strGroup, int intItemType)
    {
        this.strProductName = strProductName;
        this.strShopName = strShopName;
        this.strPeriod = strPeriod;
        this.strStatus = strStatus;
        this.strWarning = strWarning;
        this.strGroup = strGroup;
        this.intItemType = intItemType;
    }

    public String getStrProductName()
    {
        return strProductName;
    }

    public String getStrShopName()
    {
        return strShopName;
    }

    public String getStrPeriod()
    {
        return strPeriod;
    }

    public String getStrStatus()
    {
        return strStatus;
    }

    public String getStrWarning()
    {
        return strWarning;
    }

    public int getItemType() { return intItemType; }

    public String getStrGroup() { return strGroup;}

    public void setItemType(int intItemType)
    {
        this.intItemType = intItemType;
    }
}