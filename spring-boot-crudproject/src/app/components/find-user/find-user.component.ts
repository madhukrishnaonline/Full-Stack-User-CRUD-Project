import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { UserDetailsContract } from '../contracts/UserDetailsContracts';

@Component({
  selector: 'app-find-user',
  templateUrl: './find-user.component.html',
  styleUrls: ['./find-user.component.css']
})
export class FindUserComponent implements OnInit {

  public constructor(private user: UserService) { }

  public objectData: UserDetailsContract = {
    userId: 0,
    userName: "",
    profile: null,
    email: "",
    fullName: "",
    address: "",
    city: "",
    state: "",
    mobile: null,
  };
  ngOnInit(): void {
    // this.FindUser();
  }
  ERequired: string = "";
  userName: string = "";
  errMsg: string = "";
  errStatus: number | null = null;

  isFinding: boolean = false;
  fetchingImage:boolean=false;
  public found: string = "";
  // Find User User
  public FindUser() {
    // alert(this.userName);
    this.isFinding = true;
    this.user.findByUserName(this.userName).subscribe(data => {
      this.objectData = data;
      console.log(data);
      this.isFinding = false;
    }, error => {
      this.isFinding = false;
      if (error.status == 302) {
        this.objectData = error.error;
        this.fetchingImage=true;
        this.user.getImageByUserId(this.objectData.userId).subscribe(file => {
          let objectURL = URL.createObjectURL(file);
          this.objectData.profile = objectURL;
          this.fetchingImage=false;
          // console.log(this.objectData.profile);
        }, error => {
          console.log(error.message);
        })
        this.found = "Found";
        // this.errStatus = error.status;
        // console.log("error: ", error);
      } else {
        this.errMsg = error.message;
        this.errStatus = error.status;
        this.found = "Not Found";
      }
    });
  }

}
