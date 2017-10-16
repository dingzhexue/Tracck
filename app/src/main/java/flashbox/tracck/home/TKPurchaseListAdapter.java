package flashbox.tracck.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import flashbox.tracck.R;
import flashbox.tracck.home.details.TKPurchaseDetailActivity;
import flashbox.tracck.model.TKPurchase;

/**
 * Created by Security on 10/8/2017.
 */
public class TKPurchaseListAdapter extends ArrayAdapter {
    LayoutInflater inflater;
    Context context;

    public TKPurchaseListAdapter(Context context, ArrayList items) {
        super(context, 0, items);

        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TKPurchase cell = (TKPurchase) getItem(position);

        //If the cell is a section header we inflate the header layout
        if(cell.isSectionHeader())
        {
            v = inflater.inflate(R.layout.list_header_item, null);

            v.setClickable(false);

            TextView header = v.findViewById(R.id.section_header);
            header.setText(cell.getStrGroup());
        }
        else
        {
            v = inflater.inflate(R.layout.list_cell_item, null);
            TextView item_name = v.findViewById(R.id.txtProductName);
            TextView amazon = v.findViewById(R.id.txtShopName);
            TextView period = v.findViewById(R.id.txtPeriod);
            Button status = v.findViewById(R.id.btnStatus);
            Button warning = v.findViewById(R.id.btnWarning);

            item_name.setText(cell.getStrProductName());
            amazon.setText(cell.getStrShopName());
            period.setText(cell.getStrPeriod());
            status.setText(cell.getStrStatus());

            switch (cell.getStrStatus())
            {
                case "En route":
                    status.setBackgroundResource(R.drawable.roundyellow);
                    break;
                case "Delivered":
                    status.setBackgroundResource(R.drawable.roundgreen);
                    break;
                case "Service":
                    status.setBackgroundResource(R.drawable.roundred);
                    break;
                case "Packing":
                    status.setBackgroundResource(R.drawable.roundpurple);
                    break;
            }

            if(cell.getStrWarning() != null) {
                warning.setText(cell.getStrWarning());
            }else{
                warning.setVisibility(View.GONE);
            }

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mainIntent = new Intent(context,
                            TKPurchaseDetailActivity.class);
                    context.startActivity(mainIntent);
                }
            });
        }
        return v;
    }
}
