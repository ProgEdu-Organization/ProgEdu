import { environment } from '../../environments/environment';

let login: string = environment.NEW_SERVER_URL + '/LoginAuth';
let checkLogin: string = environment.NEW_SERVER_URL + '/auth/login';


export const LoginAPI = {
    login: login,
    checkLogin: checkLogin
}