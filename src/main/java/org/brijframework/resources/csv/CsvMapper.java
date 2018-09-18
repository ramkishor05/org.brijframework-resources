package org.brijframework.resources.csv;
public class CsvMapper {
	public String headerkey;
	public String mappedkey;
	public boolean isRequied;

	public CsvMapper() {
	}

	public CsvMapper(String headerkey, String mappedkey, boolean isRequied) {
		this(headerkey, mappedkey);
		this.isRequied=isRequied;
	}

	public CsvMapper(String headerkey, String mappedkey) {
         this.headerkey=headerkey;
         this.mappedkey=mappedkey;
	}
}