package com.android.cs.plantquery.widget;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.cs.plantquery.R;
import com.android.cs.plantquery.bean.XmlPaser;
import com.android.cs.plantquery.common.PlantManager;
import com.android.cs.plantquery.common.UIHelper;

/**
 * 搜索条件
 * 
 * @author CHENSHI
 * 
 */
public class SearchCondition extends LinearLayout {
	Spinner spinner_area, spinner_city, spinner_sort;
	SearchEditText searchEditText;
	View btn_search;
	OnQueryListen onqueryListen;
	Context context;

	public SearchCondition(Context context) {
		this(context, null);
	}

	public SearchCondition(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.search_condition, this);
		init();
	}

	private void init() {
		spinner_area = (Spinner) findViewById(R.id.spinner_area);
		spinner_city = (Spinner) findViewById(R.id.spinner_city);
		spinner_sort = (Spinner) findViewById(R.id.spinner_sort);

		List<String> areas = XmlPaser.getAreaListFromXml(context);
		List<String> citys = XmlPaser.getCityListFromXml(context);
		List<String> sorts = XmlPaser.getSortListFromXml(context);

		ArrayAdapter<String> adapter_areas = new ArrayAdapter<String>(context,
				R.layout.spainner, areas);
		spinner_area.setAdapter(adapter_areas);
		adapter_areas
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ArrayAdapter<String> adapter_citys = new ArrayAdapter<String>(context,
				R.layout.spainner, citys);
		spinner_city.setAdapter(adapter_citys);
		adapter_citys
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ArrayAdapter<String> adapter_sorts = new ArrayAdapter<String>(context,
				R.layout.spainner, sorts);
		spinner_sort.setAdapter(adapter_sorts);
		adapter_sorts
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		searchEditText = (SearchEditText) findViewById(R.id.edit_search_key);
		searchEditText.setHint(getResources().getString(
				R.string.frame_search_keyWord));

		btn_search = findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String skeyWord = "";
				String keys = "";
				String area = "";
				String city = "";
				String sort = "";
				UIHelper.hideSoftInput(context, searchEditText.getWindowToken());
				if (searchEditText != null
						&& !searchEditText.getText().toString().trim()
								.equals(""))
					keys = searchEditText.getText().toString();

				if (spinner_area.getSelectedItemPosition() > 0) {
					area = spinner_area.getSelectedItem().toString();
				}
				if (spinner_city.getSelectedItemPosition() > 0) {
					city = spinner_city.getSelectedItem().toString();
				}
				if (spinner_sort.getSelectedItemPosition() > 0) {
					sort = spinner_sort.getSelectedItem().toString();
				}

				skeyWord += PlantManager.KEY_WORD + "=" + keys + "&"
						+ PlantManager.KEY_WORD_AREA + "=" + area + "&"
						+ PlantManager.KEY_WORD_CITY + "=" + city + "&"
						+ PlantManager.KEY_WORD_SORT + "=" + sort;

				if (onqueryListen != null)
					onqueryListen.onClick(skeyWord);
			}
		});
	}

	public void setOnqueryListen(OnQueryListen onqueryListen) {
		this.onqueryListen = onqueryListen;
	}

	/**
	 * 查询关键字事件
	 * 
	 * @author CHENSHI
	 * 
	 */
	public interface OnQueryListen {
		void onClick(CharSequence keyWord);
	}
}
