export interface ReportedPost {
    id: number;
    text: string;
    ownerUsername: string;
    createdAt: string;
    threadName: string;
    likes: number;
    dislikes: number;
    reports: number;
}