import { Forum } from "./forum.model";
import { Post } from "./post.model";
import { User } from "./user.model";

export interface Thread {
    id: number;
    name: string;
    forum: Forum;
    owner: User;
    posts: Post[];
    numberPosts: number;
    createdAt: Date;
}
