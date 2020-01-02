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

  getIntervalTime(releaseTime: Date, deadline: Date) {
    // ex: 12-12 ~ 12-20 -> return [12-12, 12-13, 12-14 ... 12-20]
    let set: Set<any> = new Set();
    let time_diff: number = deadline.getTime() - releaseTime.getTime();
    for(let i=0; i <= time_diff; i += 86400000){
      var date = new Date(releaseTime.getTime() + i);
      set.add((date.getMonth() + 1) +'-'+ date.getDate());
    }
    set.add((releaseTime.getMonth() + 1) +'-'+ releaseTime.getDate());
    set.add((deadline.getMonth() + 1) +'-'+ deadline.getDate());
    return set;

  }
}
