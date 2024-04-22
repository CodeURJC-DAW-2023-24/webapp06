export class Post {
  id: number;
  text: string;
  author: string;
  isAuthor: boolean = false;
  elapsedTime: String;
  threadName: String;
  hasImage: boolean = false;
  likes: number;
  isLiked: boolean = false;
  dislikes: number;
  isDisliked: boolean = false;
  reports: number;

  constructor(
    id: number,
    text: string,
    author: string,
    createdAt: Date,
    threadName: String,
    likes: number,
    dislikes: number,
    reports: number
  ) {
    this.id = id;
    this.text = text;
    this.author = author;

    const postTime: Date = new Date(createdAt);
    const currentTime: Date = new Date();
    const seconds: number = Math.floor(
      (currentTime.getTime() - postTime.getTime()) / 1000
    );
    const days: number = Math.floor(seconds / (60 * 60 * 24));
    const hours: number = Math.floor(seconds / (60 * 60));
    const minutes: number = Math.floor(seconds / 60);

    if (days > 0) {
      this.elapsedTime = days + ' days ago';
    } else if (hours > 0) {
      this.elapsedTime = hours + ' hours ago';
    } else if (minutes > 0) {
      this.elapsedTime = minutes + ' minutes ago';
    } else {
      this.elapsedTime = seconds + ' seconds ago';
    }

    this.threadName = threadName;
    this.likes = likes;
    this.dislikes = dislikes;
    this.reports = reports;
  }

  setImage(image: Uint8Array) {
    this.hasImage = image.length > 0;
  } 
}
