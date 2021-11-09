import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormGroup } from '@angular/forms';
import {AddJwtTokenHttpClient} from '../../services/add-jwt-token.service';
import { UserAPI } from '../../api/UserAPI';
import { CommitRecordAPI } from '../../api/CommitRecordAPI';

const modifySecretOptions = ({
  headers: new HttpHeaders({
  })
});
@Injectable({
  providedIn: 'root'
})
export class DefaultLayoutService {
  ALL_COMMIT_API = CommitRecordAPI.getAllUsersCommitRecord
  MODIFY_API = UserAPI.updatePassword;
  constructor(private http: HttpClient, private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getNavData(): Observable<any> {
   return this.addJwtTokenHttpClient.get(this.ALL_COMMIT_API);
  }

  modifySecret(modifySecretForm: FormGroup): Observable<any> {
    const form = new FormData();
    form.append('username', modifySecretForm.get('username').value);
    form.append('currentPassword', modifySecretForm.get('currentPassword').value);
    form.append('newPassword', modifySecretForm.get('newPassword').value);

    return this.addJwtTokenHttpClient.post(this.MODIFY_API, form, modifySecretOptions);
  }
}
