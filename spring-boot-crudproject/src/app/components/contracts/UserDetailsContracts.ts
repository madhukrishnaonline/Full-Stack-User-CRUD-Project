export interface UserDetailsContract {
    userId:number;
    profile?: any;
    contentType?: string;
    userName: string;
    password?: string;
    email: string;
    fullName: string;
    address: string;
    city: string;
    state: string
    mobile: number | null;
    status?: boolean;
}