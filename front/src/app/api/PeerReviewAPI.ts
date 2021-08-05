import { environment } from '../../environments/environment';

let getAllReviewedRecord: string = environment.NEW_SERVER_URL + '/peerReview/record/allUsers';
let getReviewStatus: string = environment.NEW_SERVER_URL + '/peerReview/status/oneUser';

let getReviewMetrics: string = environment.NEW_SERVER_URL + '/peerReview/metrics';
let getReviewedRecordDetail: string = environment.NEW_SERVER_URL + '/peerReview/record/detail';
let getAllReviewStatus: string = environment.NEW_SERVER_URL + '/peerReview/status/allUsers';

let getReviewStatusDetail: string = environment.NEW_SERVER_URL + '/peerReview/status/detail';



export const PeerReviewAPI = {
    getAllReviewedRecord: getAllReviewedRecord,
    getReviewStatus: getReviewStatus,
    getReviewMetrics: getReviewMetrics,
    getReviewedRecordDetail: getReviewedRecordDetail,
    getAllReviewStatus: getAllReviewStatus,
    getReviewStatusDetail: getReviewStatusDetail
}