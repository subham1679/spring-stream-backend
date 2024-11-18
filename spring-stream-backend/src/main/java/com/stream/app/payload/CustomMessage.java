package com.stream.app.payload;

public class CustomMessage {
	
	private String message;
	private boolean success = false;
	
	// Constructor for required fields
	public CustomMessage(String message, boolean success) {
		this.message = message;
		this.success = success;
	}
	
	public CustomMessage() {}

	// Getters and Setters
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	// Static inner Builder class
	public static class CustomMessageBuilder {
		private String message;
		private boolean success;

		// Builder methods for setting properties
		public CustomMessageBuilder setMessage(String message) {
			this.message = message;
			return this;
		}

		public CustomMessageBuilder setSuccess(boolean success) {
			this.success = success;
			return this;
		}

		// Build method to create the CustomMessage object
		public CustomMessage build() {
			return new CustomMessage(this.message, this.success);
		}
	}

	// Static method to get the builder
	public static CustomMessageBuilder builder() {
		return new CustomMessageBuilder();
	}
}
