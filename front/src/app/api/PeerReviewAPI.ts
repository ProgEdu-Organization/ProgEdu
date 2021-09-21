import { environment } from '../../environments/environment';

let getAllReviewedRecord: string = environment.NEW_SERVER_URL + '/peerReview/record/allUsers';
let getReviewStatus: string = environment.NEW_SERVER_URL + '/peerReview/status/oneUser';

let getReviewMetrics: string = environment.NEW_SERVER_URL + '/peerReview/metrics';
let getReviewedRecordDetail: string = environment.NEW_SERVER_URL + '/peerReview/record/detail';
let getAllReviewStatus: string = environment.NEW_SERVER_URL + '/peerReview/status/allUsers';

let getReviewStatusDetail: string = environment.NEW_SERVER_URL + '/peerReview/status/detail';

let createReviewRecord: string = environment.NEW_SERVER_URL + '/peerReview/create';

let getSourceCode: string = environment.NEW_SERVER_URL + '/peerReview/sourceCode';

let getOneUserReviewedRecord: string = environment.NEW_SERVER_URL + '/peerReview/record/oneUser';
let getReviewRoundStatus: string = environment.NEW_SERVER_URL + '/peerReview/status/round/oneUser';
let getAllReviewRoundStatus: string = environment.NEW_SERVER_URL + '/peerReview/status/round/allUsers';
let getReviewRoundStatusDetail: string = environment.NEW_SERVER_URL + '/peerReview/status/round/detail';

export const PeerReviewAPI = {
    getAllReviewedRecord: getAllReviewedRecord,
    getReviewStatus: getReviewStatus,
    getReviewMetrics: getReviewMetrics,
    getReviewedRecordDetail: getReviewedRecordDetail,
    getAllReviewStatus: getAllReviewStatus,
    getReviewStatusDetail: getReviewStatusDetail,
    createReviewRecord: createReviewRecord,
    getSourceCode: getSourceCode,
    getOneUserReviewedRecord: getOneUserReviewedRecord,
    getReviewRoundStatus: getReviewRoundStatus,
    getAllReviewRoundStatus: getAllReviewRoundStatus,
    getReviewRoundStatusDetail: getReviewRoundStatusDetail
}