package multiple.zone.commons.base.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * 填充excel，导出
 * 需要制作excel模板
 * @date 2018年2月9日14:04:36
 * @author lichao36
 *
 */
public class FreemarkExportExcel {

	/**
	 * 
	 * @param request
	 * @param response
	 * @param templateName 模板名称
	 * @param dataMap 数据源
	 * @param buildFile	生成文件临时路劲
	 * @param newName	导出文件名称
	 */
	public static void export(HttpServletRequest request,HttpServletResponse response,String templateName, Map<String, Object> dataMap,  
            String buildFile, String newName) {  
        try {  
//        	ServletContext application = request.getSession().getServletContext(); 
//	    	String exportExcel = application.getRealPath("template")+ UUID.randomUUID() + ".xls";
            
        	Configuration configuration = new Configuration(Configuration.VERSION_2_3_20);  
            configuration.setDefaultEncoding("utf-8");  
            configuration.setClassForTemplateLoading(FreemarkExportExcel.class, "/");
            Template template = configuration.getTemplate(templateName);  
            File outFile = new File(buildFile);  
            Writer writer = null;  
            template.setEncoding("utf-8");
            // 此处为输 出文档编码  
            writer = new OutputStreamWriter(new FileOutputStream(outFile), "utf-8");
            template.process(dataMap, writer);  
            writer.flush();  
            writer.close();  
            // 设置response的编码方式  
            response.setContentType("application/msexcel");  
            // 设置附加文件名  
            response.setHeader("Content-Disposition", "attachment;filename="  
                     +URLEncoder.encode(newName,"utf-8")+".xls");  
            // 读出文件到i/o流  
            FileInputStream fis = new FileInputStream(outFile);  
            BufferedInputStream buff = new BufferedInputStream(fis);  
  
            byte[] b = new byte[1024];// 相当于我们的缓存  
  
            long k = 0;// 该值用于计算当前实际下载了多少字节  
            // 从response对象中得到输出流,准备下载  
            OutputStream myout = response.getOutputStream();  
            // 开始循环下载  
            while (k < outFile.length()) {  
                int j = buff.read(b, 0, 1024);  
                k += j;  
                // 将b中的数据写到客户端的内存  
                myout.write(b, 0, j);  
            }  
            // 将写入到客户端的内存的数据,刷新到磁盘  
            myout.flush();  
            myout.close();
            buff.close();
            cleanFile(buildFile);
        } catch (Exception e) {  
        	cleanFile(buildFile);
            e.printStackTrace();  
        }  
    }
	
	public static void createFile(String templatePath, Map<String, Object> dataMap,  
			String buildFile) {  
		try {  
			Configuration configuration = new Configuration(Configuration.VERSION_2_3_20);  
			configuration.setDefaultEncoding("utf-8");  
			configuration.setClassForTemplateLoading(FreemarkExportExcel.class, "/");
			Template template = configuration.getTemplate(templatePath);  
			File outFile = new File(buildFile);  
			Writer writer = null;  
			template.setEncoding("utf-8");
			// 此处为输 出文档编码  
			writer = new OutputStreamWriter(new FileOutputStream(outFile), "utf-8");
			template.process(dataMap, writer);  
			writer.flush();  
			writer.close();  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	} 
	
	
	
	private static void cleanFile(String path){
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
	}
}
