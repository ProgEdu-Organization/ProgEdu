
import { JwtService } from '../services/jwt.service';
export class User {
  public isTeacher: boolean = false;
  private username: string;

  constructor(private jwtService: JwtService) {
    const decodedToken = jwtService.getDecodedToken();
    if (decodedToken.sub === 'teacher') {
      this.isTeacher = true;
    }
    this.username = decodedToken.aud;
  }

  public getUsername(): string {
    return this.username;
  }

}
