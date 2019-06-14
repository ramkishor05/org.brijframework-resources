package org.brijframework.resources.context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.context.Context;
import org.brijframework.resources.container.ResourceContainer;
import org.brijframework.support.model.Assignable;
import org.brijframework.support.model.DepandOn;
import org.brijframework.util.reflect.MethodUtil;
import org.brijframework.util.reflect.ReflectionUtils;

public class ResourceContext implements Context{
	
	private ConcurrentHashMap<Object, Container> cache=new ConcurrentHashMap<Object, Container>();

	private Context context;

	@Override
	public void initialize(Context context) {
		this.context=context;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void startup() {
		System.err.println("ResourceContext loading start.");
		List<Class<? extends ResourceContainer>> classes = new ArrayList<>();
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls->{
				if(ResourceContainer.class.isAssignableFrom(cls)) {
					classes.add((Class<? extends ResourceContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls->{
				if(ResourceContainer.class.isAssignableFrom(cls)) {
					classes.add((Class<? extends ResourceContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		classes.stream().sorted((container1, container2) -> {
			if (container1.isAnnotationPresent(DepandOn.class)) {
				DepandOn depandOn = container1.getAnnotation(DepandOn.class);
				if (depandOn.depand().equals(container2)) {
					return 1;
				}
			}
			if (container2.isAnnotationPresent(DepandOn.class)) {
				DepandOn depandOn = container2.getAnnotation(DepandOn.class);
				if (depandOn.depand().equals(container1)) {
					return -1;
				}
			}
			return 0;
		}).forEach((Context) -> {
			loading(Context);
		});
		System.err.println("ResourceContext loading done.");
	}

	private void loading(Class<?>cls) {
		if(cls.isInterface() || cls.getModifiers() == Modifier.ABSTRACT) {
			return ;
		}
		boolean called=false;
		for(Method method:MethodUtil.getAllMethod(cls)) {
			if(method.isAnnotationPresent(Assignable.class)) {
				try {
					System.err.println("Container    : "+cls.getName());
					Container container=(Container) method.invoke(null);
					getContainers().put(cls.getSimpleName(), container);
					called=true;
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		if(!called) {
			try {
				System.err.println("Container    : "+cls.getName());
				Container container=(Container) cls.newInstance();
				container.loadContainer();
				getContainers().put(cls.getSimpleName(), container);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void destory() {
	}

	@Override
	public Context getParent() {
		return context;
	}

	@Override
	public ConcurrentHashMap<Object, Container> getContainers() {
		return cache;
	}

}
