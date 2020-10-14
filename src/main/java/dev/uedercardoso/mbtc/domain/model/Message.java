package dev.uedercardoso.mbtc.domain.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	private String message;
	
	@NotNull
	private Boolean execAudio;
	
	public Message(String message, Boolean execAudio) {
		this.message = message;
		this.execAudio = execAudio;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Boolean getExecAudio() {
		return execAudio;
	}
	public void setExecAudio(Boolean execAudio) {
		this.execAudio = execAudio;
	}
}
