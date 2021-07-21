import { environment } from '../../environments/environment';

let getUsers: string = environment.NEW_SERVER_URL + '/user/getUsers';

let addOneUser: string = environment.NEW_SERVER_URL + '/user/new';
let addUserByCsv: string = environment.NEW_SERVER_URL + '/user/upload';

let updatePassword: string = environment.NEW_SERVER_URL + '/user/updatePassword';

export const UserAPI = {
    getUsers: getUsers,
    addOneUser: addOneUser,
    addUserByCsv: addUserByCsv,
    updatePassword: updatePassword
}