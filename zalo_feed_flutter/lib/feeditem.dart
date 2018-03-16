// The base class for different types of feeds the List can contain
abstract class FeedItem {
  final String userName;
  final String avatarUrl;
  final String postTime;
  final int totalLike;
  final int totalComment;

  FeedItem(this.userName, this.avatarUrl, this.postTime, this.totalLike, this.totalComment);
}

class FeedLink extends FeedItem {
  final String link;

  FeedLink(this.link, String userName,
      String avatarUrl, String postTime,
      int totalLike, int totalComment)
      : super(userName, avatarUrl, postTime, totalLike, totalComment);
}

class FeedText extends FeedItem {
  final String text;

  FeedText(this.text, String userName,
      String avatarUrl, String postTime,
      int totalLike, int totalComment)
      : super(userName, avatarUrl, postTime, totalLike, totalComment);
}

class FeedPhoto extends FeedItem {
  final String imageUrl;
  final String text;

  FeedPhoto(this.imageUrl, this.text, String userName,
      String avatarUrl, String postTime,
      int totalLike, int totalComment)
      : super(userName, avatarUrl, postTime, totalLike, totalComment);
}