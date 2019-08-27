
import { JwtService } from '../services/jwt.service';
export class User {
  private isTeacher: boolean = false;
  private username: string;

  constructor(private jwtService: JwtService) {
    const decodedToken = jwtService.getDecodedToken();
    console.log('decodedToken' + JSON.stringify(decodedToken));
    if (decodedToken.sub === 'teacher') {
      this.isTeacher = true;
    }
    console.log('isTeacher: ' + this.isTeacher);
    this.username = decodedToken.aud;
  }
  public getIsTeacher(): boolean {
    return this.isTeacher;
  }

  public getUsername(): string {
    return this.username;
  }

}
