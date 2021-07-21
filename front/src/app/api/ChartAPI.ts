import { environment } from '../../environments/environment';

let getAllCommitRecord: string = environment.NEW_SERVER_URL + '/chart/allCommitRecord';


export const ChartAPI = {
    getAllCommitRecord: getAllCommitRecord
}