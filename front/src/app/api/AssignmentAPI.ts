import { environment } from '../../environments/environment';


let createAssignment: string = environment.NEW_SERVER_URL + '/assignment/create';

//這是只適用Maven 有順序的創作業API
let createAutoAssessment: string = environment.NEW_SERVER_URL + '/assignment/autoAssessment/create';

let createPeerReviewAssignment: string = environment.NEW_SERVER_URL + '/assignment/peerReview/create';


let getAllAssignments: string = environment.NEW_SERVER_URL + '/assignment/getAllAssignments';

//顯示學生普通的作業
let getAllAutoAssessment: string = environment.NEW_SERVER_URL + '/assignment/autoAssessment/allAssignment';

//顯示peer review學生的作業
let getAllPeerReviewAssignment: string = environment.NEW_SERVER_URL + '/assignment/peerReview/allAssignment';

let getAssignmentDescription: string = environment.NEW_SERVER_URL + '/assignment/getAssignment';

let getAssignmentOrder: string = environment.NEW_SERVER_URL + '/assignment/getAssignmentOrder';


let editAssignment: string = environment.NEW_SERVER_URL + '/assignment/edit';

let deleteAssignment: string = environment.NEW_SERVER_URL + '/assignment/delete';

let getAssignmentFile: string = environment.NEW_SERVER_URL + '/assignment/getAssignmentFile';

let modifyAssignmentOrderFile: string = environment.NEW_SERVER_URL + '/assignment/order';



export const AssignmentAPI = {
    createAssignment: createAssignment,
    createAutoAssessment: createAutoAssessment,
    createPeerReviewAssignment: createPeerReviewAssignment,

    getAllAssignments: getAllAssignments,
    getAllAutoAssessment: getAllAutoAssessment,
    getAllPeerReviewAssignment: getAllPeerReviewAssignment,
    getAssignmentDescription: getAssignmentDescription,
    getAssignmentOrder: getAssignmentOrder,
    
    editAssignment: editAssignment,
    deleteAssignment: deleteAssignment,

    getAssignmentFile: getAssignmentFile,
    modifyAssignmentOrderFile: modifyAssignmentOrderFile
}