package org.brijframework.resources.files;

import java.io.File;

import org.brijframework.util.text.StringUtil;

public class MetaResource extends FileResource {
	
	private static final String META_INF = "META-INF/";

	public MetaResource(File file) {
		super(file);
		this.setPath(StringUtil.getOfter(file.getAbsolutePath(), META_INF));
		this.setId(file.getName());
	}

	public MetaResource(String path) {
		super();
		this.setPath(path);
		this.setId(getFile().getName());
	}

	public MetaResource() {
		super();
	}
	public String resource() {
		return new String(this.getContent());
	}

	public void setPath(String path) {
		if (!path.contains(META_INF)) {
			path = META_INF + path;
		}
		super.setPath(path);
	}
}
