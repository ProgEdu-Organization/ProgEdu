import { environment } from '../../environments/environment';

let getAllGroupCommitRecord: string = environment.NEW_SERVER_URL + '/groups/commits';


let getCommitRecord = function (groupName: string, projectName:string): string {
    return environment.NEW_SERVER_URL + `/groups/${groupName}/projects/${projectName}/commits`;
} 

let getPartCommitRecord = function (groupName: string, projectName:string, currentPage:string): string {
    return environment.NEW_SERVER_URL + `/groups/${groupName}/projects/${projectName}/partCommits/${currentPage}`;
} 

let getFeedback = function (groupName: string, projectName:string, commitNumber:string): string {
    return environment.NEW_SERVER_URL + `/groups/${groupName}/projects/${projectName}/feedback/${commitNumber}`;
} 



export const GroupCommitRecordAPI = {
    getAllGroupCommitRecord: getAllGroupCommitRecord,
    getCommitRecord: getCommitRecord,
    getPartCommitRecord: getPartCommitRecord,
    getFeedback: getFeedback
}