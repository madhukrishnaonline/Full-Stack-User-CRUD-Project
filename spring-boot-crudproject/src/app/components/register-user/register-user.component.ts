import { Component } from '@angular/core';
import { UserDetailsContract } from '../contracts/UserDetailsContracts';
import { FormBuilder, Validators } from '@angular/forms';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-user',
  templateUrl: './register-user.component.html',
  styleUrls: ['./register-user.component.css']
})
export class RegisterUserComponent {
  //constructor
  public constructor(private fb: FormBuilder, private user: UserService, private router: Router) { }

  public formRegister = this.fb.group({
    userName: this.fb.control('', Validators['required']),
    profile: this.fb.control(null, Validators['required']),
    password: this.fb.control('', Validators['required']),
    fullName: this.fb.control('', Validators['required']),
    email: this.fb.control('', Validators['required']),
    address: this.fb.control('', Validators['required']),
    city: this.fb.control('', Validators['required']),
    state: this.fb.control('', Validators['required']),
    mobile: this.fb.control(null, Validators['required'])
  });

  get userName() {
    return this.formRegister.get("userName");
  }
  get password() {
    return this.formRegister.get("password");
  }
  get profile() {
    return this.formRegister.get("profile");
  }
  get email() {
    return this.formRegister.get("email");
  }
  get fullName() {
    return this.formRegister.get("fullName");
  }
  get address() {
    return this.formRegister.get("address");
  }
  get city() {
    return this.formRegister.get("city");
  }
  get state() {
    return this.formRegister.get("state");
  }
  get mobile() {
    return this.formRegister.get("mobile");
  }


  registerMessage: any;
  ERequired: boolean = false;
  prRequired: boolean = false;
  PRequired: boolean = false;
  MRequired: boolean = false;
  FRequired: boolean = false;
  ARequired: boolean = false;
  CRequired: boolean = false;
  SRequired: boolean = false;

  isRegistering: boolean = false;

  SelectedFile: File | null = null;

  onFileSelected(event: any) {
    // const file = event.target.files[0];
    const file = event.target as HTMLInputElement;
    if (file.files?.length) {
      this.SelectedFile = file.files[0];
    }
  }

  // Register User
  public RegisterUser() {
    this.isRegistering = true;
    if (this.formRegister.valid) {
      const formData = new FormData();
      const UserJson = this.formRegister.value;
      delete UserJson.profile;
      formData.append("userJson", JSON.stringify(UserJson));
      if (this.SelectedFile) {
        formData.append('file', this.SelectedFile);
        // console.log(this.SelectedFile);
      }
      // formData.forEach((v, k) => {
      //   console.log(k, v);
      // });
      this.user.registerUser(formData).subscribe(response => {
        this.registerMessage = response;
        console.log(response);
        this.isRegistering = false;
        // alert(this.registerMessage);
        this.formRegister.reset();
        this.router.navigate(['login/user']);
      }, error => {
        // this.RegistrationError = error.message;
        this.isRegistering = false;
      });
      // this.formRegister.reset();
    }
    else {
      this.isRegistering = false;
      this.ERequired = true;
      this.prRequired = true;
      this.PRequired = true;
      this.MRequired = true;
      this.FRequired = true;
      this.ARequired = true;
      this.CRequired = true;
      this.SRequired = true;
    }
  }

}
