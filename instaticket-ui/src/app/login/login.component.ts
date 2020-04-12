import { Component, OnInit, HostListener } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { DataServiceService } from '../data-service.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  height: any = 500;
  loginForm: FormGroup;


  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.height = window.innerHeight;
  }
  constructor(private fb: FormBuilder, private dataService: DataServiceService) { }

  ngOnInit() {
    this.height = window.innerHeight;
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    })

  }

  get f() {
    return this.loginForm.controls;
  }
  onSubmit() {

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }
    var user = { username: this.f.username.value, password: this.f.password.value };

    //console.log(this.f.username.value);


    this.dataService.login(user).subscribe(data => {
      console.log(data)
    })
    console.log("Form Submitted")
  }

}
