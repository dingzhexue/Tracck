package flashbox.tracck.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import flashbox.tracck.R;
import flashbox.tracck.common.Common;
import flashbox.tracck.common.Constants;
import flashbox.tracck.home.details.TKPurchaseDetailActivity;
import flashbox.tracck.model.TKPurchase;
import android.widget.Filter;

public class TKPurchaseListAdapter extends ArrayAdapter implements Filterable {
    private static final int TYPE_MAX_COUNT = Constants.PURCHASE_ITEM + 1;
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<TKPurchase> itemsTemp;
    private ArrayList<TKPurchase> search_items;
    private static TKPurchase curCell;

    public TKPurchaseListAdapter(Context context, ArrayList<TKPurchase> items) {
        super(context, 0, items);

        this.context = context;
        this.itemsTemp = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<TKPurchase> results = new ArrayList<TKPurchase>();
                if (search_items == null)
                    search_items = itemsTemp;
                if (constraint != null) {
                    if (search_items != null && search_items.size() > 0) {
                        for (final TKPurchase g : search_items) {
                            if (g.getItemType() == Constants.PURCHASE_ITEM && g.getStrProductName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          Filter.FilterResults results) {
                itemsTemp = (ArrayList<TKPurchase>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getItemType();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return itemsTemp.size();
    }


    @Override
    public TKPurchase getItem(int position) {
        return itemsTemp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TKPurchase cell = getItem(position);
        ViewHolder holder = null;

        int rowType = getItemViewType(position);

        if (convertView == null) {
            //If the cell is a section header we inflate the header layout
            holder = new ViewHolder();

            switch (rowType){
                case Constants.PURCHASE_HEADER:
                    convertView = inflater.inflate(R.layout.list_header_item, null);

                    holder.txtSectionHeader = convertView.findViewById(R.id.section_header);
                    break;

                case Constants.PURCHASE_ITEM:
                    convertView = inflater.inflate(R.layout.list_cell_item, null);

                    holder.imgPriductPreview = convertView.findViewById(R.id.imgPriductPreview);
                    holder.txtProductName = convertView.findViewById(R.id.txtProductName);
                    holder.txtShopName = convertView.findViewById(R.id.txtShopName);
                    holder.txtPeriod = convertView.findViewById(R.id.txtPeriod);
                    holder.btnStatus = convertView.findViewById(R.id.btnStatus);
                    holder.btnWarning = convertView.findViewById(R.id.btnWarning);

                    break;
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (rowType){
            case Constants.PURCHASE_HEADER:
                holder.txtSectionHeader.setText(cell.getStrDate());
                break;
            case Constants.PURCHASE_ITEM:
                holder.txtProductName.setText(cell.getStrProductName());
                holder.txtShopName.setText(cell.getStrShopName());
                holder.txtPeriod.setText(cell.getStrPeriod());
                holder.btnStatus.setText(cell.getStrStatus());

                switch (cell.getStrStatus()) {
                    case "En route":
                        holder.btnStatus.setBackgroundResource(R.drawable.roundyellow);
                        break;
                    case "Delivered":
                        holder.btnStatus.setBackgroundResource(R.drawable.roundgreen);
                        break;
                    case "Service":
                        holder.btnStatus.setBackgroundResource(R.drawable.roundred);
                        break;
                    case "Packing":
                        holder.btnStatus.setBackgroundResource(R.drawable.roundpurple);
                        break;
                }

                if (cell.getStrWarning() != null) {
                    holder.btnWarning.setVisibility(View.VISIBLE);
                    holder.btnWarning.setText(cell.getStrWarning());
                } else {
                    holder.btnWarning.setVisibility(View.GONE);
                }

                holder.imgPriductPreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickPurchaseItem(cell);
                    }
                });
                holder.txtProductName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickPurchaseItem(cell);
                    }
                });
                holder.txtShopName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickPurchaseItem(cell);
                    }
                });
                holder.txtPeriod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickPurchaseItem(cell);
                    }
                });
                holder.btnStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickPurchaseItem(cell);
                    }
                });
                holder.btnWarning.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickPurchaseItem(cell);
                    }
                });
                break;
        }

        return convertView;
    }

    public void onClickPurchaseItem(TKPurchase cell) {
        Intent intent = new Intent(context, TKPurchaseDetailActivity.class);
        Common.setPurchase(cell);
        context.startActivity(intent);
    }

    public static class ViewHolder {
        ImageView imgPriductPreview;
        TextView txtSectionHeader;
        TextView txtProductName;
        TextView txtShopName;
        TextView txtPeriod;
        Button btnWarning;
        Button btnStatus;
    }
}
