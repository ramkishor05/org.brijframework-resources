package org.brijframework.resources.group;

import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.env.Environment;
import org.brijframework.group.impl.DefaultGroup;

public class EnvResourceGroup implements DefaultGroup{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConcurrentHashMap<String, Environment> cache=new ConcurrentHashMap<String, Environment>();
	
	private Object groupKey;

	public EnvResourceGroup(Object groupKey) {
		this.groupKey=groupKey;
	}

	@Override
	public Object getGroupKey() {
		return groupKey;
	}

	@Override
	public ConcurrentHashMap<String, Environment> getCache() {
		return cache;
	}

	@Override
	public <T> T find(String parentID, Class<?> type) {
		return null;
	}

}
