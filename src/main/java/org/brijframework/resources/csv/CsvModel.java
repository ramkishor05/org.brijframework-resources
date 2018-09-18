package org.brijframework.resources.csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvModel {
	public Class<?> model;
	public String uniqueKeys;
	public boolean isValidKeys;
	
	public List<CsvMapper> fields=new ArrayList<>();
	
	private Map<String, CsvMapper> csvFieldHash;
	
	private Map<String, CsvMapper> mdlFieldHash;
	
	public Map<String, CsvMapper> csvFieldHash(){
		if(csvFieldHash!=null && !csvFieldHash.isEmpty()){
			return csvFieldHash;
		}
		Map<String, CsvMapper> map=new HashMap<>();
		for(CsvMapper field:fields){
			map.put(field.headerkey, field);
		}
		return map;
	}
	
	public Map<String, CsvMapper> mdlFieldHash(){
		if(mdlFieldHash!=null && !mdlFieldHash.isEmpty()){
			return mdlFieldHash;
		}
		Map<String, CsvMapper> map=new HashMap<>();
		for(CsvMapper field:fields){
			map.put(field.mappedkey, field);
		}
		return map;
	}
}

