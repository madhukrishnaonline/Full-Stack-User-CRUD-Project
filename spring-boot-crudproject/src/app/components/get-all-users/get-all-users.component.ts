import { Component } from '@angular/core';
import { UserDetailsContract } from '../contracts/UserDetailsContracts';
import { FormBuilder, Validators } from '@angular/forms';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-get-all-users',
  templateUrl: './get-all-users.component.html',
  styleUrls: ['./get-all-users.component.css']
})
export class GetAllUsersComponent {
  public getAllUsers: UserDetailsContract[] = [];

  //constructor
  public constructor(private fb: FormBuilder, private user: UserService, private router: Router) { }

  public refresh() {
    this.ngOnInit();
  }
  
  // ngOnint
  ngOnInit(): void {
    //get All User Details
    this.GetAllUsers();
  }

  public ErrorText: string = '';
  public isFetching: boolean = true;
  // Get All Users
  public GetAllUsers() {
    this.user.getAllUsers().subscribe(data => {
      this.getAllUsers = data;
      this.getAllUsers.forEach(userData=>{
        this.user.getImageByUserId(userData.userId).subscribe(file=>{
          let objectURL = URL.createObjectURL(file);
          userData.profile=objectURL;
        },error=>{
          console.log(error.message);
        })
      })
      this.isFetching = false;
    }, error => {
      this.ErrorText = error.message;
      this.isFetching = false;
    }
    );
  } 

  public DeleteUserById(userId:number) {
    // alert(JSON.stringify(email));
    this.user.deleteUserById(userId).subscribe(data => alert(data));
  }
}
