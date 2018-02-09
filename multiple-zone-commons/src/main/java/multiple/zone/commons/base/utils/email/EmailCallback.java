package multiple.zone.commons.base.utils.email;

/**
 * User: lichao
 * Date: 2018年2月9日15:30:49
 *
 */
public interface EmailCallback {

    /**
     * 当发送email成功是回调函数
     */
    void whenSuccess();

    /**
     * 当发送email失败时回调函数
     */
    void whenFailed();
}
