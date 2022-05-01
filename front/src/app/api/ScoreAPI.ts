import { environment } from '../../environments/environment';

let getScores: string = environment.NEW_SERVER_URL + '/score/getScores';

let addAssignmentScoreByCsv: string = environment.NEW_SERVER_URL + '/score/assignment/upload';

let addExamScoreByCsv: string = environment.NEW_SERVER_URL + '/score/exam/upload';


export const ScoreAPI = {
    getScores: getScores,
    addAssignmentScoreByCsv: addAssignmentScoreByCsv,
    addExamScoreByCsv: addExamScoreByCsv
}