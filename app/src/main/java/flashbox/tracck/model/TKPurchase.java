package flashbox.tracck.model;

import java.util.Date;

public class TKPurchase {
    private String strProductName;
    private String strShopName;
    private String strPeriod;
    private String strStatus;
    private String strWarning;
    private String strDate;
    private int intItemType;

    public TKPurchase(String strProductName, String strShopName, String strPeriod, String strStatus, String strWarning, String strDate, int intItemType)
    {
        this.strProductName = strProductName;
        this.strShopName = strShopName;
        this.strPeriod = strPeriod;
        this.strStatus = strStatus;
        this.strWarning = strWarning;
        this.strDate = strDate;
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

    public String getStrDate() { return strDate;}

    public void setItemType(int intItemType)
    {
        this.intItemType = intItemType;
    }
}