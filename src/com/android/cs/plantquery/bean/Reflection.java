package com.android.cs.plantquery.bean;

import java.lang.reflect.Method;

import android.content.Context;
import android.util.Log;


/**
 * 反射类
 * @author chenshi
 *
 */
public class Reflection {
	private static final String TAG = "Reflection";
	
	/**
	 * 反射的方法（不定参数）
	 * @param object
	 * @param action
	 * @param params
	 */
	@SuppressWarnings("rawtypes")
	public static final void callAction(Context object,String action, Object... params)
	{
		if(action==null) return;
		try {
			Class[] classes = new Class[params.length];
			for (int i = 0; i < classes.length; i++) {
				if(!params.getClass().isPrimitive())
					classes[i] = getParameterTypes(params[i]);
				else {
					classes[i] = params[i].getClass();
				}
			}
			Method method =  object.getClass().getDeclaredMethod(action, classes);
			method.invoke(object,params);
		} catch (Exception e) {
			Log.e(TAG, e.getLocalizedMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static Class<? extends Context> getParameterTypes(Object params) throws Exception {  
        if(params == null){  
            return null;  
        }  
        Class parameterTypes = null;; 
        if(params instanceof Integer){  
            parameterTypes = Integer.TYPE;  
        }else if(params instanceof Byte){  
            parameterTypes = Byte.TYPE;  
        }else if(params instanceof Short){  
            parameterTypes = Short.TYPE;  
        }else if(params instanceof Float){  
            parameterTypes = Float.TYPE;  
        }else if(params instanceof Double){  
            parameterTypes = Double.TYPE;  
        }else if(params instanceof Character){  
            parameterTypes = Character.TYPE;  
        }else if(params instanceof Long){  
            parameterTypes = Long.TYPE;  
        }else if(params instanceof Boolean){  
            parameterTypes = Boolean.TYPE;  
        }else{  
            parameterTypes = params.getClass();  
        }  
        return parameterTypes;  
    }  
}
