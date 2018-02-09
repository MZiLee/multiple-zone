package multiple.zone.commons.base.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.FieldPosition;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 *
 * @author pcwang
 *
 * @version 1.0, 上午11:03:26  create $Id$
 */
public class PdfUtil {

    /**
     * 将数据转换为输入字节流
     * */
    public static InputStream convertTransData(Map<String,Object> input,String fileName,HttpServletResponse response)
            throws Exception {
        if (input == null || input.isEmpty())
            return null;
        try {
            InputStream in = PdfUtil.class.getClassLoader().getResourceAsStream("templePdf.pdf");
            ByteArrayOutputStream out = (ByteArrayOutputStream)generate(new PdfReader(in),input);
            ByteArrayInputStream ret = new ByteArrayInputStream(out.toByteArray());
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment; filename="
                    + java.net.URLEncoder.encode(fileName, "UTF-8") + ".pdf");
            OutputStream outPut = response.getOutputStream();

            out.writeTo(outPut);
            outPut.flush();
            out.close();
            outPut.close();
            return ret;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
    
    /**
     * 保存临时PDF
     * */
    public static void saveTempPdf(Map<String,Object> input,String fileName,String filePath)
    		throws Exception {
    	try {
    		InputStream in = PdfUtil.class.getClassLoader().getResourceAsStream("templePdf.pdf");
    		ByteArrayOutputStream out = (ByteArrayOutputStream)generate(new PdfReader(in),input);
    		// 输出文档路径及名称
    		File file = new File(filePath);
    		FileOutputStream  outFile = new FileOutputStream(file);
    		outFile.write(out.toByteArray());
    		out.close();
    		outFile.close();
    	} catch (Exception e) {
    		throw new Exception(e);
    	}
    }

    /**
     *  将数据，填入pdf表单域
     *
     * */
    public static OutputStream generate(PdfReader template, Map<String,Object> data) throws Exception {
        String path = PdfUtil.class.getClassLoader().getResource("").getPath()+"font/simsun.ttc";
        //BaseFont bfChinese = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        BaseFont bfChinese = BaseFont.createFont(path+",1", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfStamper stamp = new PdfStamper(template, out);
            AcroFields form = stamp.getAcroFields();
            // set the field values in the pdf form
            for (Iterator<String> it = data.keySet().iterator(); it.hasNext();) {
                String key = (String) it.next();
                if(!"qeImage".equals(key)){
                	String value = (String) data.get(key);
                	form.setFieldProperty(key, "textfont", bfChinese, null);
                	if(needSettingLeading(key)) {
                		FieldPosition pos = form.getFieldPositions(key).get(0);
                		ColumnText ct = new ColumnText(stamp.getOverContent(pos.page));
                		ct.setSimpleColumn(pos.position);
                		ct.setLeading(new Float(15.00));
                		ct.addText(Phrase.getInstance(0, value, new Font(bfChinese, 10)));
                		ct.go();
                	} else {
                		form.setField(key, value);
                	}
                }
            }
            insertImage(stamp,form,(String) data.get("qeImage"));
            stamp.setFormFlattening(true);
            stamp.close();
            template.close();
            return out;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void insertImage(PdfStamper ps, AcroFields s,String imgPath) {
	    try {
		    List<AcroFields.FieldPosition> list = s.getFieldPositions("qeImage");
		    Rectangle signRect = list.get(0).position;
		    
		    Image image = Image.getInstance(imgPath);
		    PdfContentByte under = ps.getOverContent(6);
		    float x = signRect.getLeft();
		    float y = signRect.getBottom();
		    image.setAbsolutePosition(x, y);
		    image.scaleToFit(signRect.getWidth(), signRect.getHeight());
		    under.addImage(image);
	    } catch (Exception e) {
    		e.printStackTrace();
	    }
    }
    
    private static boolean needSettingLeading(String key) {
    	return "qeCharacterTendencyTilte".equals(key) 
    			|| "qeLmsg".equals(key)
    			|| "qeBasicFeatures".equals(key)
    			|| "qeWorkAdvantage".equals(key)
    			|| "qeWorkUnfair".equals(key)
    			|| "qeSuitablePostFeature".equals(key)
    			|| "qeSecretOfSuccess".equals(key)
    			|| "developSuggestion".equals(key);
    }
    
}
