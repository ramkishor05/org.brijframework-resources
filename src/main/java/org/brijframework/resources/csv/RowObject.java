package org.brijframework.resources.csv;

import java.util.Map;

public class RowObject {
	public boolean isError;
	public boolean isIgnor;
	public String message;
	public Map<String, Object> csvObject;
	public Map<String, Object> rowObject;
	
	@Override
	public String toString() {
		StringBuffer buffer=new StringBuffer();
		buffer.append("\n\t{");
		buffer.append("\n\t\tisError	="+isError);
		buffer.append("\n\t\tisIgnor	="+isIgnor);
		buffer.append("\n\t\tmessage	="+message);
		buffer.append("\n\t\tcsvObject  ="+csvObject);
		buffer.append("\n\t\trowObject  ="+rowObject);
		buffer.append("\n\t\n\t}\n\t");
		return buffer.toString();
	}
}
