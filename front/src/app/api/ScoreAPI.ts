import { environment } from '../../environments/environment';

let getScores: string = environment.NEW_SERVER_URL + '/score/getScores';

let addScoreByCsv: string = environment.NEW_SERVER_URL + '/score/assignment/upload';


export const ScoreAPI = {
    getScores: getScores,
    addScoreByCsv: addScoreByCsv
}