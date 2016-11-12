package test;


public class Bean {

	private String beanClassName;

	private PropertyValues propertyValues = new PropertyValues();

	public String getBeanClassName() {
		return beanClassName;
	}
	
	public void setBeanClassName(String name) {
		beanClassName = name;
	}
	
	public PropertyValues getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(PropertyValues propertyValues) {
		this.propertyValues = propertyValues;
	}
}
