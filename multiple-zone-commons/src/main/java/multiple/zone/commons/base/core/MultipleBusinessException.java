package multiple.zone.commons.base.core;

/**
* @Description: 通用checked异常，所有自定义的checked异常需要继承此类
* @author lichao 
* @version V1.0   
*/
public class MultipleBusinessException extends Exception {
	
	
    private static final long serialVersionUID = 1L;

    protected String code;

    public MultipleBusinessException() {
    }

    public MultipleBusinessException(String code) {
        super(code);
        this.setCode(code);
    }

    public MultipleBusinessException(Throwable t) {
        super(t);
    }

    public MultipleBusinessException(String code, Throwable t) {
        super(t);
        this.setCode(code);
    }

    public MultipleBusinessException(String code, String msg) {
        super(msg);
        this.setCode(code);
    }

    public MultipleBusinessException(String code, String msg, Throwable t) {
        super(msg, t);
        this.setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
