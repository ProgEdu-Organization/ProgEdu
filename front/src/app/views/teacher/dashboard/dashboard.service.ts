import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { JwtService } from '../../../services/jwt.service';

@Injectable({
  providedIn: 'root'
})


export class DashboardService {
  ALL_COMMIT_API = environment.SERVER_URL + '/webapi/commits/allUsers';
  ALL_ASSIGNMENT_API = environment.SERVER_URL + '/webapi/assignment/getAllAssignments';
  constructor(private http: HttpClient) { }


  // call(url, type, data) {
  //   var request = $.ajax({
  //     url: url,
  //     method: "GET",
  //     beforeSend: function(xhr){xhr.setRequestHeader('authorization', 'Bearer eyJhbGciOiJIUzI1');}
     
  //   });
 
  //   request.done(function(resp) {
  //     console.log(resp);
  //   });
 
  //   request.fail(function(jqXHR, textStatus) {
  //     console.log("Request failed: " + textStatus);
  //   });
  // };

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

    // const franky_test = ({
    //   headers: new HttpHeaders({
    //     'Authorization' : 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwcm9nZWR1Iiwic3ViIjoidGVhY2hlciIsImF1ZCI6InJvb3QiLCJuYW1lIjoicm9vdCIsImV4cCI6MTYxODEzNjI2MywianRpIjoiNmNmMzU2MDAtZTk4NC00OWZjLWEwNGYtOWJkNTVkYzNiNGE4In0.i7KCRAla3ko_yPV8JGhKa6koPCE8kvccdav9sNiQHSk'
    //   }),
    //   withCredentials: true
    // });

    // return this.http.get<any>(this.ALL_COMMIT_API, franky_test);



    // this.call('http://httpbin.org/get', "GET", "application/json");
    // this.call(this.ALL_COMMIT_API, "GET", "application/json");

    const jwtService = new JwtService();


    const params = new HttpParams()
    .set('token', jwtService.getToken());

    return this.http.get<any>(this.ALL_COMMIT_API, {params});
  }

  getAllAssignments(): Observable<any> {

    return this.http.get<any>(this.ALL_ASSIGNMENT_API);

  }

}
