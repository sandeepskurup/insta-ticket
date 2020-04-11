import { Component, OnInit, HostListener } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  height: any = 500;

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.height = window.innerHeight;
  }
  constructor() { }

  ngOnInit() {
    this.height = window.innerHeight;
  }

}
