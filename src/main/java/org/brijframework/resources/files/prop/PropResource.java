package org.brijframework.resources.files.prop;

import java.io.File;

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

}
