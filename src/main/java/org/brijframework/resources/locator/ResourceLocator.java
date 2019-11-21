package org.brijframework.resources.locator;

public enum ResourceLocator {

	CLASSPATH("classpath:","classes"),
	FILEPATH("filepath:","file:");
	
	String locator;
	String types;
	
	private ResourceLocator(String locator, String types) {
		this.locator = locator;
		this.types = types;
	}
	
	public String getLocator() {
		return locator;
	}
	
	public String getTypes() {
		return types;
	}
	
}
