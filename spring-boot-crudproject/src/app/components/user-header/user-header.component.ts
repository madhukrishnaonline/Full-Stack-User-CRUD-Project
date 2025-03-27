import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-user-header',
  templateUrl: './user-header.component.html',
  styleUrls: ['./user-header.component.css']
})
export class UserHeaderComponent implements OnInit {
  constructor(private user: UserService) { }
  fullName: string = "";
  Refresh(){
    this.fullName = this.user.getFullName();
  }
  ngOnInit() {
    this.Refresh();
  }
}
