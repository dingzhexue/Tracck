package flashbox.tracck.common.api;

import android.content.Context;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import flashbox.tracck.api.TKBaseAPI;

/**
 * Created by android on 12/11/2017.
 */

public class TKPurchaseItemMoreInfoAPI extends TKBaseAPI<TKPurchaseItemMoreInfoAPI, TKPurchaseItemMoreInfoResponse> {
    private String mName;
    private String mPassword;
    private String mInvoiceNumber;
    private String mPrice;
    private String mCardInfo;
    private String mPurchasedDate;
    private String mWarrantyInfo;
    private String mProductDetails;

    public TKPurchaseItemMoreInfoAPI(Context context, String invoiceNumber, String price, String cardInfo, String purchaseDate, String warrantyInfo, String productDetails) {
        super(context, "/auth/moreInfo_by_post");

        mInvoiceNumber = invoiceNumber;
        mPrice = price;
        mCardInfo = cardInfo;
        mPurchasedDate = purchaseDate;
        mWarrantyInfo = warrantyInfo;
        mProductDetails = productDetails;
    }

    @Override
    protected RequestParams createReqParams(RequestParams params) {
        super.createReqParams(params);

        params.put("login", mName);
        params.put("password", mPassword);

        return params;
    }

    @Override
    protected boolean handleResponse(JSONObject response) throws JSONException {
        return super.handleResponse(response);
    }
}
