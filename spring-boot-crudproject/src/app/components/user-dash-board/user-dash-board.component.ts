import { Component, OnInit } from '@angular/core';
import { UserDetailsContract } from '../contracts/UserDetailsContracts';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';
import { AuthServiceComponent } from '../services/auth.service';

@Component({
  selector: 'app-user-dash-board',
  templateUrl: './user-dash-board.component.html',
  styleUrls: ['./user-dash-board.component.css']
})
export class UserDashBoardComponent implements OnInit {
  constructor(private user: UserService, private router: Router, private auth: AuthServiceComponent) { }

  public userProfile: UserDetailsContract = {
    userId: 0,
    profile: null,
    userName: '',
    password: '',
    email: '',
    fullName: '',
    address: '',
    city: '',
    state: '',
    mobile: null
  }
  fetchingDetails: boolean = true;
  ngOnInit() {
    this.fetchingDetails = true;
    this.userProfile = this.user.getUserProfileData();
    this.getUserByToken();
    this.fetchingDetails = false;
    // console.log(this.userProfile);
  }

  getUserByToken() {
    this.user.getUserDetailsByToken(this.auth.getToken()).subscribe(data => {
      this.userProfile = data;
    })
  }
  isEditable: boolean = false;
  toggleEdit() {
    if (this.isEditable) {
      this.Update();
    }
    this.isEditable = !this.isEditable;
  }

  ChangeUserName() {

  }
  response: any = '';
  errorMsg: string = '';
  isUpdating: boolean = false;
  Update() {
    this.isUpdating = true;
    let updatedProfile = { ...this.userProfile };
    delete updatedProfile.profile;
    this.user.updateUser(updatedProfile).subscribe(data => {
      this.response = data;
      this.user.findByUserName(this.userProfile.userName).subscribe(data => {
        this.userProfile = data;
        this.user.setUserProfileData(this.userProfile);
      })
      this.isUpdating = false;
    },
      error => {
        this.errorMsg = error.message;
        this.isUpdating = false;
      })
  }

  Logout() {
    this.auth.logout();
    this.user.clearUserProfileData();
    this.router.navigate([`login/user`]);
  }
}
