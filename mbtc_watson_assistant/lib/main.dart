
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:mbtc_watson_assistant/screens/chat_screen.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp])
      .then((_) {
    runApp(App());
  });
}

class App extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Sofia Assistant',
      theme: ThemeData(
        primaryColor: Colors.white,
      ),
      home: ChatScreen(),
    );
  }
}
