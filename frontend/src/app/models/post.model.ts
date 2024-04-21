export class Post {
    id: number;
    text: string;
    author: string;
    createdAt: Date;

    constructor(id: number, text: string, author: string, createdAt: Date) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.createdAt = createdAt;
    }
}