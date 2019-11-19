package org.brijframework.resources.container.impl;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.brijframework.container.impl.AbstractContainer;
import org.brijframework.group.Group;
import org.brijframework.resources.container.ResourceContainer;
import org.brijframework.resources.factory.FileResourceFactory;
import org.brijframework.resources.group.FileResourceGroup;
import org.brijframework.support.config.Assignable;
import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;
import org.brijframework.util.resouces.ResourcesUtil;
import org.brijframework.util.text.StringUtil;

public class FileResourceContainer extends AbstractContainer implements ResourceContainer{

	public static final String MANIFEST_MF = "MANIFEST.MF";
	public static final String POM_PROPERTIES = "pom.properties";
	public static final String POM_XML = "pom.xml";
	public static final String META_INF = "META-INF";
	public static final String All_INF = "";
	public static final String COM_INF = "comman";

	private static FileResourceContainer container;

	@Assignable
	public static FileResourceContainer getContainer() {
		if (container == null) {
			container = InstanceUtil.getSingletonInstance(FileResourceContainer.class);
		}
		return container;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls -> {
				if (FileResourceFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends FileResourceFactory>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls -> {
				if (FileResourceFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends FileResourceFactory>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Map<String, File> fileMapping(File file) throws URISyntaxException {
		LinkedHashMap<String, File> mapping = new LinkedHashMap<>();
		String pathKey = "";
		String[] pathList = StringUtil.getOfter(file.getAbsolutePath(), META_INF).split("/");
		for (int i = 0; i < pathList.length; i++) {
			if (!pathKey.isEmpty()) {
				pathKey += "/";
			}
			pathKey = pathKey + pathList[i];
			URL url = ResourcesUtil.getResource(META_INF + pathKey);
			Assertion.notNull(url, AssertMessage.arg_null_or_empty_message);
			mapping.put(pathKey, new File(url.toURI()));
		}
		return mapping;
	}

	public boolean isIgnoreFile(File file) {
		if (MANIFEST_MF.equalsIgnoreCase(file.getName()) || POM_PROPERTIES.equalsIgnoreCase(file.getName())
				|| POM_XML.equalsIgnoreCase(file.getName())) {
			return true;
		}
		return false;
	}

	@Override
	public Group load(Object groupKey) {
		Group group=getCache().get(groupKey);
		if(group==null){
			group=new FileResourceGroup((String)groupKey);
		}
		return group;
	}

}
