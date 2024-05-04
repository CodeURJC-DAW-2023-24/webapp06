export interface ReportedPost {
    id: number;
    text: string;
    ownerUsername: string;
    createdAt: string;
    threadId: number;
    likes: number;
    dislikes: number;
    reports: number;
}