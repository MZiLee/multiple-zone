package multiple.zone.commons.base.core;

/**
 * @author lichao
 * @Description: 通用runtime异常，所有自定义的runtime异常需要继承此类
 */
public class MultipleRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -227961057827878851L;

    protected String code;

    public MultipleRuntimeException() {
    }

    public MultipleRuntimeException(String code) {
        super(code);
        this.setCode(code);
    }

    public MultipleRuntimeException(Throwable t) {
        super(t);
    }

    public MultipleRuntimeException(String code, Throwable t) {
        super(t);
        this.setCode(code);
    }

    public MultipleRuntimeException(String code, String msg) {
        super(msg);
        this.setCode(code);
    }

    public MultipleRuntimeException(String code, String msg, Throwable t) {
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
