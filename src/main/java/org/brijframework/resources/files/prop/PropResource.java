package org.brijframework.resources.files.prop;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.brijframework.resources.files.MetaResource;

public class PropResource extends MetaResource {

	public PropResource(String path) {
		super(path);
	}
	
	public PropResource(File path) {
		super(path);
	}

	public PropResource() {
	}
	
	public Properties getProperties(){
		Properties properties=new Properties();
		try {
			properties.load(this.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	

}
