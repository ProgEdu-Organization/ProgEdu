import { environment } from '../../environments/environment';

let getAllGroup: string = environment.NEW_SERVER_URL + '/groups';


let getGroup = function (groupName: string): string {
    return environment.NEW_SERVER_URL + `/groups/${groupName}`;
} 


let updateLeader = function (groupName: string, leader: string): string {
    return environment.NEW_SERVER_URL + `/groups/${groupName}/members/${leader[0]}`;
} 

let removeMember = function (groupName: string, member: string): string {
    return environment.NEW_SERVER_URL + `/groups/${groupName}/members/${member[0]}`;
} 

let addMembers = function (groupName: string): string {
    return environment.NEW_SERVER_URL + `/groups/${groupName}/members`;
} 

let createGroup: string = environment.NEW_SERVER_URL + '/groups';

let removeGroup = function (groupName: string): string {
    return environment.NEW_SERVER_URL + `/groups/${groupName}`;
} 

export const GroupsAPI = {
    getGroup: getGroup,
    getAllGroup: getAllGroup,
    updateLeader: updateLeader,
    removeMember: removeMember,
    addMembers: addMembers,
    createGroup: createGroup,
    removeGroup: removeGroup
}
   