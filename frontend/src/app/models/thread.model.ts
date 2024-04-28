import { Forum } from "./forum.model";
import { User } from "./user.model";

export interface Thread {
    id: number;
    name: string;
    forum: Forum;
    owner: User;
    posts: any[];
    numberPosts: number;
    createdAt: Date;
}
