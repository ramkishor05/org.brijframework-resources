package org.brijframework.resources.factory;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.factories.impl.FileFactory;
import org.brijframework.resources.Resource;

public interface ResourceFactory<K,T> extends FileFactory<K,T> {
	
	public String getResourceType();
	
	public Collection<T> getResources();

	void load(Resource metaResource);
	
	ConcurrentHashMap<K,T> getCache();
	
	public Resource build(String id, File file);
	
	default boolean isIgnoreFile(File file) {
		if (MANIFEST_MF.equalsIgnoreCase(file.getName()) || POM_PROPERTIES.equalsIgnoreCase(file.getName())
				|| POM_XML.equalsIgnoreCase(file.getName())) {
			return true;
		}
		return false;
	}

	Collection<T> getResources(K dir);

	
}
