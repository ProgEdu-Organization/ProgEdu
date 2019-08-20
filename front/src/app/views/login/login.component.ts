import { Component, ViewChild, SystemJsNgModuleLoader } from '@angular/core';
import { Router } from '@angular/router';
import { LoginAuthService } from '../../services/login-auth.service';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { JwtService } from '../../services/jwt.service';
@Component({
  selector: 'app-dashboard',
  templateUrl: 'login.component.html'
})
export class LoginComponent {

  private errors;
  public loginForm: FormGroup;
  @ViewChild('dangerModal', { static: false }) public dangerModal: ModalDirective;

  constructor(private router: Router, private _loginAuthService: LoginAuthService, private fb: FormBuilder,
    private jwtService: JwtService) {
    this.loginForm = this.fb.group({
      username: ['', Validators.pattern('^[a-zA-Z0-9-_]{5,20}')],
      password: ['', Validators.pattern('^[a-zA-Z0-9-_]{5,20}')],
      rememberMe: [true]
    });
    this.autoLogin();
  }

  onInit() {
  }
  public getUsername() { return this.loginForm.value.username; }
  public getPassword() { return this.loginForm.value.password; }

  async login() {
    this._loginAuthService.Login(this.getUsername(), this.getPassword()).subscribe(
      (response) => {
        console.log(response);
        if (!response.isLogin) {
          this.dangerModal.show();
        } else {
          this.jwtService.setToken(response.token);
          if (response.user === 'teacher') {
            this.router.navigate(['dashboard']);
          } else if (response.user === 'student') {
            this.router.navigate(['studashboard']);
          }
        }
      },
      (err) => {
        this.router.navigate(['500']);
      }
    );
  }
  async autoLogin() {
    if (this.jwtService.getToken() != null) {
      const decodedToken = this.jwtService.getDecodedToken();
      console.log(JSON.stringify(decodedToken));
      if (!this.jwtService.isTokenExpired()) {
        if (decodedToken.sub === 'teacher') {
          this.router.navigate(['dashboard']);
        } else {
          this.router.navigate(['studashboard']);
        }
      } else {
        this.jwtService.removeToken();
      }
    }
  }
}
