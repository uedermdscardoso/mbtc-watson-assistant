
class Message {

  String text;
  bool execAudio;
  String sessionId;

  Message({ this.text, this.execAudio, this.sessionId });

  factory Message.fromJson(Map<String, dynamic> json){
    return Message(
      text: json['text'],
    );
  }

}