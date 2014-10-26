package ar.tunuyan.eda.executor;

public class DispatcherException extends Exception {

	private static final long serialVersionUID = 7903227771706670422L;

	public DispatcherException() {

	}

	public DispatcherException(String message) {
		super(message);

	}

	public DispatcherException(Throwable cause) {
		super(cause);

	}

	public DispatcherException(String message, Throwable cause) {
		super(message, cause);

	}

	public DispatcherException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

}
