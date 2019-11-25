package org.brijframework.resources.config.impl;

import org.brijframework.resources.config.ResourceConfig;

public class ResourceConfigration implements ResourceConfig{
	
	private boolean enable;
	private String location;
	private String packages;
	
	public ResourceConfigration() {
	}

	public ResourceConfigration(String location, boolean enable) {
		this();
		this.location=location;
		this.enable=enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}
	
	public String getPackages() {
		return packages;
	}
	
	public void setPackages(String packages) {
		this.packages = packages;
	}
	
}