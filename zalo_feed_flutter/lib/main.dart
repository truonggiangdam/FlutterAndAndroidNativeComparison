import 'dart:async';

import 'package:flutter/material.dart';
import 'package:zalo_feed_flutter/feeditem.dart';
import 'package:transparent_image/transparent_image.dart';
import 'dart:io';
import 'dart:convert';

void main() => runApp(new MyApp(
));

List<FeedItem> _generateItemList() {

  final youtubeLink = 'https://www.youtube.com/watch?v=UNhBsAFSldU';
  final userName = 'Joker';
  final avatarUrl = 'https://smultron.files.wordpress.com/2008/08/heath-ledger-igen.jpg';
  final postTime = '3 hours ago';
  final totalLike = 10;
  final totalComment = 6;
  final imageUrl = 'https://vignette.wikia.nocookie.net/godfather/images/e/e5/Don_Michael_Corleone.jpg';
  final imageText = 'I asked God for a bike but I know God does not work that way. So I stole a bike and asked God for forgiveness.';
  final text = 'My advice to other disabled  people would be, concentrate on things your '
      'disability does not prevent you doing well, and do not regret the things it interferes '
      'with. Do not be disabled in spirit as well as physically \n~ Stephen Hawking ~';

  return new List<FeedItem>.generate(
      1000, (i) =>
  i % 2 == 0 ? new FeedLink(youtubeLink, userName, avatarUrl, postTime, totalLike, totalComment)
      : (i % 3 == 0 ? new FeedPhoto(imageUrl, imageText, userName, avatarUrl, postTime, totalLike, totalComment)
      : new FeedText(text, userName, avatarUrl, postTime, totalLike, totalComment)));
}

class MyApp extends StatelessWidget {

  @override
  Widget build(BuildContext context){
   return new MaterialApp(
     home: new MyFeedPage(),
   );
  }
}

class MyFeedPage extends StatefulWidget {
  @override
  _MyFeedPageState createState() => new _MyFeedPageState();

}

class _MyFeedPageState  extends State<MyFeedPage>{
  var items = _generateItemList();
  var jsonResponse = '';

  @override
  Widget build(BuildContext context) {

    var listFeed = new ListView.builder(
      // Let the ListView know how many items it needs to build
        itemCount: items.length,
        itemBuilder: (context, index) {
          final item = items[index];
          return _buildRow(item);
        }
    );

    return new Scaffold(
      appBar: new AppBar(
        title: new Text('Zalo Feed'),
      ),
      body: new Container(
        color: Colors.grey[500],
        child: listFeed,
      ),
    );
  }

  _requestJsonData(String url)  async {
    var apiUrl = 'https://www.youtube.com/oembed?url=$url&format=json';
    var httpClient = new HttpClient();
    var result;

    try {
      var request = await httpClient.getUrl(Uri.parse(apiUrl));
      var response = await request.close();
      if(response.statusCode == HttpStatus.OK) {
        result = await response.transform(UTF8.decoder).join();
      } else {
        result = '';
      }
    } catch (exception) {
      result = '';
    }

    setState(() {
      jsonResponse = result;
    });
  }

