package org.brijframework.resources.group;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.brijframework.group.impl.DefaultGroup;
import org.brijframework.resources.Resource;

public class FileResourceGroup implements DefaultGroup {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object id;
	private ConcurrentHashMap<String, Resource> cache = new ConcurrentHashMap<>();

	public FileResourceGroup(String id) {
		this.setGroupKey(id);
	}

	public void setGroupKey(Object id) {
		this.id=id;
	}

	public void init() {
	}

	@Override
	public ConcurrentHashMap<String, Resource> getCache() {
		return cache;
	}

	public void destory() {
	}

	protected ClassLoader getClassLoader() {
		return this.getClass().getClassLoader();
	}
	
	@Override
	public Object getGroupKey() {
		return id;
	}

	
	@Override
	public String toString() {
		AtomicInteger count=new AtomicInteger(0);
		StringBuilder builder=new StringBuilder();
		builder.append(this.getClass().getSimpleName()+"(");
		getCache().forEach((key,value)->{
			builder.append(key+" = "+value.toString());
			if(count.getAndIncrement()<getCache().size()-1) {
				builder.append(",");
			}
		});
		builder.append(")");
		return builder.toString();
	}

	@Override
	public <T> T find(String parentID, Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

}
