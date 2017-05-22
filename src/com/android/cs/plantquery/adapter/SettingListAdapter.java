package com.android.cs.plantquery.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.cs.plantquery.R;
import com.android.cs.plantquery.bean.Setting;
import com.android.cs.plantquery.common.SettingUtils;
import com.android.cs.plantquery.widget.Switch;

/**
 * 设置
 * 
 * @author CHENSHI
 * 
 */
public class SettingListAdapter extends BaseAdapter {
	private List<Setting> list;
	private Context context;
	OnCheckedChangeListener onCheckedChangeListener;

	public void setOnCheckedChangeListener(
			OnCheckedChangeListener onCheckedChangeListener) {
		this.onCheckedChangeListener = onCheckedChangeListener;
	}

	public void setList(List<Setting> list) {
		this.list = list;
	}

	public List<Setting> getList() {
		return list;
	}

	/**
	 * 植物列表
	 * 
	 * @param context
	 * @param list
	 */
	public SettingListAdapter(Context context, List<Setting> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		final Setting setting = list.get(position);
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.setting_list_item, null);
			holder.txtView_title = (TextView) convertView
					.findViewById(R.id.txt_title);
			holder.txtView_summary = (TextView) convertView
					.findViewById(R.id.txt_summary);
			holder.switch_scroll = (Switch) convertView
					.findViewById(R.id.switch_scroll);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.txtView_title.setText(setting.getTitle() != null ? setting
				.getTitle() : "");
		holder.txtView_summary.setText(setting.getSummary() != null ? setting
				.getSummary() : "");
		if (setting.isUseCheckbox()) {
			if (setting.getSetting() != null) {
				boolean check = SettingUtils.getBoolean(context,
						setting.getSetting(), false);
				holder.switch_scroll.setChecked(check);
				if (check)
					holder.txtView_summary
							.setText(setting.getSummary_checked() != null ? setting
									.getSummary_checked() : "");
				else {
					holder.txtView_summary
							.setText(setting.getSummary() != null ? setting
									.getSummary() : "");
				}
			}
			holder.switch_scroll.setVisibility(View.VISIBLE);
			holder.switch_scroll.setTag(holder.txtView_summary);
			holder.switch_scroll
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (onCheckedChangeListener != null) {
								try {
									TextView textView = (TextView) buttonView.getTag();
									if (setting.getSetting() != null) {
										if (isChecked)
											textView
													.setText(setting.getSummary_checked() != null ? setting
															.getSummary_checked() : "");
										else {
											textView
													.setText(setting.getSummary() != null ? setting
															.getSummary() : "");
										}
									}
								} catch (Exception e) {
								}
								
								onCheckedChangeListener.onCheckedChanged(
										buttonView, setting, isChecked);
							}
						}
					});
		} else {
			holder.switch_scroll.setVisibility(View.GONE);
		}

		return convertView;
	}

	class Holder {
		public TextView txtView_title;
		public TextView txtView_summary;
		public Switch switch_scroll;
	}

	public interface OnCheckedChangeListener {
		void onCheckedChanged(CompoundButton buttonView, Setting setting,
				boolean isChecked);
	}
}