  Widget _buildMiddleItemYoutubeLink(String url) {
    if(jsonResponse ==  '') {
      _requestJsonData(url);
      return new Center();
    } else {
      var data = JSON.decode(jsonResponse);
      var thumbnailUrl = data['thumbnail_url'];
      var title = data['title'];
      return new Container(
        margin: const EdgeInsets.only(left: 0.00, top: 16.00, right: 0.00, bottom: 8.00),
        child: new Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            new Container(
              margin: const EdgeInsets.only(bottom: 8.00),
              child: new FadeInImage.memoryNetwork(
                  placeholder: kTransparentImage,
                  image: thumbnailUrl,
                  fit: BoxFit.fill,
                  imageScale: 0.2,
              ),
            ),
            new Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                new Expanded(
                    child: new Text(
                      title,
                      style: new TextStyle(
                        fontSize: 18.00,
                        fontWeight: FontWeight.bold
                      ),
                    )
                ),
                new Container(
                  margin: const EdgeInsets.only(left: 48.00),
                  decoration: new BoxDecoration(
                    border: new Border.all(
                      color: Colors.grey[500]
                    ),
                    borderRadius: const BorderRadius.all(const Radius.circular(18.0)),
                  ),
                  padding: const EdgeInsets.only(left: 14.00, top: 6.00, right: 14.00, bottom: 6.00),
                  child: new Text(
                      'SHARE',
                      style: new TextStyle(
                        fontSize: 16.00,
                        color: Colors.grey[500],
                        fontWeight: FontWeight.bold
                      ),
                  ),
                )
              ],
            ),
            new Container(
              child: new Text(
                'www.youtube.com',
                style: new TextStyle(
                  color: Colors.grey[500]
                ),
              ),
            )
          ],
        ),
      );
    }
  }

  Widget _buildRow(FeedItem item) {
    Widget top = _buildTop(item.avatarUrl, item.userName, item.postTime);
    Widget bottom = _buildBottom(item.totalLike, item.totalComment);
    Widget middle;

    if(item is FeedLink) {
      middle =  _buildMiddleItemYoutubeLink(item.link);
    } else if(item is FeedPhoto) {
      middle=  new Container(
          padding: const EdgeInsets.only(top: 16.00),
          child: new Column(
            children: <Widget>[
              new Container(
                padding: const EdgeInsets.only(bottom: 8.00),
                child: new Text(
                  item.text,
                  style: new TextStyle(color: Colors.black),
                ),
              ),
              new Stack(
                children: <Widget>[
                  new Center(
                    child: new FadeInImage.memoryNetwork(
                        placeholder: kTransparentImage,
                        image: item.imageUrl
                    ),
                  )
                ],
              ),
            ],
          )
      );
    } else { // Feed Text
      middle =  new Container(
        padding: const EdgeInsets.only(top: 16.00),
        child: new Text(
          (item as FeedText).text,
          style: new TextStyle(
            color: Colors.black,
          ),
        ),
      );
    }

    return _buildItemContainer(top, middle, bottom);
  }

  Widget _buildItemContainer(Widget top, Widget middle, Widget bottom) {
    return new Container(
      color: Colors.white,
      padding: const EdgeInsets.all(16.00),
      margin: const EdgeInsets.only(left: 0.0, top: 8.0, right: 0.0, bottom: 8.0),
      child: new Column(
        children: <Widget>[
          top,
          middle,
          bottom
        ],
      ),
    );
  }

  Widget _buildTop(String avatarUrl, String userName, String postTime) {
    return new Container(
      child: new Row(
        mainAxisAlignment: MainAxisAlignment.start,
        children: <Widget>[
          new Container(
            width: 70.0,
            height: 70.0,
            decoration: new BoxDecoration(
              image: new DecorationImage(
                  image: new NetworkImage(avatarUrl),
                  fit: BoxFit.cover
              ),
              borderRadius: new BorderRadius.all(new Radius.circular(50.0))
            ),
          ),
          new Container(
              margin: const EdgeInsets.only(left: 16.00),

              child: new Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  new Text(
                    userName,
                    style: new TextStyle(
                        fontSize: 18.0,
                        fontWeight: FontWeight.bold
                    ),
                  ),
                  new Text(
                    postTime,
                    style: new TextStyle(
                        color: Colors.grey[500]
                    ),
                  )
                ],
              )
          )
        ],
      ),
    );
  }

  Widget _buildBottom(int totalLike, int totalComment) {

    Widget bottomContainer =  new Container(
        child: new Row(
          children: <Widget>[
            new Expanded(
                child: new Row(
                  children: <Widget>[
                    new Container(
                      child: new Row(
                        children: <Widget>[
                          new Icon(Icons.favorite_border),
                          new Container(
                            margin: const EdgeInsets.only(left: 8.00),
                            child: new Text('$totalLike'),
                          )
                        ],
                      ),
                    ),
                    new Container(
                      margin: const EdgeInsets.only(left: 48.00),
                      child: new Row(
                        children: <Widget>[
                          new Icon(Icons.chat_bubble_outline),
                          new Container(
                            margin: const EdgeInsets.only(left: 8.00),
                            child: new Text('$totalComment'),
                          )
                        ],
                      ),
                    )
                  ],
                )
            ),
            new Icon(Icons.more_horiz)
          ],
        )
    );


    return new Column(
      children: <Widget>[
        new Divider(),
        bottomContainer
      ],
    );
  }
}

