package flashbox.tracck.model;

import android.text.Spanned;

public class TKChat {
    private int intMsgType;
    private Spanned spanMsg;
    private String strDate;
    private String strTime;

    public int getIntMsgType() {
        return intMsgType;
    }

    public Spanned getSpanMsg() {
        return spanMsg;
    }

    public String getStrDate() {
        return strDate;
    }

    public String getStrTime() {
        return strTime;
    }

    public void setIntMsgType(int intMsgType) {
        this.intMsgType = intMsgType;
    }

    public void setSpanMsg(Spanned spanMsg) {
        this.spanMsg = spanMsg;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }
}
