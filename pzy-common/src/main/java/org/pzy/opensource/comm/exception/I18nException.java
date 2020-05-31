package org.pzy.opensource.comm.exception;

/**
 * 对于异常信息需要进行国际化展示的都需要使用I18nException将原始异常进行一次包装
 *
 * @author pan
 * @date 5/31/20
 */
public class I18nException extends AbstractBusinessException {

    /**
     * i18n资源编码
     */
    private String i18nCode;

    /**
     * 构造函数
     *
     * @param i18nCode       国际化资源编码
     * @param defaultMessage 默认异常提示信息
     */
    public I18nException(String i18nCode, String defaultMessage) {
        super(defaultMessage);
        this.i18nCode = i18nCode;
    }

    /**
     * 构造函数
     *
     * @param i18nCode       国际化资源编码
     * @param defaultMessage 默认异常提示信息
     * @param exp            被包装的异常
     */
    public I18nException(String i18nCode, String defaultMessage, Exception exp) {
        super(defaultMessage, exp);
        this.i18nCode = i18nCode;
    }

    public String getI18nCode() {
        return i18nCode;
    }

    public void setI18nCode(String i18nCode) {
        this.i18nCode = i18nCode;
    }
}
