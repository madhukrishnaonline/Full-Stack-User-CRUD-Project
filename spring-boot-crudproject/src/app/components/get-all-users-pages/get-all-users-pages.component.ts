import { PageDetailsContract } from './../contracts/PageDetailsContract';
import { UserDetailsContract } from './../contracts/UserDetailsContracts';
import { Component } from '@angular/core';
import { UserService } from '../services/user.service';
import { UserDashBoardComponent } from '../user-dash-board/user-dash-board.component';

@Component({
  selector: 'app-get-all-users-pages',
  templateUrl: './get-all-users-pages.component.html',
  styleUrls: ['./get-all-users-pages.component.css']
})
export class GetAllUsersPagesComponent {

  public pageDetails: PageDetailsContract= {
    totalPages: 0,
    currentPage: 0,
    isFirst: false,
    isLast: false,
    isEmpty: false,
    hasPrevious: false,
    hasNext: false,
    noOfRecordsInCurrentPage:0,
    totalNoOfRecords:0,
    userDataBindResponse: []
  };

  //constructor
  public constructor(private user: UserService) { }

  public pages:number[] = [];
  public refresh() {
    this.ngOnInit();
  }
  
  public pageNo: number = 0;
  public pageSize: number = 5;
  
  // ngOnint
  ngOnInit(): void {
    //get All User Details
    this.GetAllUsersByPages1(this.pageNo, this.pageSize);
    // console.log(this.pageDetails.userDataBindResponse)
  }
  
  public ErrorText: string = '';
  public isFetching: boolean = true;
  // Get All Users
  public GetAllUsersByPages1(pageNo: number, pageSize: number) {
    this.user.getAllUsersByPages1(pageNo, pageSize).subscribe(data => {
      this.pageDetails = data;
      this.isFetching = false;
      for(let i=1;i<=this.pageDetails.totalPages;i++){
        this.pages.push(i);
      }
    }, error => {
      this.ErrorText = error.message;
      this.isFetching = false;
    }
    );
  }

  nextCount:number=0;
  previousCount:number=0;
  //Next Page
  public next() {
    this.nextCount++;
    this.GetAllUsersByPages1(this.nextCount,this.pageSize);  
    if(this.pageDetails.isLast){
      this.nextCount--;
    }
    this.previousCount = this.nextCount;
  }
  //Previous Page
  public previous() {
    this.previousCount--;
    this.GetAllUsersByPages1(this.previousCount,this.pageSize);
  }

  page:number = 0;
  public GetSpecificPage(e:any){
    // console.log(e.target.value);
    this.page = e.target.value-1;
    this.GetAllUsersByPages1(this.page,this.pageSize);
  }

  public DeleteUserById(userId:number) {
    // alert(JSON.stringify(email));
    this.user.deleteUserById(userId).subscribe(response => alert(response));
  }
}
