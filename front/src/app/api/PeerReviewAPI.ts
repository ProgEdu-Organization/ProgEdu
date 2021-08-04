import { environment } from '../../environments/environment';

let getAllReviewedRecord: string = environment.NEW_SERVER_URL + '/peerReview/record/allUsers';
let getReviewStatus: string = environment.NEW_SERVER_URL + '/peerReview/status/oneUser';

export const PeerReviewAPI = {
    getAllReviewedRecord: getAllReviewedRecord,
    getReviewStatus: getReviewStatus
}