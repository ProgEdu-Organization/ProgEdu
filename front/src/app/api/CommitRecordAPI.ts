import { environment } from '../../environments/environment';

let getGitLabURL: string = environment.NEW_SERVER_URL + '/commits/gitLab';
let getFeedback: string = environment.NEW_SERVER_URL + '/commits/feedback';
let getPartCommitRecord: string = environment.NEW_SERVER_URL + '/commits/partCommitRecords';
let getAutoAssessment: string = environment.NEW_SERVER_URL + '/commits/autoAssessment';
let getAllUsersCommitRecord: string = environment.NEW_SERVER_URL + '/commits/allUsers';


export const CommitRecordAPI = {
    getGitLabURL: getGitLabURL,
    getFeedback: getFeedback,
    getPartCommitRecord: getPartCommitRecord,
    getAutoAssessment: getAutoAssessment,
    getAllUsersCommitRecord: getAllUsersCommitRecord
}