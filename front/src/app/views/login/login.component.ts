import { StudentEventsService } from './../../services/student-events-log.service';
import { Component, ViewChild, SystemJsNgModuleLoader, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginAuthService } from '../../services/login-auth.service';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { JwtService } from '../../services/jwt.service';
import { StudentEvent } from '../../services/student-event';

@Component({
  selector: 'app-dashboard',
  templateUrl: 'login.component.html'
})
export class LoginComponent implements OnInit {

  private errors;
  public loginForm: FormGroup;
  @ViewChild('dangerModal', { static: false }) public dangerModal: ModalDirective;

  constructor(private router: Router, private _loginAuthService: LoginAuthService, private fb: FormBuilder,
    private jwtService: JwtService, private studentEventsService: StudentEventsService) { }

  emitStudentEvent(event: StudentEvent) {
    this.studentEventsService.createReviewRecord(event);
  }

  ngOnInit() {
    this.loginForm = this.fb.group({
      username: ['', Validators.pattern('^[a-zA-Z0-9-_]{4,20}')],
      password: ['', Validators.pattern('^[a-zA-Z0-9-_]{8,20}')],
      rememberMe: [true]
    });

    this.onChanges();
    this.autoLogin();
  }
  onChanges(): void {
    const username = 'username';
    const password = 'password';

    this.loginForm.get(username).valueChanges.subscribe(
      () => {
        this.loginForm.get(username).valid ? this.showIsValidById(username) : this.hideIsInvalidById(username);
      }
    );

    this.loginForm.get(password).valueChanges.subscribe(
      val => {
        this.loginForm.get(password).valid ? this.showIsValidById(password) : this.hideIsInvalidById(password);
      }
    );
  }

  showIsValidById(id: string) {
    $('#' + id).addClass('is-valid');
    $('#' + id).removeClass('is-invalid');
  }

  hideIsInvalidById(id: string) {
    $('#' + id).removeClass('is-valid');
    $('#' + id).addClass('is-invalid');
  }

  public getUsername() { return this.loginForm.value.username; }
  public getPassword() { return this.loginForm.value.password; }

  async login() {
    this._loginAuthService.Login(this.getUsername(), this.getPassword()).subscribe(
      (response) => {


        if(response == "fail!") {
          this.dangerModal.show();
        } else {
          this.jwtService.setToken(response);

          let jwtInfo = this.jwtService.getDecodedToken();

          if (jwtInfo.authorities.includes("ROLE_TEACHER")) {
            this.router.navigate(['dashboard']);
          } else if (jwtInfo.authorities.includes("ROLE_STUDENT")) {
            const event: StudentEvent = {name: 'progedu.login', page: this.router.url, event: {} };
            this.emitStudentEvent(event);
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
      if (!this.jwtService.isTokenExpired()) {
        if (decodedToken.authorities.includes("ROLE_TEACHER")) {
          this.router.navigate(['dashboard']);
        } else if(decodedToken.authorities.includes("ROLE_STUDENT")) {
          this.router.navigate(['studashboard']);
        }
      } else {
        this.jwtService.removeToken();
      }
    }
  }
}
