import { Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { LoginAuthService } from '../../services/login-auth.service';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms'
import { ModalDirective } from 'ngx-bootstrap/modal';
import { JwtModule } from '@auth0/angular-jwt';

@Component({
  selector: 'app-dashboard',
  templateUrl: 'login.component.html'
})
export class LoginComponent {
  public loginForm: FormGroup;
  @ViewChild('dangerModal', { static: false }) public dangerModal: ModalDirective;

  constructor(private router: Router, private _loginAuthService: LoginAuthService, private fb: FormBuilder) {
    this.loginForm = this.fb.group({
      username: ['', Validators.pattern('^[a-zA-Z0-9-_]{5,20}')],
      password: ['', Validators.pattern('^[a-zA-Z0-9-_]{5,20}')],
      rememberMe: [true]
    });
  }

  onInit() {
    this.autoLogin();
  }
  public getUsername() { return this.loginForm.value.username; }
  public getPassword() { return this.loginForm.value.password; }

  async login() {
    const response = await this._loginAuthService.Login(this.getUsername(), this.getPassword());
    console.log(response);
    if (!response.isLogin) {
      this.dangerModal.show();
    } else {
      localStorage.setItem('token', response.token);
      if (response.user === 'teacher') {
        this.router.navigate(['dashboard']);
      } else if (response.user === 'student') {
        this.router.navigate(['studashboard']);
      }
    }
  }
  autoLogin() {

  }
}
