package org.brijframework.resources.container;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.asm.container.DefaultContainer;
import org.brijframework.container.Container;
import org.brijframework.group.Group;
import org.brijframework.resources.factory.ResourceFactory;
import org.brijframework.resources.group.ResourceGroup;
import org.brijframework.support.model.Assignable;
import org.brijframework.support.model.DepandOn;
import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.MethodUtil;
import org.brijframework.util.reflect.ReflectionUtils;
import org.brijframework.util.resouces.ResourcesUtil;
import org.brijframework.util.text.StringUtil;

public class ResourceContainer implements DefaultContainer {

	public static final String MANIFEST_MF = "MANIFEST.MF";
	public static final String POM_PROPERTIES = "pom.properties";
	public static final String POM_XML = "pom.xml";
	public static final String META_INF = "META-INF";
	public static final String All_INF = "";
	public static final String COM_INF = "comman";
	

	private ConcurrentHashMap<Object, Group> cache = new ConcurrentHashMap<>();
	private static ResourceContainer container;

	@Assignable
	public static ResourceContainer getContainer() {
		if (container == null) {
			container = InstanceUtil.getSingletonInstance(ResourceContainer.class);
			container.loadContainer();
		}
		return container;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Container loadContainer() {
		List<Class<? extends ResourceFactory>> classes=new ArrayList<>();
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls->{
				if(ResourceFactory.class.isAssignableFrom(cls) && !cls.isInterface() && cls.getModifiers() != Modifier.ABSTRACT) {
					classes.add((Class<? extends ResourceFactory>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls->{
				if(ResourceFactory.class.isAssignableFrom(cls) && !cls.isInterface() && cls.getModifiers() != Modifier.ABSTRACT) {
					classes.add((Class<? extends ResourceFactory>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		classes.forEach(( resourceFactory)->{
			System.err.println("Factory      : "+resourceFactory.getName());
			if(resourceFactory.isAnnotationPresent(DepandOn.class)) {
			   DepandOn depandOn=resourceFactory.getAnnotation(DepandOn.class);
			   loading(depandOn.depand());
			}
			loading(resourceFactory);
		});
		/*try {
			for (File file : ResourcesUtil.getResources(All_INF)) {
				if (isIgnoreFile(file)) {
					continue;
				}
				String groupKey = null;
				if (file.isDirectory()) {
					groupKey = file.getName();

				} else {
					if (file.getName().lastIndexOf(".") != -1) {
						groupKey = file.getName().substring(file.getName().lastIndexOf(".") + 1,
								file.getName().length());
					}
				}
				if (groupKey == null) {
					groupKey = COM_INF;
				}
				ResourceGroup group = (ResourceGroup) this.search(groupKey);
				if (group == null) {
					group = new ResourceGroup(groupKey);
					this.add(groupKey, group);
				}
				Resource resource = new MetaResource(file);
				group.add(resource.getId(), resource);
				this.update(groupKey, group);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return container;
	}

	private void loading(Class<?> cls) {
		boolean called=false;
		for(Method method:MethodUtil.getAllMethod(cls)) {
			if(method.isAnnotationPresent(Assignable.class)) {
				try {
					ResourceFactory resourceFactory=(ResourceFactory) method.invoke(null);
					resourceFactory.loadFactory();
					called=true;
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		if(!called) {
			try {
				ResourceFactory container=(ResourceFactory) cls.newInstance();
				container.loadFactory();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
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
	public ConcurrentHashMap<Object, Group> getCache() {
		return cache;
	}

	@Override
	public Group load(Object groupKey) {
		Group group=getCache().get(groupKey);
		if(group==null){
			group=new ResourceGroup((String)groupKey);
		}
		return group;
	}

}
