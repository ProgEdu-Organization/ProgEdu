import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})


export class DashboardService {
  ALL_COMMIT_API = environment.SERVER_URL + '/webapi/commits/allUsers';
  ALL_ASSIGNMENT_API = environment.SERVER_URL + '/webapi/assignment/getAllAssignments';
  constructor(private http: HttpClient) { }

  getAllStudentCommitRecord(): Observable<any> {

    // var header = ({
    //   headers: new HttpHeaders()
    //     .set('authorization',  "y eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwcm9nZWR1Iiwic3ViIjoidGVhY2hlciIsImF1ZCI6InJvb3QiLCJuYW1lIjoicm9vdCIsImV4cCI6MTYxODEzNjI2MywianRpIjoiNmNmMzU2MDAtZTk4NC00OWZjLWEwNGYtOWJkNTVkYzNiNGE4In0.i7KCRAla3ko_yPV8JGhKa6koPCE8kvccdav9sNiQHSk")
    // });

    // const franky_test = ({
    //   headers: new HttpHeaders({
    //     'Content-Type' : 'application/json; charset=utf-8',
    //     'Accept'       : 'application/json',
    //     'authorization': 'y eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwcm9nZWR1Iiwic3ViIjoidGVhY2hlciIsImF1ZCI6InJvb3QiLCJuYW1lIjoicm9vdCIsImV4cCI6MTYxODEzNjI2MywianRpIjoiNmNmMzU2MDAtZTk4NC00OWZjLWEwNGYtOWJkNTVkYzNiNGE4In0.i7KCRAla3ko_yPV8JGhKa6koPCE8kvccdav9sNiQHSk'
    //   }),
    //   withCredentials: true
    // });

    const franky_test = ({
      headers: new HttpHeaders({
        // 'Content-Type' : 'application/jso',
        'authorization' : 'y eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwcm9nZWR1Iiwic3ViIjoidGVhY2hlciIsImF1ZCI6InJvb3QiLCJuYW1lIjoicm9vdCIsImV4cCI6MTYxODEzNjI2MywianRpIjoiNmNmMzU2MDAtZTk4NC00OWZjLWEwNGYtOWJkNTVkYzNiNGE4In0.i7KCRAla3ko_yPV8JGhKa6koPCE8kvccdav9sNiQHSk',
      })
    });

    return this.http.get<any>(this.ALL_COMMIT_API, franky_test);
    // return this.http.get<any>(this.ALL_COMMIT_API);
  }

  getAllAssignments(): Observable<any> {
    return this.http.get<any>(this.ALL_ASSIGNMENT_API);
  }

}
