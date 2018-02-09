package multiple.zone.commons.base.utils.email;

/**
 * Created with IntelliJ IDEA.
 * User: wanglinyong
 * Date: 2016/12/2
 * Time: 15:08
 *
 * @Copyright(c) gome inc Gome Co.,LTD
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
