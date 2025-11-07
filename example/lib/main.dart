import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter_new_pipe_extractor/flutter_new_pipe_extractor.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  late Future<List<SearchResultItem>> searchResultsFuture;

  @override
  void initState() {
    super.initState();
    searchResultsFuture = initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<List<SearchResultItem>> initPlatformState() async {
    await NewPipeExtractor.init();

    final results = await NewPipeExtractor.search('How Long - Charlie Puth');
    // final results = await NewPipeExtractor.getVideoInfo('nfs8NYg7yQM');

    print("Video title: $results");

    return results;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: FutureBuilder(
          future: searchResultsFuture,
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(child: CircularProgressIndicator());
            } else if (snapshot.hasError) {
              debugPrint(snapshot.error?.toString());
              debugPrintStack(stackTrace: snapshot.stackTrace);
              return Center(child: Text('Error: ${snapshot.error}'));
            } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
              return const Center(child: Text('No results found.'));
            }

            final results = snapshot.data!;

            return ListView.builder(
              itemCount: results.length,
              itemBuilder: (context, index) {
                final result = results[index];
                return ListTile(
                  leading: result.thumbnails.isNotEmpty
                      ? Image.network(result.thumbnails[0].url)
                      : null,
                  title: Text(result.name),
                  subtitle: Text(result.url),
                );
              },
            );
          },
        ),
      ),
    );
  }
}
