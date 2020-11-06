package dev.uedercardoso.mbtc.domain.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	private String text;
	
	@NotNull
	private Boolean execAudio;
	
	@NotNull
	private String sessionId;
	
	public Message() {
		
	}
	
	public Message(String text, Boolean execAudio) {
		this.text = text;
		this.execAudio = execAudio;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Boolean getExecAudio() {
		return execAudio;
	}
	public void setExecAudio(Boolean execAudio) {
		this.execAudio = execAudio;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
