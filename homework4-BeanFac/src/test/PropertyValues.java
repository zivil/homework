package test;

import java.util.ArrayList;
import java.util.List;

/**
 * 包装一个对象所有的PropertyValue。<br/>
 * */
public class PropertyValues {

	private final List<PropertyValue> propertyValueList = new ArrayList<PropertyValue>();

	public PropertyValues() {
	}

	public void addPropertyValue(PropertyValue pv) {
		this.propertyValueList.add(pv);
	}

	public PropertyValue getPropertyValue(int index) {
		return this.propertyValueList.get(index);
	}
	
	public List<PropertyValue> getPropertyValues() {
		return this.propertyValueList;
	}
	
	public List<Object> toArray(){
		int len = propertyValueList.size();
		List<Object> objs = new ArrayList<Object>();
		for(int i = 0; i < len; i++){
			objs.set(i, propertyValueList.get(i).getValue().toString());
		}
		return objs;
	}
}
