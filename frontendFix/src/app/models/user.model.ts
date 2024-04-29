export interface User {
    id?: number;
    username: string;
    email: string,
    isActive: boolean,
    createdAt: string,
    roles: string[];
}