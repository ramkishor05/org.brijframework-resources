package org.brijframework.resources.files;

import java.util.Map;
import java.util.Properties;

import org.brijframework.asm.resources.DefaultResource;

public interface MapResource extends DefaultResource  {

	public Properties getProperties();
	
	public Map<String, Object> getHashMap();
}
