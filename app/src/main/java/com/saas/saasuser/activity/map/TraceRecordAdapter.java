package com.saas.saasuser.activity.map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saas.saasuser.R;

import java.util.List;

public class TraceRecordAdapter extends BaseAdapter {

	private Context mContext;
	private List<PathRecord> mRecordList;

	public TraceRecordAdapter(Context context, List<PathRecord> list) {
		this.mContext = context;
		this.mRecordList = list;
	}

	@Override
	public int getCount() {
		return mRecordList.size();
	}

	@Override
	public Object getItem(int position) {
		return mRecordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.trace_record_item, null);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.record = (TextView) convertView.findViewById(R.id.record);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PathRecord item = mRecordList.get(position);
		holder.date.setText("开始时间："+item.getDate());
		holder.record.setText("记录编号："+item.getId()+"      行程总长："+item.getDistance()+"      行程耗时："+item.getDuration());
		return convertView;
	}

	private class ViewHolder {
		TextView date;
		TextView record;
	}
}
