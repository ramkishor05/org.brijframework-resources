package org.brijframework.resources.factory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.factories.Factory;
import org.brijframework.resources.Resource;
import org.brijframework.resources.files.json.JsonResource;
import org.brijframework.support.enums.ResourceType;
import org.brijframework.util.resouces.ResourcesUtil;

public interface ResourceFactory extends Factory {
	public static final String MANIFEST_MF = "MANIFEST.MF";
	public static final String POM_PROPERTIES = "pom.properties";
	public static final String POM_XML = "pom.xml";
	public static final String META_INF = "META-INF";
	public static final String All_INF = "";
	public static final String COM_INF = "comman";
	
	public ResourceType getResourceType();
	
	public Collection<? extends Resource> getResources();

	void load(Resource metaResource);
	
	default ResourceFactory loadFactory() {
		this.clear();
		try {
			for (File file : ResourcesUtil.getResources(All_INF)) {
				if(file.getName().endsWith(getResourceType().toString()) && !isIgnoreFile(file)) {
					load(build(file));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	ConcurrentHashMap<Object, ? extends Resource> getCache();
	
	@Override
	default ResourceFactory clear() {
		this.getCache().clear();
		return this;
	}

	public Resource build(File file);
	
	default boolean isIgnoreFile(File file) {
		if (MANIFEST_MF.equalsIgnoreCase(file.getName()) || POM_PROPERTIES.equalsIgnoreCase(file.getName())
				|| POM_XML.equalsIgnoreCase(file.getName())) {
			return true;
		}
		return false;
	}

	Collection<JsonResource> getResources(String dir);
}
