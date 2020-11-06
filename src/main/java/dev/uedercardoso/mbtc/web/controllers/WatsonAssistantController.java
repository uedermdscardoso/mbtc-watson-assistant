package dev.uedercardoso.mbtc.web.controllers;

import java.util.Calendar;

import javax.validation.Valid;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

	final String ASSISTANT_NAME = "Sofia";
	
	@Value("${watson.assistant.apiKey}")
	public String assistantApiKey;
	
	@Value("${watson.assistant.id}")
	public String assistantId;
	
	@Value("${watson.assistant.serviceUrl}")
	public String assistantServiceUrl;
	
	@Autowired
	private WatsonAssistantService assistantService;
	
	@GetMapping("/session")
	public ResponseEntity<String> createSession() {
		try {
			String today = DateFormatUtils.format(Calendar.getInstance().getTime(),"yyyy-MM-dd");
			WatsonAssistant sofia = new WatsonAssistant(ASSISTANT_NAME, assistantApiKey, assistantId, "", today, assistantServiceUrl);
			
			String sessionId = this.assistantService.createSession(sofia);
			
			return ResponseEntity.ok(sessionId);
			
		} catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	//Using Assistent V2 - Watson Assistant
	@PostMapping("/conversation")
	public ResponseEntity<String> getRobotText(@Valid @RequestBody Message message){
		try {
	
			JSONObject result = new JSONObject();
			String today = DateFormatUtils.format(Calendar.getInstance().getTime(),"yyyy-MM-dd");
			
			WatsonAssistant sofia = new WatsonAssistant(ASSISTANT_NAME, assistantApiKey, assistantId, message.getText(), today, assistantServiceUrl);
			MessageResponse response = this.assistantService.getRobotText(message.getSessionId(), sofia);
			
			String robotText = this.assistantService.getText(response.toString());	
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
	
	/*@PostMapping("/speech-to-text")
	public ResponseEntity<String> convertSpeechToText(){
		try {
			String teste = this.assistantService.convertSpeechToText();
			return ResponseEntity.ok(teste);
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}*/
	
}
