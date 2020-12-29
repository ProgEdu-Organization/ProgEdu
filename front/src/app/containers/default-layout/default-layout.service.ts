import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormGroup } from '@angular/forms';

const modifySecretOptions = ({
  headers: new HttpHeaders({
  })
});
@Injectable({
  providedIn: 'root'
})
export class DefaultLayoutService {
  ALL_COMMIT_API = environment.SERVER_URL + '/webapi/commits/allUsers';
  MODIFY_API = environment.SERVER_URL + '/webapi/user/updatePassword';
  constructor(private http: HttpClient) { }

  getNavData(): Observable<any> {
    return this.http.get<any>(this.ALL_COMMIT_API);
  }

  modifySecret(modifySecretForm: FormGroup): Observable<any> {
    const form = new FormData();
    form.append('username', modifySecretForm.get('username').value);
    form.append('currentPassword', modifySecretForm.get('currentPassword').value);
    form.append('newPassword', modifySecretForm.get('newPassword').value);

    return this.http.post<any>(this.MODIFY_API, form, modifySecretOptions);
  }
}
