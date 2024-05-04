export interface ReportedPost {
    id: number;
    text: string;
    ownerId: number;
    ownerUsername: string;
    createdAt: string;
    threadId: number;
    likes: number;
    dislikes: number;
    reports: number;
}