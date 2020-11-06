package dev.uedercardoso.mbtc.web.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.SessionResponse;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.AddAudioOptions;
import com.ibm.watson.speech_to_text.v1.model.CreateLanguageModelOptions;
import com.ibm.watson.speech_to_text.v1.model.LanguageModel;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.text_to_speech.v1.util.WaveUtils;

import dev.uedercardoso.mbtc.domain.model.WatsonAssistant;

@Service
public class WatsonAssistantService {

	@Value("${watson.TextToSpeech.apiKey}")
	public String textToSpeechApiKey;
	
	@Value("${watson.TextToSpeech.serviceUrl}")
	public String textToSpeechServiceUrl;
	
	@Value("${watson.SpeechToText.apiKey}")
	public String speechToTextApiKey;
	
	@Value("${watson.SpeechToText.serviceUrl}")
	public String speechToTextServiceUrl;
	

	public Assistant authenticate(WatsonAssistant virtualAssistant) {
		IamAuthenticator authenticator = new IamAuthenticator(virtualAssistant.getApiKey());
		Assistant service = new Assistant(virtualAssistant.getDate(), authenticator);
		service.setServiceUrl(virtualAssistant.getServiceUrl());
		return service;
	}
	
	public String createSession(WatsonAssistant virtualAssistant) {
		Assistant service = authenticate(virtualAssistant);
		
		CreateSessionOptions sessionOptions = new CreateSessionOptions.Builder(virtualAssistant.getAssistantId()).build();
		SessionResponse sessionResponse = service.createSession(sessionOptions).execute().getResult();
		
		return sessionResponse.getSessionId();
		
	}
	
	//Using Assistant v2
	public MessageResponse getRobotText(String sessionId, WatsonAssistant virtualAssistant) {
		
		Assistant service = authenticate(virtualAssistant);
		
		MessageInput input = new MessageInput.Builder()
		  .messageType("text")
		  .text(virtualAssistant.getMessage())
		  .build();

		MessageOptions options = new MessageOptions.Builder(virtualAssistant.getAssistantId(), sessionId)
		  .input(input)
		  .build();

		MessageResponse response = service.message(options).execute().getResult();
		
		return response;
	}
	
	public void execAudio(String message) {
		
		IamAuthenticator authenticator = new IamAuthenticator(textToSpeechApiKey);
		TextToSpeech textToSpeech = new TextToSpeech(authenticator);
		textToSpeech.setServiceUrl(textToSpeechServiceUrl);

		try {
		  SynthesizeOptions synthesizeOptions =
		    new SynthesizeOptions.Builder()
		      .text(message)
		      .accept("audio/wav")
		      .voice("pt-BR_IsabelaVoice")
		      .build();

		  InputStream inputStream = textToSpeech.synthesize(synthesizeOptions).execute().getResult();
		  InputStream in = WaveUtils.reWriteWaveHeader(inputStream);

//		  File audio = File.createTempFile("allison", ".wav");
//		  audio.deleteOnExit();
//		  FileOutputStream out = new FileOutputStream(audio);
//		  IOUtils.copy(in, out);

		  //Execute audio
		  AudioInputStream stream;
		  AudioFormat format;
		  DataLine.Info info;
		  Clip clip;

		  stream = AudioSystem.getAudioInputStream(in); //Or File ou InputStream
		  format = stream.getFormat();
		  info = new DataLine.Info(Clip.class, format);
		  clip = (Clip) AudioSystem.getLine(info);
		  clip.open(stream);
		  clip.start();
		  
		  in.close();
		  inputStream.close();
		 
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
		  e.printStackTrace();
		}
	}
	
	public String getText(String response) throws Exception {
		
		JSONObject output = new JSONObject(response);
		
		if(output.has("output")) {
			output = output.getJSONObject("output");
			if(output.has("generic")) {
				JSONArray generic = output.getJSONArray("generic"); 
				
				if(!generic.isEmpty()) {
					JSONObject text = generic.getJSONObject(0);
					if(text.has("text")) {
						
						return text.getString("text");
						
					} else if(text.has("suggestions")) {
						
						JSONArray vector = text.getJSONArray("suggestions");
						
						if(vector.length() > 0) {
							JSONObject obj = vector.getJSONObject(0);
							
							
							if(obj.has("output")) {
								output = obj.getJSONObject("output");
								
								if(output.has("generic")) {
									generic = output.getJSONArray("generic");
									if(!generic.isEmpty()) {
										text = generic.getJSONObject(0);
										return text.getString("text");			
									}
								}
							}
						}
					}
				}
			}
		}
		
		throw new Exception("Não foi possível retornar o texto");
		
	}
	
	
	public String convertSpeechToText() {
		
		IamAuthenticator authenticator = new IamAuthenticator(speechToTextApiKey);
		SpeechToText speechToText = new SpeechToText(authenticator);
		speechToText.setServiceUrl(speechToTextServiceUrl);

		CreateLanguageModelOptions createLanguageModelOptions =
		  new CreateLanguageModelOptions.Builder()
		    .name("First example language model")
		    .baseModelName("en-US_BroadbandModel")
		    .description("First custom language model example")
		    .build();

		LanguageModel languageModel =
		  speechToText.createLanguageModel(createLanguageModelOptions).execute().getResult();
		
		String customizationId = languageModel.getCustomizationId();
		
		try {
		  AddAudioOptions addAudioOptions = new AddAudioOptions.Builder()
		    .customizationId(customizationId)
		    .contentType("audio/wav")
		    .audioResource(new File("C:/Users/User/Desktop/hello_world.wav"))
		    .audioName("hello_world")
		    .build();

		  return speechToText.addAudio(addAudioOptions).execute().getResult().toString();
		  
		  // Poll for audio status.
		} catch (FileNotFoundException e) {
		  e.printStackTrace();
		}
		
		return "";
		
	}
	
}
