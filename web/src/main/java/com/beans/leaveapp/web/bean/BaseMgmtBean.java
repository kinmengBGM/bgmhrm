package com.beans.leaveapp.web.bean;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import com.beans.leaveapp.refresh.Refresh;

public class BaseMgmtBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3163673178573695415L;
	private static ResourceBundle appExceptionMsgBundle = ResourceBundle.getBundle("ExceptionMessages", Locale.getDefault());
	
	public static ResourceBundle getAppExceptionMsgBundle() {
		return appExceptionMsgBundle;
	}
	
	public static void setAppExceptionMsgBundle(ResourceBundle appExceptionMsgBundle) {
		BaseMgmtBean.appExceptionMsgBundle = appExceptionMsgBundle;
	}
	
	public String getExcptnMesProperty(String key)
    {
		if(appExceptionMsgBundle != null && appExceptionMsgBundle.containsKey(key)){
			return appExceptionMsgBundle.getString(key).intern();
		} else{
			return "Error in processing your request, please try after sometime!!!";
		}
    }
	
	public void resetFormFields(){
		new Refresh().refreshPage();
	}
	
	
}
