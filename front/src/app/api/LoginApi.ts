import { environment } from '../../environments/environment';

let login: string = environment.NEW_SERVER_URL + '/login';

export const LoginAPI = {
    login: login
}