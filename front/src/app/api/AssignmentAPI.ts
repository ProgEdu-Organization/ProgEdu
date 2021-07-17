import { environment } from '../../environments/environment';


let createAssignment: string = environment.NEW_SERVER_URL + '/assignment/create';
let getAllAssignments: string = environment.NEW_SERVER_URL + '/assignment/getAllAssignments';


export const AssignmentAPI = {
    createAssignment: createAssignment,
    getAllAssignments: getAllAssignments
}