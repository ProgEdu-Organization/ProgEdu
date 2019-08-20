
import { JwtService } from '../services/jwt.service';
export class User {
  private isAdmin: boolean;
  private userId: string;

  constructor(private jwtService: JwtService) {
    const decodedToken = jwtService.getDecodedToken();
    console.log('decodedToken' + JSON.stringify(decodedToken));
    this.isAdmin = decodedToken.admin;
    console.log('isAdmin' + decodedToken.admin);
    this.userId = decodedToken.aud;
  }
  public getIsAdmin(): boolean {
    return this.isAdmin;
  }

  public getUsername(): string {
    return this.userId;
  }

}
