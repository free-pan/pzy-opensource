package org.pzy.opensource.comm.exception;

/**
 * PdfOperateException: pdf操作异常
 *
 * @author pan
 * @date 5/30/20
 */
public class PdfOperateException extends AbstractBusinessException {
    public PdfOperateException(String message) {
        super(message);
    }

    public PdfOperateException(String message, Exception exp) {
        super(message, exp);
    }
}
