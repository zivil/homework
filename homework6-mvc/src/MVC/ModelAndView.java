package MVC;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class ModelAndView {
	private String view;
	
	
	private HttpServletRequest request;
	
	public ModelAndView(){
	}
	
	public void setRequest(HttpServletRequest request){
		this.request = request;
	}
	
	public void setViewName(String string) {
		// TODO Auto-generated method stub
		view = string;
	}
	
	public String getView(){
		return view+".jsp";
	}
	
	public Object getMap(String string) {
		// TODO Auto-generated method stub
		return request.getParameter(string);
	}

	public void addObject(String string, Object map) {
		// TODO Auto-generated method stub
		request.setAttribute(string, map);
	}

}
