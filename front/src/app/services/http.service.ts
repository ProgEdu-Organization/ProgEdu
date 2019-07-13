import { Injectable } from '@angular//core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HttpService {
  constructor(private http: HttpClient) { }

  getData(URL: string): Promise<any> {
    const result = this.http.get<any>(URL).toPromise()
      .catch(this.handleError);
    return result;
  }

  postData(URL: string, data: any): Promise<any> {
    const options = ({
      headers: new HttpHeaders({
        'Content-Type': 'application/x-www-form-urlencoded',
      })
    });

    const result = this.http.post<any>(URL, data, options).toPromise()
      .catch(this.handleError);
    return result;
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // return an observable with a user-facing error message
    return throwError(
      'Something bad happened; please try again later.');
  }
}
