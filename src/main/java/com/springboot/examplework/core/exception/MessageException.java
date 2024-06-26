package com.springboot.examplework.core.exception;

public class MessageException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	/**程級分類*/
	private MessageLevel level;
	/**完整訊息*/
	private String message;
	
	public MessageException(MessageLevel level, String customMsg){
		this.level = level; 
		this.message = customMsg;
	}
	
	/**
	 * 取得程級分類
	 * @return the level
	 */
	public MessageLevel getLevel() {
		return level;
	}

	/**
	 * 取得完整訊息
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
}
