import { Component } from '@angular/core';
import { UserService } from '../services/user.service';
import { UserDetailsContract } from '../contracts/UserDetailsContracts';
import { UserLoginBind } from '../contracts/UserLoginBind';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { state } from '@angular/animations';
import { AuthServiceComponent } from '../services/auth.service';

@Component({
  selector: 'app-login-user',
  templateUrl: './login-user.component.html',
  styleUrls: ['./login-user.component.css']
})
export class LoginUserComponent {
  hide = true;

  constructor(private fb: FormBuilder, private user: UserService, private router: Router, private auth: AuthServiceComponent) { }

  public formLogin = this.fb.group({
    userName: this.fb.control('', Validators['required']),
    password: this.fb.control('', Validators['required']),
  })
  get userName() {
    return this.formLogin.get("userName");
  }
  get password() {
    return this.formLogin.get("password");
  }

  UserDataBindResponse: UserDetailsContract = {
    userId: 0,
    profile: null,
    userName: '',
    email: '',
    fullName: '',
    address: '',
    city: '',
    state: '',
    mobile: null,
  }

  URequired: boolean = false;
  PRequired: boolean = false;
  isLoging: boolean = false;
  errMsg: string = "";
  errStatus: number | null = null;

  public LoginUser() {
    this.isLoging = true;
    if (this.formLogin.valid) {
      // console.log(this.formLogin.value);
      this.user.loginUser(this.formLogin.value).subscribe((responseToken: any) => {
        const token = responseToken.token;
        this.auth.saveToken(token);
        this.isLoging = false;
        this.formLogin.reset();
        const jwtToken = this.auth.getToken();
        this.user.getUserDetailsByToken(jwtToken).subscribe(userData => {
          this.UserDataBindResponse = userData;
          this.user.getImageByUserId(this.UserDataBindResponse.userId).subscribe(file => {
            let objectURL = URL.createObjectURL(file);
            this.UserDataBindResponse.profile = objectURL;
            // console.log(this.UserDataBindResponse.profile);
            this.user.setUserProfileData(this.UserDataBindResponse);
            // console.log(userData);
          }, error => {
            console.log(error.message);
          })
        })
        if (this.auth.isLoggedIn()) {
          this.router.navigate(['user/dashBoard']);
        }
      }, error => {
        this.errMsg = error.message;
        this.errStatus = error.status;
        this.isLoging = false;
      });
      // this.formRegister.reset();
    }
    else {
      this.isLoging = false;
      this.URequired = true;
      this.PRequired = true;
    }
  }

}
