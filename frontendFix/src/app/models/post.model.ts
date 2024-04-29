import { User } from "./user.model";

export interface Post {
    id: number;
    text: string;
    owner: User;
    createdAt: string;
    userLikes: User[];
    userDislikes: User[];
    reports: number;
    likes: number;
    dislikes: number;
}