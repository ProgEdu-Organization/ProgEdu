
import { JwtService } from '../services/jwt.service';
export class User {
  private userRole: string;
  private userId: string;

  constructor(private jwtService: JwtService) {
    const decodedToken = jwtService.getDecodedToken();
    console.log(JSON.stringify(decodedToken));
    this.userRole = decodedToken.sub;
    this.userId = decodedToken.aud;
  }
  public getUserRole(): string {
    return this.userRole;
  }

  public getUserId(): string {
    return this.userId;
  }

}
