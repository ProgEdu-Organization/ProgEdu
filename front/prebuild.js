const fs = require('fs');
const path = require('path');


let environment_file_path = path.join('src', 'environments', 'environment.ts');

if (fs.existsSync(environment_file_path) == false) { //Copy prod env to create env 
  let environment_prod_file_path = path.join('src', 'environments', 'environment.prod.ts');
  fs.copyFileSync(environment_prod_file_path, environment_file_path);
}
  
