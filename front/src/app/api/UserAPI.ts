import { environment } from '../../environments/environment';
let getUsers: string = environment.NEW_SERVER_URL + '/user/getUsers';

export const UserAPI = {
    getUsers: getUsers
}