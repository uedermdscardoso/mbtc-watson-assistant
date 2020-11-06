
import 'dart:collection';

import 'package:flutter/material.dart';
import 'package:mbtc_watson_assistant/domain/message.dart';
import 'package:mbtc_watson_assistant/services/watson_assistant_service.dart';

class ChatScreen extends StatefulWidget {
  @override
  _ChatScreenState createState() => _ChatScreenState();
}

class _ChatScreenState extends State<ChatScreen> {

  TextEditingController _messageController;
  WatsonAssistantService _watsonAssistantService;
  List<Map<String, String>> _conversations;
  String sessionId;

  @override
  void initState() {
    _watsonAssistantService = WatsonAssistantService();
    _messageController = TextEditingController();
    _conversations = List<Map<String, String>>();
    loadSession();
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }

  void loadSession() async {
    sessionId = await _watsonAssistantService.getSessionId();
  }

  @override
  Widget build(BuildContext context) {

    _conversations.clear();

    return Material(
      child: Container(
        color: Colors.white,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            Container(
              color: Colors.purple,
              height: 100,
              child: Padding(
                padding: const EdgeInsets.only(top: 20, left: 12),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: [
                    ClipRRect(
                      borderRadius: BorderRadius.circular(100),
                      child: Image.asset('assets/images/female.jpg', scale: 4.5)
                    ),
                    SizedBox(width: 12),
                    Text('Sofia Montenegro', style: TextStyle(color: Colors.white, decoration: TextDecoration.none, fontSize: 18, fontFamily: 'Arial', fontWeight: FontWeight.bold)),
                  ],
                ),
              ),
            ),
            Expanded(
              child: NotificationListener<OverscrollIndicatorNotification>(
                onNotification: (overscroll) { overscroll.disallowGlow(); },
                child: ListView.builder(
                  reverse: true,
                  padding: EdgeInsets.only(left: 12, right: 12, bottom: 20, top: 12),
                  physics: const PageScrollPhysics(),
                  scrollDirection: Axis.vertical,
                  itemCount: _conversations.length,
                  itemBuilder: (context, position) {
                    if(_conversations.length > 0){
                      var chat = _conversations.reversed;

                      if(chat.elementAt(position).containsKey('user'))
                        return showMessageByFrom(position: position, message: chat.elementAt(position)['user']);
                      else
                        return showMessageByTo(position: position, message: chat.elementAt(position)['assistant']);
                    }
                  },
                ),
              ),
            ),
            Container(
              color: Colors.white,
              height: 60,
              child: Row(
                children: [
                  Expanded(
                    child: Material(
                      child: TextField(
                        onEditingComplete: () async => await sendMessage(message: _messageController.text),
                        autofocus: false,
                        controller: _messageController,
                        maxLines: 1,
                        textAlign: TextAlign.start,
                        style: TextStyle(color: Colors.grey),
                        decoration: InputDecoration(
                          filled: true,
                          fillColor: Colors.white,
                          enabledBorder: UnderlineInputBorder(
                              borderSide: BorderSide(color: Colors.grey[200])
                          ),
                          focusedBorder: UnderlineInputBorder(
                            borderSide: BorderSide(color: Colors.grey[200]),
                          ),
                          contentPadding: EdgeInsets.only(left: 20, right: 20),
                          hintText: 'Encontre o caixa mais próximo...',
                          hintStyle: TextStyle(color: Colors.grey),
                        ),
                      ),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(right: 12),
                    child: GestureDetector(
                      onTap: () async => await sendMessage(message: _messageController.text),
                      child: Container(
                        width: 60,
                        height: 80,
                        decoration: BoxDecoration(
                          color: Color.fromRGBO(255, 187, 255, 1),
                          borderRadius: BorderRadius.circular(100),
                        ),
                        child: Icon(Icons.play_arrow, color: Colors.purple, size: 45)
                      ),
                    ),
                  ),
                ],
              ),
            ),
            SizedBox(height: MediaQuery.of(context).viewInsets.bottom * 0.99),
          ],
        ),
      ),
    );
  }

  Widget showMessageByFrom({ String message, int position }){
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: Row(
        children: [
          /*Container(
            width: 30,
            height: 30,
            decoration: BoxDecoration(
              color: Colors.grey,
              borderRadius: BorderRadius.circular(100),
            ),
            child: Icon(Icons.account_circle, size: 30, color: Colors.grey),
          ),*/
          ClipRRect(
              borderRadius: BorderRadius.circular(100),
              child: Image.asset('assets/images/male.jpg', scale: 12)
          ),
          SizedBox(width: 5),
          Flexible(
            child: Container(
              decoration: BoxDecoration(
                color: Color.fromRGBO(224, 224, 224, 1), //Colors.black12,
                borderRadius: BorderRadius.circular(12),
              ),
              child: Padding(
                padding: const EdgeInsets.only(top: 12, bottom: 12, left: 22, right: 22),
                child: Text(
                  message,
                  style: TextStyle(fontSize: 15, color: Colors.black, decoration: TextDecoration.none, fontWeight: FontWeight.normal, fontFamily: 'Arial'),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget showMessageByTo({ String message, int position }){
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.end,
        children: [
          Flexible(
            child: Container(
              decoration: BoxDecoration(
                color: Color.fromRGBO(240, 240, 240, 1), //Colors.lightGreen[100],
                borderRadius: BorderRadius.circular(12),
              ),
              child: Padding(
                padding: const EdgeInsets.only(top: 12, bottom: 12, left: 22, right: 22),
                child: Text(
                  message,
                  style: TextStyle(fontSize: 15, color: Colors.black, decoration: TextDecoration.none, fontWeight: FontWeight.normal, fontFamily: 'Arial'),
                ),
              ),
            ),
          ),
          SizedBox(width: 5),
          /*Container(
            width: 30,
            height: 30,
            decoration: BoxDecoration(
              color: Colors.grey,
              borderRadius: BorderRadius.circular(100),
            ),
            child: Icon(Icons.account_circle, size: 30, color: Colors.grey),
          ),*/
          ClipRRect(
            borderRadius: BorderRadius.circular(100),
            child: Image.asset('assets/images/female.jpg', scale: 5.8)
          ),
        ],
      ),
    );
  }

  Future<void> sendMessage({ String message }) async {
    _messageController.clear();

    setState(() => _conversations.add({ 'user': message }) );

    await _watsonAssistantService.postMessage(message: Message(text: message, execAudio: false, sessionId: sessionId))
        .then((msg) {
        if(msg != null)
          setState( () => _conversations.add({ 'assistant': msg.text}) );
        else
          loadSession();
    });
  }

}
