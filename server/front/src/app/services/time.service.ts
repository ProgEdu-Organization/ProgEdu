import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TimeService {

  constructor() { }

  getUTCTime(time: any): Date {
    const timeOffset = (new Date().getTimezoneOffset() * 60 * 1000);
    const assigenmentTime = new Date(time).getTime();
    return new Date(assigenmentTime - timeOffset);
  }
}
