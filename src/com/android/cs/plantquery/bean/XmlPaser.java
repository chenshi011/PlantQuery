package com.android.cs.plantquery.bean;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.android.cs.plantquery.R;

/**
 * 解析xml
 * @author chenshi
 */
public class XmlPaser {
	/**
	 * 由配置的Xml生成底部控件
	 * @param context
	 * @return
	 */
	public static final List<MainFooterWidget> getBaseWidgetsListFromXml(
			Context context) {
		List<MainFooterWidget> gadgets = null;
		// 通过Resources，获得XmlResourceParser实例
		XmlResourceParser xrp = context.getResources().getXml(
				R.xml.map_footer_widgets);
		try {
			int eventType = xrp.getEventType();
			MainFooterWidget gadget = null;
			int index = 0;
			while(eventType!=XmlPullParser.END_DOCUMENT)
			{
				switch(eventType)
				{
					case XmlPullParser.START_DOCUMENT:
						//
						gadgets = new ArrayList<MainFooterWidget>();
						break;
					case XmlPullParser.START_TAG:
						//
						String tagName = xrp.getName();
						if("widget".equals(tagName))
						{
							gadget = new MainFooterWidget();
							gadget.setIndex(index);
							index++;
						}
						else if(gadget!=null)
						{
							if("id".equals(tagName))
								gadget.setId(xrp.nextText());
							else if("name".equals(tagName))
								gadget.setName(xrp.nextText());
							else if("icon".equals(tagName))
							{
								int icon = getGenRidByName(context,xrp.nextText());
								gadget.setIcon(icon);
							}
						}
						break;
					case XmlPullParser.END_TAG:
						//
						if("widget".equals(xrp.getName()))
							gadgets.add(gadget);
						break;
				}
				eventType = xrp.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return gadgets;
	}
	
	/**
	 * 由配置的Xml生成Excel模板配置
	 * @param context
	 * @return
	 */
	public static final ExcelBean getExcelBeanFromXml(
			Context context) {
		ExcelBean excelBean = null;
		// 通过Resources，获得XmlResourceParser实例
		XmlResourceParser xrp = context.getResources().getXml(
				R.xml.excel_template);
		try {
			int eventType = xrp.getEventType();
			while(eventType!=XmlPullParser.END_DOCUMENT)
			{
				switch(eventType)
				{
					case XmlPullParser.START_DOCUMENT:
						//
						excelBean = new ExcelBean();
						break;
					case XmlPullParser.START_TAG:
						//
						String tagName = xrp.getName();
						if("sheet".equals(tagName))
						{
							excelBean.setSheetName(xrp.getAttributeValue(null, "name"));
							excelBean.setBeginRow(Integer.valueOf(xrp.getAttributeValue(null, "beginRow")));
						}
						else if("id".equals(tagName))
							excelBean.setId_column(Integer.valueOf(xrp.nextText()));
						else if("name".equals(tagName))
							excelBean.setName_column(Integer.valueOf(xrp.nextText()));
						else if("alias".equals(tagName))
							excelBean.setAlias_column(Integer.valueOf(xrp.nextText()));
						else if("latinName".equals(tagName))
							excelBean.setLatinName_column(Integer.valueOf(xrp.nextText()));
						else if("belongs".equals(tagName))
							excelBean.setBelongs_column(Integer.valueOf(xrp.nextText()));
						else if("branch".equals(tagName))
							excelBean.setBranch_column(Integer.valueOf(xrp.nextText()));
						else if("city".equals(tagName))
							excelBean.setCity_column(Integer.valueOf(xrp.nextText()));
						else if("details".equals(tagName))
							excelBean.setDetails_column(Integer.valueOf(xrp.nextText()));
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				eventType = xrp.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return excelBean;
	}
	
	/**
	 * 获取城市列表
	 * @param context
	 * @return
	 */
	public static final List<String> getCityListFromXml(Context context){
		return getComBoxBeanFromXml(context,R.xml.citys);
	}
	
	/**
	 * 获取筛选区域列表
	 * @param context
	 * @return
	 */
	public static final List<String> getAreaListFromXml(Context context){
		return getComBoxBeanFromXml(context,R.xml.area);
	}
	
	/**
	 * 获取种类列表
	 * @param context
	 * @return
	 */
	public static final List<String> getSortListFromXml(Context context){
		return getComBoxBeanFromXml(context,R.xml.sort);
	}
			
	
	public static final List<String> getComBoxBeanFromXml(
			Context context,int xmlId) {
		List<String> citys = null;
		// 通过Resources，获得XmlResourceParser实例
		XmlResourceParser xrp = context.getResources().getXml(xmlId);
		try {
			int eventType = xrp.getEventType();
			ComBoxBean comBoxBean = null;
			while(eventType!=XmlPullParser.END_DOCUMENT)
			{
				switch(eventType)
				{
					case XmlPullParser.START_DOCUMENT:
						//
						citys = new ArrayList<String>();
						break;
					case XmlPullParser.START_TAG:
						//
						String tagName = xrp.getName();
						if("option".equals(tagName))
						{
//							comBoxBean = new ComBoxBean();
//							comBoxBean.setIndex(Integer.valueOf(xrp.getAttributeValue(null, "value")));
//							comBoxBean.setTitle(xrp.nextText());
							citys.add(xrp.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				eventType = xrp.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return citys;
	}
	
	/**
	 * 获取系统设置配置
	 * @param context
	 * @return
	 */
	public static final List<Setting> getSettingListFromXml(
			Context context) {
		List<Setting> result = null;
		// 通过Resources，获得XmlResourceParser实例
		XmlResourceParser xrp = context.getResources().getXml(
				R.xml.setting);
		try {
			int eventType = xrp.getEventType();
			Setting setting = null;
			int index = 0;
			while(eventType!=XmlPullParser.END_DOCUMENT)
			{
				switch(eventType)
				{
					case XmlPullParser.START_DOCUMENT:
						//
						result = new ArrayList<Setting>();
						break;
					case XmlPullParser.START_TAG:
						//
						String tagName = xrp.getName();
						if("item".equals(tagName))
						{
							setting = new Setting();
							String sCheckBox = xrp.getAttributeValue(null, "checkbox").toLowerCase();
							setting.setUseCheckbox(Boolean.parseBoolean(sCheckBox));
							
							String sset = xrp.getAttributeValue(null, "setting");
							setting.setSetting(sset);
						}
						else if(setting!=null)
						{
							if("title".equals(tagName))
								setting.setTitle(xrp.nextText());
							else if("summary".equals(tagName))
								setting.setSummary(xrp.nextText());
							else if("summary_checked".equals(tagName))
								setting.setSummary_checked(xrp.nextText());
							else if("mode".equals(tagName))
								setting.setMode(Integer.parseInt(xrp.nextText()));
							else if("action".equals(tagName))
								setting.setAction(xrp.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						//
						if("item".equals(xrp.getName()))
							result.add(setting);
						break;
				}
				eventType = xrp.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * 根据R资源类型获取对应int值
	 * 
	 * @param name
	 *            参数 如 R.drawable.defult
	 * @return
	 */
	public static final int getGenRidByName(Context context, String name) 
	{
		String[] names = name.split("\\.");
		if (names.length != 3)		return 0;
		String RPath = names[0];
		String RType = names[1];
		String RId = names[2];
		try {
			Class<?> c = Class.forName(context.getPackageName() + "." + RPath);
			Class<?>[] classes = c.getDeclaredClasses();
			for (Class<?> subClass : classes) {
				if (subClass.getSimpleName().equals(RType)) {
					Field[] fields = subClass.getDeclaredFields();
					for (Field field : fields) {
						if (field.getName().equals(RId)) {
							return field.getInt(null);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
