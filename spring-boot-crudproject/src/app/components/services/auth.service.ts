import { Injectable } from '@angular/core';

@Injectable({
    providedIn: "root"
})
export class AuthServiceComponent {
    private jwtToken = "jwtToken";

    //save token in LocalStorage
    saveToken(token: string) {
        localStorage.setItem(this.jwtToken, token);
    }

    //get token
    getToken(): string | null {
        return localStorage.getItem(this.jwtToken);
    }

    isLoggedIn(): boolean {
        const token = this.getToken();
        return token !== null && !this.isTokenExpired();
    }

    //decode and check the expiry time of token
    isTokenExpired(): boolean {
        const token = this.getToken();
        if (!token) return true;
        const payload = JSON.parse(atob(token.split('.')[1]));
        const expiry = payload.exp * 1000;
        return Date.now() > expiry;
    }

    //remove token when logged out
    logout(): void {
        localStorage.removeItem(this.jwtToken);
    }
}