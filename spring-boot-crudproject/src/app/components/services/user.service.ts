import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserDetailsContract } from '../contracts/UserDetailsContracts';
import { Observable } from 'rxjs';
import { PageDetailsContract } from '../contracts/PageDetailsContract';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  baseUrl = 'http://localhost:5656/user';

  constructor(private http: HttpClient) { }

  // Register User
  public registerUser(formData:FormData):Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/register`,formData);
  }

  public getImageByUserId(id:number):Observable<Blob>{
    return this.http.get<Blob>(`${this.baseUrl}/image/${id}`, { responseType: 'blob' as 'json' });
  }

  //Get All Users
  public getAllUsers(): Observable<UserDetailsContract[]> {
    return this.http.get<UserDetailsContract[]>(`${this.baseUrl}/findAllUsers`);
  }

  //Get All Users By Sort Ascending
  public getAllUsersBySortAsc(): Observable<UserDetailsContract[]> {
    return this.http.get<UserDetailsContract[]>(`${this.baseUrl}/findAllUsers/sort/asc`);
  }

  //Get All Users By Sort Descending
  public getAllUsersBySortDesc(): Observable<UserDetailsContract[]> {
    return this.http.get<UserDetailsContract[]>(`${this.baseUrl}/findAllUsers/sort/desc`);
  }

  //Get All Users By Pages
  public getAllUsersByPages1(pageNo: number, pageSize: number): Observable<PageDetailsContract> {
    return this.http.get<PageDetailsContract>(`${this.baseUrl}/findAllUsers/bypage/${pageNo}/${pageSize}`);
  }


  //Find User
  public findByUserName(userName: string): Observable<UserDetailsContract> {
    return this.http.get<UserDetailsContract>(`${this.baseUrl}/find/username/${userName}`);
  }

  // Update User
  public updateUser(User: any) {
    return this.http.put(`${this.baseUrl}/update`, User);
  }

  //Login User
  public loginUser(loginBind: any): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/login`, loginBind);
  }

  public getUserDetailsByToken(token:any):Observable<UserDetailsContract>{
    return this.http.get<UserDetailsContract>(`${this.baseUrl}/token/${token}`);
  }

  // Delete User 
  public deleteUserById(userId: number) {
    return this.http.delete(`${this.baseUrl}/delete/${userId}`);
  }

  // helper components
  public UserProfileData: UserDetailsContract = {
    userId:0,
    profile:null,
    userName: '',
    password: '',
    email: '',
    fullName: '',
    address: '',
    city: '',
    state: '',
    mobile: null
  };

  public setUserProfileData(data: UserDetailsContract) {
    this.UserProfileData = data;
  }
  // get Object
  public getUserProfileData() {
    return this.UserProfileData;
  }

  public clearUserProfileData() {
    this.UserProfileData = {
      userId:0,
      profile: null,
      userName: '',
      password: '',
      email: '',
      fullName: '',
      address: '',
      city: '',
      state: '',
      mobile: null
    };
  }

  public getFullName() {
    return this.UserProfileData.fullName;
  }
  public getUserName() {
    return this.UserProfileData.userName;
  }
  public getProfile() {
    return this.UserProfileData.profile;
  }
  public getEmail() {
    return this.UserProfileData.email;
  }
  public getMobile() {
    return this.UserProfileData.mobile;
  }
  public getAddress() {
    return this.UserProfileData.address;
  }
  public getCity() {
    return this.UserProfileData.city;
  }
  public getState() {
    return this.UserProfileData.city;
  }

}