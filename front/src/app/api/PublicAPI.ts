import { environment } from '../../environments/environment';

let getScreenshotURL: string = environment.NEW_SERVER_URL + '/publicApi/commits/screenshot/getScreenshotURL';
let getScreenshotURLofGroup: string = environment.NEW_SERVER_URL + '/publicApi/groups/commits/screenshot/getScreenshotURL';



export const PublicAPI = {
    getScreenshotURL: getScreenshotURL,
    getScreenshotURLofGroup: getScreenshotURLofGroup
}