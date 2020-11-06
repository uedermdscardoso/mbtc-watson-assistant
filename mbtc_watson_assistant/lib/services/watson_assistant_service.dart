
import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:mbtc_watson_assistant/domain/message.dart';

class WatsonAssistantService {

  final String URL = 'http://192.168.0.101:8080';

  Future<String> getSessionId() async {
    final response = await http.get('${ URL }/assistant/session');

    try{
      if(response.statusCode == 200)
        return response.body;
    } catch(ex){
      throw new Exception('failed to load countries');
    }
  }

  Future<Message> postMessage({ Message message }) async {
    Map<String,String> headers = {
      'Content-type' : 'application/json',
      'Accept': 'application/json',
    };

    var body = { "text": message.text, "execAudio": message.execAudio.toString(), "sessionId": message.sessionId };
    final response = await http.post('${ URL }/assistant/conversation', body: json.encode(body), headers: headers);

    try {
      if(response.statusCode == 200)
        return Message.fromJson(json.decode(response.body));
    } catch(ex) {
      throw new Exception('failed to load messages');
    }
  }
  
  /*Future<Message> getMessage() async {
    final response = await http.get(URL);

    try{
      if(response.statusCode == 200)
        return Message.fromJson(json.decode(response.body));
    } catch(ex){
      throw new Exception('failed to load countries');
    }
  }*/

}