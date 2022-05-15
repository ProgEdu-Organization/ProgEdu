import { environment } from '../../environments/environment';

let getAvgScores: string = environment.NEW_SERVER_URL + '/score/allScore/mean';

let addAssignmentScoreByCsv: string = environment.NEW_SERVER_URL + '/score/assignment/upload';

let addExamScoreByCsv: string = environment.NEW_SERVER_URL + '/score/exam/upload';

let getAllUserScore: string = environment.NEW_SERVER_URL + '/score/allUser';

let editUsersScore: string = environment.NEW_SERVER_URL + '/score/update';

let deleteAllScores: string = environment.NEW_SERVER_URL + '/score/delete';


export const ScoreAPI = {
    getAvgScores: getAvgScores,
    addAssignmentScoreByCsv: addAssignmentScoreByCsv,
    addExamScoreByCsv: addExamScoreByCsv,
    getAllUserScore: getAllUserScore,
    editUsersScore: editUsersScore,
    deleteAllScores: deleteAllScores
}