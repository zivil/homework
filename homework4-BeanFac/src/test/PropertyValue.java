package test;

/**
 * 用于bean的属性注入
 */
public class PropertyValue {

    private final String name;

    private final Object value;
    
    private String type;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
