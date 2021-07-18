import { environment } from '../../environments/environment';


let createAssignment: string = environment.NEW_SERVER_URL + '/assignment/create';
let getAllAssignments: string = environment.NEW_SERVER_URL + '/assignment/getAllAssignments';
let createAutoAssessment: string = environment.NEW_SERVER_URL + '/assignment/autoAssessment/create';


export const AssignmentAPI = {
    createAssignment: createAssignment,
    getAllAssignments: getAllAssignments,
    createAutoAssessment: createAutoAssessment
}