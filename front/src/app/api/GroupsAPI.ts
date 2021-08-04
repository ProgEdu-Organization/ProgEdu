import { environment } from '../../environments/environment';

let getAllGroup: string = environment.NEW_SERVER_URL + '/groups';


let getGroup = function (groupName: string): string {
    return environment.NEW_SERVER_URL + `/groups/${groupName}`;
} 




export const GroupsAPI = {
    getGroup: getGroup,
    getAllGroup: getAllGroup
}
   