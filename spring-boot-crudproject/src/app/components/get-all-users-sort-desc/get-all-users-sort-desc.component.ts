import { Component } from '@angular/core';
import { UserDetailsContract } from '../contracts/UserDetailsContracts';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-get-all-users-sort-desc',
  templateUrl: './get-all-users-sort-desc.component.html',
  styleUrls: ['./get-all-users-sort-desc.component.css']
})
export class GetAllUsersSortDescComponent {
  public getAllUsers: UserDetailsContract[] = [];

  //constructor
  public constructor(private user: UserService) { }

  public refresh() {
    this.ngOnInit();
  }

  // ngOnint
  ngOnInit(): void {
    //get All User Details
    this.GetAllUsersBySort();
  }

  public ErrorText: string = '';
  public isFetching: boolean = true;
  // Get All Users
  public GetAllUsersBySort() {
    this.user.getAllUsersBySortDesc().subscribe(data => {
      this.getAllUsers = data;
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
