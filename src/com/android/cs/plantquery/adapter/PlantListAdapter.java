package com.android.cs.plantquery.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.cs.plantquery.R;
import com.android.cs.plantquery.bean.Plant;

/**
 * 植物列表
 * @author CHENSHI
 * 
 */
public class PlantListAdapter extends BaseAdapter {
	private List<Plant> list;
	private Context context;

	public void setList(List<Plant> list) {
		this.list = list;
	}

	public List<Plant> getList() {
		return list;
	}

	/**
	 * 植物列表
	 * @param context
	 * @param list
	 */
	public PlantListAdapter(Context context, List<Plant> list) {
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
		final Plant plant = list.get(position);
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.search_list_item, null);
			holder.txtView_Id = (TextView) convertView.findViewById(R.id.txt_ID);
			holder.txtView_name = (TextView) convertView.findViewById(R.id.txt_name);
			holder.txtView_alias = (TextView) convertView.findViewById(R.id.txt_alias);
			holder.txtView_latinName = (TextView) convertView.findViewById(R.id.txt_latinName);
			holder.txtView_belongs = (TextView) convertView.findViewById(R.id.txt_belongs);
			holder.txtView_branch = (TextView) convertView.findViewById(R.id.txt_branch);
			holder.txtView_city = (TextView) convertView.findViewById(R.id.txt_city);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		holder.txtView_Id.setText(String.valueOf(plant.getId()));
		holder.txtView_name.setText(plant.getName()!=null?plant.getName():"");
		holder.txtView_alias.setText(plant.getAlias()!=null?plant.getAlias():"");
		holder.txtView_latinName.setText(plant.getLatinName()!=null?plant.getLatinName():"");
		holder.txtView_belongs.setText(plant.getBelongs()!=null?plant.getBelongs():"");
		holder.txtView_branch.setText(plant.getBranch()!=null?plant.getBranch():"");
		holder.txtView_city.setText(plant.getCity()!=null?plant.getCity():"");

		return convertView;
	}

	class Holder {
		public TextView txtView_Id;
		public TextView txtView_name;
		public TextView txtView_alias;
		public TextView txtView_latinName;
		public TextView txtView_belongs;
		public TextView txtView_branch;
		public TextView txtView_city;
	}
}
