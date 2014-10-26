package ar.tunuyan.eda.eventbus;

public class ReplyException extends RuntimeException {

	private static final long serialVersionUID = -6971225160312958610L;

	private String code;

	public ReplyException() {
	}

	public ReplyException(String message) {
		super(message);
	}

	public ReplyException(String code, String message) {
		super(message);
		this.code = code;
	}

	public ReplyException(Throwable cause) {
		super(cause);
	}

	public ReplyException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public ReplyException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReplyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public String getCode() {
		return code;
	}

}
