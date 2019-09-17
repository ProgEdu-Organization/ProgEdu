const fs = require('fs');
const path = require('path');

if (fs.existsSync(path.join(
  'src', 
  'environments', 
  'environment.ts'
)) == false)
  fs.copyFileSync(path.join(
    'src', 
    'environments', 
    'environment.prod.ts'
  ), path.join(
    'src', 
    'environments', 
    'environment.ts'
  ));
