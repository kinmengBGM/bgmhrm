package com.beans.leaveapp.refresh;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;


public class Refresh {

	public void refreshPage() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String refreshpage = facesContext.getViewRoot().getViewId();
		ViewHandler viewHandler =facesContext.getApplication().getViewHandler();
		UIViewRoot uiViewroot = viewHandler.createView(facesContext,refreshpage);
		uiViewroot.setViewId(refreshpage);
		facesContext.setViewRoot(uiViewroot);	
}
}	
