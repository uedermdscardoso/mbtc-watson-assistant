package dev.uedercardoso.mbtc.domain.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WatsonAssistant implements Serializable {
	
	private static final long serialVersionUID = -817163757205052825L;

	private String name; 
	private String apiKey;
	private String assistantId;
	private String message;
	private String date;
	private String serviceUrl;
	
	public WatsonAssistant() {
		
	}

	public WatsonAssistant(String name, String apiKey, String assistantId, String message, String date, String serviceUrl) {
		this.name = name;
		this.apiKey = apiKey;
		this.assistantId = assistantId;
		this.message = message;
		this.date = date;
		this.serviceUrl = serviceUrl;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getAssistantId() {
		return assistantId;
	}
	public void setAssistantId(String assistantId) {
		this.assistantId = assistantId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getServiceUrl() {
		return serviceUrl;
	}
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	
	
}
