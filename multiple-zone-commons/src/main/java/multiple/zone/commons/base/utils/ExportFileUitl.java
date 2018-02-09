package multiple.zone.commons.base.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class ExportFileUitl {
	
	public static void exportExcel(HttpServletResponse response,InputStream in,String name){
		try {
        	byte[] buffer = new byte[in.available()];
        	in.read(buffer);
        	in.close();
        	String fileName = java.net.URLEncoder.encode(name, "UTF-8");  
        	String newName = fileName.replaceAll("\\+", "%20").replaceAll("%28", "\\(")
        			.replaceAll("%29", "\\)").replaceAll("%3B", ";").replaceAll("%40", "@")
        			.replaceAll("%23", "\\#").replaceAll("%26", "\\&").replaceAll("%2C", "\\,");
        	//fileName = fileName.replace("+", "%20"); 
        	//String newFileName = StringEscapeUtils.unescapeXml(fileName);
        	response.setCharacterEncoding("utf-8");
			response.setHeader("Content-disposition", "attachment; filename="+newName);
			OutputStream outPut = response.getOutputStream();
			outPut.write(buffer);
			outPut.flush();
			outPut.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
