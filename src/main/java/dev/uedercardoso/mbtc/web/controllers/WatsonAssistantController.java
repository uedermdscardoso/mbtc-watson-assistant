package dev.uedercardoso.mbtc.web.controllers;

import java.util.Calendar;

import javax.validation.Valid;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.watson.assistant.v2.model.MessageResponse;

import dev.uedercardoso.mbtc.domain.model.Message;
import dev.uedercardoso.mbtc.domain.model.WatsonAssistant;
import dev.uedercardoso.mbtc.web.services.WatsonAssistantService;

@RestController
@RequestMapping("/assistant")
public class WatsonAssistantController {

	@Value("${watson.assistant.apiKey}")
	public String assistantApiKey;
	
	@Value("${watson.assistant.id}")
	public String assistantId;
	
	@Value("${watson.assistant.serviceUrl}")
	public static String assistantServiceUrl;
	
	@Autowired
	private WatsonAssistantService assistantService;
	
	//Using Assistent V2 - Watson Assistant
	@PostMapping("/conversation")
	public ResponseEntity<String> getRobotText(@Valid @RequestBody Message message){
		try {
	
			JSONObject result = new JSONObject();
			String robotText = "";
			String today = DateFormatUtils.format(Calendar.getInstance().getTime(),"yyyy-MM-dd");
			
			WatsonAssistant sofia = new WatsonAssistant("Sofia", assistantApiKey, assistantId, message.getMessage(), today, assistantServiceUrl);
			
			//Using Watson Assistant
			MessageResponse response = this.assistantService.getRobotText(sofia);
			
			robotText = this.assistantService.getText(response.toString());	
			result.put("text", robotText);
			
			if(message.getExecAudio()) {
				this.assistantService.execAudio(robotText);
				
				return ResponseEntity.ok(result.toString());	
			} else
				return ResponseEntity.ok(result.toString());
			
		} catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping("/speech-to-text")
	public ResponseEntity<String> convertSpeechToText(){
		try {
			String teste = this.assistantService.convertSpeechToText();
			return ResponseEntity.ok(teste);
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	//https://cloud.ibm.com/apidocs/assistant/assistant-v2?code=java
	//https://cloud.ibm.com/apidocs/text-to-speech/text-to-speech?code=java
	//https://cloud.ibm.com/apidocs/speech-to-text/speech-to-text?code=java
	
}
