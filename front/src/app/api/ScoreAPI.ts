import { environment } from '../../environments/environment';

let getAvgScores: string = environment.NEW_SERVER_URL + '/score/allScore/mean';

let addAssignmentScoreByCsv: string = environment.NEW_SERVER_URL + '/score/assignment/upload';

let addExamScoreByCsv: string = environment.NEW_SERVER_URL + '/score/exam/upload';


export const ScoreAPI = {
    getAvgScores: getAvgScores,
    addAssignmentScoreByCsv: addAssignmentScoreByCsv,
    addExamScoreByCsv: addExamScoreByCsv,
}