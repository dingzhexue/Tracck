package flashbox.tracck.model;

public class TKPurchase implements Comparable {

    private String strProductName;
    private String strShopName;
    private String strPeriod;
    private String strStatus;
    private String strWarning;
    private String strGroup;
    private boolean isSectionHeader;

    public TKPurchase(String strProductName, String strShopName, String strPeriod, String strStatus, String strWarning, String strGroup)
    {
        this.strProductName = strProductName;
        this.strShopName = strShopName;
        this.strPeriod = strPeriod;
        this.strStatus = strStatus;
        this.strWarning = strWarning;
        this.strGroup = strGroup;
        this.isSectionHeader = false;
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

    public void setToSectionHeader()
    {
        isSectionHeader = true;
    }

    public String getStrGroup() { return strGroup;}

    public boolean isSectionHeader()
    {
        return isSectionHeader;
    }

    @Override
    public int compareTo(Object another) {
        return this.strGroup.compareTo(((TKPurchase)another).strGroup);
    }
}