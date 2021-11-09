import { environment } from '../../environments/environment';

let getCategory: string = environment.NEW_SERVER_URL + '/categoryMetrics/category';
let createCategory: string = environment.NEW_SERVER_URL + '/categoryMetrics/category/create';
let editCategory: string = environment.NEW_SERVER_URL + '/categoryMetrics/category/edit';
let deleteCategory: string = environment.NEW_SERVER_URL + '/categoryMetrics/category/delete';


let getMetrics: string = environment.NEW_SERVER_URL + '/categoryMetrics/metrics';
let createMetrics: string = environment.NEW_SERVER_URL + '/categoryMetrics/metrics/create';
let editMetrics: string = environment.NEW_SERVER_URL + '/categoryMetrics/metrics/edit';
let deleteMetrics: string = environment.NEW_SERVER_URL + '/categoryMetrics/metrics/delete';


export const CategoryMetricsAPI = {
    getCategory: getCategory,
    getMetrics: getMetrics,
    createCategory: createCategory,
    editCategory: editCategory,
    deleteCategory: deleteCategory,
    createMetrics: createMetrics,
    editMetrics: editMetrics,
    deleteMetrics: deleteMetrics
}