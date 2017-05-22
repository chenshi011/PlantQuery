package com.android.cs.plantquery.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.cs.plantquery.R;

/**
 * 翻页控件
 * 
 * @author CHENSHI
 * 
 */
public class PagingBar extends RelativeLayout implements OnClickListener {
	TextView txt_info;

	Context context;
	private int totalcount = 0;
	private int pagesize = 10;
	private int pagecount = 0;
	private int currentPage = 1;

	private OnPageChangeListener onPageChangeListener;

	public int getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
		if (pagesize > 0)
			pagecount = (int) totalcount / pagesize
					+ (totalcount % pagesize > 0 ? 1 : 0);
		invalidateTextViews();
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
		if (pagesize > 0)
			pagecount = (int) totalcount / pagesize
					+ (totalcount % pagesize > 0 ? 1 : 0);
		invalidateTextViews();
	}

	public int getPagecount() {
		return pagecount;
	}

	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		invalidateTextViews();
	}

	public void setOnPageChangeListener(
			OnPageChangeListener onPageChangeListener) {
		this.onPageChangeListener = onPageChangeListener;
	}

	public PagingBar(Context context) {
		this(context, null);
	}

	public PagingBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0, 10);
	}

	public PagingBar(Context context, AttributeSet attrs, int totalcount,
			int pagesize) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.paging_bar, this);
		this.context = context;
		this.totalcount = totalcount;
		this.pagesize = pagesize;
		if (pagesize > 0)
			pagecount = (int) totalcount / pagesize
					+ (totalcount % pagesize > 0 ? 1 : 0);
		initViews();
	}

	private void initViews() {
		txt_info = (TextView) findViewById(R.id.txt_info);
		View v_fisrt_page = findViewById(R.id.txt_first_page);
		View v_pre_page = findViewById(R.id.txt_pre_page);
		View v_next_page = findViewById(R.id.txt_next_page);
		View v_end_page = findViewById(R.id.txt_end_page);

		v_fisrt_page.setOnClickListener(this);
		v_pre_page.setOnClickListener(this);
		v_next_page.setOnClickListener(this);
		v_end_page.setOnClickListener(this);
		invalidateTextViews();
	}

	@Override
	public void onClick(View v) {
		int oldpage = currentPage;
		switch (v.getId()) {
		case R.id.txt_first_page:
			currentPage = 1;
			break;
		case R.id.txt_pre_page:
			currentPage--;
			if (currentPage < 1) {
				currentPage = 1;
			}
			break;
		case R.id.txt_next_page:
			currentPage++;
			if (currentPage > pagecount) {
				currentPage = pagecount;
			}
			break;
		case R.id.txt_end_page:
			currentPage = pagecount;
			break;
		default:
			break;
		}

		if (onPageChangeListener != null && oldpage != currentPage)
			onPageChangeListener.onchange(currentPage);
		invalidateTextViews();
	}

	private void invalidateTextViews() {
		String searh_record = String.format(
				context.getString(R.string.paging_bar_info), pagecount,
				currentPage, totalcount);

		String sPage = String.valueOf(pagecount);
		String sCurrentPage = String.valueOf(currentPage);
		String sTotalcount = String.valueOf(totalcount);

		SpannableStringBuilder style = new SpannableStringBuilder(searh_record);
		style.setSpan(
				new ForegroundColorSpan(context.getResources().getColor(
						R.color.paging_bar_page)), searh_record.indexOf(sPage),
				searh_record.indexOf(sPage) + sPage.length(),
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

		style.setSpan(
				new ForegroundColorSpan(context.getResources().getColor(
						R.color.paging_bar_page)),
				midIndexOf(searh_record, sPage, sCurrentPage),
				midIndexOf(searh_record, sPage, sCurrentPage)
						+ sCurrentPage.length(),
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

		style.setSpan(
				new ForegroundColorSpan(context.getResources().getColor(
						R.color.paging_bar_total)),
				searh_record.lastIndexOf(sTotalcount),
				searh_record.lastIndexOf(sTotalcount) + sTotalcount.length(),
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

		txt_info.setText(style);

		// txt_info.setText(searh_record);
	}

	private int midIndexOf(String str, String frommatchs, String matchs) {
		int from = str.indexOf(frommatchs) + frommatchs.length();
		str = str.substring(from, str.length()  - 1);
		return str.indexOf(matchs) + from;
	}

	/***
	 * 翻页事件
	 * 
	 * @author cs
	 * @email hhuchenshi@gmail.com
	 * @update 2013-10-29 下午2:32:23
	 * @version 1.0
	 */
	public interface OnPageChangeListener {
		void onchange(int page);
	}
}
