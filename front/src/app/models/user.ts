
import { JwtService } from '../services/jwt.service';
export class User {
  public isTeacher: boolean = false;
  public isStudent: boolean = false;
  private username: string;
  private name: string;

  constructor(private jwtService: JwtService) {
    const decodedToken = jwtService.getDecodedToken();
    if (decodedToken.sub === 'teacher') {
      this.isTeacher = true;
    } else if (decodedToken.sub === 'student') {
      this.isStudent = true;
    }

    this.username = decodedToken.aud;
    this.name = decodedToken.name;
  }

  public getUsername(): string {
    return this.username;
  }

  public getName(): string {
    return this.name;
  }

}
