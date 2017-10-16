package flashbox.tracck.home.chat;

import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import flashbox.tracck.R;
import flashbox.tracck.common.Constants;
import flashbox.tracck.model.TKChat;

public class TKChatListAdapter extends BaseAdapter {
	private static final int TYPE_MAX_COUNT = Constants.MSG_VIEW_RECOMMEND + 1;
	private ArrayList<TKChat> comments;
	private Context mContext;
	private LayoutInflater inflater;

	public TKChatListAdapter(Context context, ArrayList<TKChat> comments) {
		this.mContext = context;
		this.comments = comments;

		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getItemViewType(int position) {
		return getItem(position).getIntMsgType();
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		int intMsgType = getItemViewType(position);
		TKChat item = getItem(position);

		if (convertView == null) {
			holder = new ViewHolder();
			switch (intMsgType) {
				case Constants.MSG_NOTIFY:
					convertView = inflater.inflate(R.layout.list_row_started, null);
					holder.txtMsg = (TextView) convertView.findViewById(R.id.btnMsg);
					holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
					break;
				case Constants.MSG_SCHEDULED_CALL:
					convertView = inflater.inflate(R.layout.list_row_odd_schedulecall, null);
					holder.btnMsg = (Button) convertView.findViewById(R.id.btnMsg);
					holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
					holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
					break;
				case Constants.MSG_RECEIVED:
					convertView = inflater.inflate(R.layout.list_row_even, null);
					holder.btnMsg = (Button) convertView.findViewById(R.id.btnMsg);
					holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
					break;
				case Constants.MSG_SENT:
					convertView = inflater.inflate(R.layout.list_row_odd, null);
					holder.btnMsg = (Button) convertView.findViewById(R.id.btnMsg);
					holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
					break;
				case Constants.MSG_FEEDBACK:
					convertView = inflater.inflate(R.layout.list_row_odd_feedback, null);
					holder.btnMsg = (Button) convertView.findViewById(R.id.btnMsg);
					holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
					break;
				case Constants.MSG_VIEW_RECOMMEND:
					convertView = inflater.inflate(R.layout.list_row_view, null);
					holder.btnMsg = (Button) convertView.findViewById(R.id.btnMsg);
					holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
					break;
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}

		Spanned spanText = item.getSpanMsg();

		switch (intMsgType) {
			case Constants.MSG_VIEW_RECOMMEND:
				break;
			case Constants.MSG_NOTIFY:
				holder.txtMsg.setText(spanText);
				holder.txtDate.setText(item.getStrDate());
				break;
			case Constants.MSG_SCHEDULED_CALL:
				holder.btnMsg.setText(spanText);
				holder.txtTime.setText(item.getStrTime());
				holder.txtDate.setText(item.getStrDate());
				break;
			default:
				holder.btnMsg.setText(spanText);
				holder.txtTime.setText(item.getStrTime());
				break;
		}

		return convertView;
	}

	@Override
	public int getCount() {
		return comments.size();
	}

	@Override
	public TKChat getItem(int position) {
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public TextView txtMsg;
		public TextView txtDate;
		public Button btnMsg;
		public TextView txtTime;
	}
}
