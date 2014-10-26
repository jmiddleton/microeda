package ar.tunuyan.eda.eventbus;

import java.io.Serializable;
import java.util.UUID;

import ar.tunuyan.eda.eventbus.handler.EventHandler;

/**
 * This class provides the input (body + header) to and/or the output from the
 * {@link EventHandler}.
 * 
 * @author jmiddleton
 *
 * @param <T>
 */
public class Event<T> implements Serializable {
	private static final long serialVersionUID = -7373958304441407225L;

	private UUID messageId;
	private T body;
	private Object reply;
	private String replyAddress;
	private String serviceName;

	public Event(String serviceName, T body) {
		this.body = body;
		this.serviceName = serviceName;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public Headers headers() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * The body of the message
	 */
	public T body() {
		return this.body;
	}

	/**
	 * Reply to this message. If the message was sent specifying a reply
	 * handler, that handler will be called when it has received a reply. If the
	 * message wasn't sent specifying a receipt handler this method does
	 * nothing.
	 */
	public void reply(Object message) {
		this.reply = message;
	}

	/**
	 * Signal that processing of this message failed. If the message was sent
	 * specifying a {@link EventCallback}, the callback will be called with a
	 * failure corresponding to the failure code and message specified here.
	 * 
	 * @param failureCode
	 *            A failure code to pass back to the sender
	 * @param message
	 *            A message to pass back to the sender
	 */
	public void fail(String code, String message) {
		this.reply = new ReplyException(message);
	}

	public void fail(String code, Throwable cause) {
		this.reply = new ReplyException(cause.getMessage(), cause);
	}

	public Object getReply() {
		return this.reply;
	}

	public String replyAddress() {
		return this.replyAddress;
	}

	public UUID getMessageId() {
		return messageId;
	}

	public void setMessageId(UUID messageId) {
		this.messageId = messageId;
	}

	public boolean isFailed() {
		return this.reply != null && this.reply instanceof ReplyException;
	}

	public void setReplyAddress(String replyAddress) {
		this.replyAddress = replyAddress;
	}
}
