import { environment } from '../../environments/environment';

let getAllGroupCommitRecord: string = environment.NEW_SERVER_URL + '/groups/commits';


let getCommitRecord = function (groupName: string, projectName:string): string {
    return environment.NEW_SERVER_URL + `/groups/${groupName}/projects/${projectName}/commits`;
} 



export const GroupCommitRecordAPI = {
    getAllGroupCommitRecord: getAllGroupCommitRecord,
    getCommitRecord: getCommitRecord
}