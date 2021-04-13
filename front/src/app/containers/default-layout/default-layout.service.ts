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
    // let token = "v eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwcm9nZWR1Iiwic3ViIjoidGVhY2hlciIsImF1ZCI6InJvb3QiLCJuYW1lIjoicm9vdCIsImV4cCI6MTYxODA3NzUzNSwianRpIjoiNGNiNWEyZWUtZjZjYi00NWYyLWIyNjMtOWRjN2IyM2IyNmVmIn0.gkcUQHYe6lIJHOmcyyoTh-ZBnJrqDycEWtKl2pD-0dg";
    // let headers = new HttpHeaders().set('authorization', token); // create header object
    // headers.append("Authorization", "v eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwcm9nZWR1Iiwic3ViIjoidGVhY2hlciIsImF1ZCI6InJvb3QiLCJuYW1lIjoicm9vdCIsImV4cCI6MTYxODA3Mjk4NSwianRpIjoiMDUzMmQ0YWEtYWE1MC00ZjExLWI3MmEtZDc1NDkwYTM1YWFiIn0.SC6VUudnPdkjGdZ8ZtcvjPoQ8cRsWLiLF2pZhMaKS54");
  //   return this.http.get<any>(this.ALL_COMMIT_API, {
  //     headers: headers
  //  });

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
