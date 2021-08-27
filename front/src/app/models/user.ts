
import { JwtService } from '../services/jwt.service';
export class User {
  public isTeacher: boolean = false;
  public isStudent: boolean = false;
  private username: string;
  private name: string;

  constructor(private jwtService: JwtService) {
    const jwtInfo = jwtService.getDecodedToken();

    if (jwtInfo.authorities.includes("ROLE_TEACHER")) {
      this.isTeacher = true;
    } else if (jwtInfo.authorities.includes("ROLE_STUDENT")) {
      this.isStudent = true;
    }

    this.username = jwtInfo.sub; // Todo username 跟 name 一樣, 這之後要拿掉其中一個
    this.name = jwtInfo.sub;
  }

  public getUsername(): string {
    return this.username;
  }

  public getName(): string {
    return this.name;
  }

}
