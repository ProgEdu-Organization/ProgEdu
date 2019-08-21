const fs = require('fs');
if (fs.existsSync('src\\environments\\environment.ts') == false)
  fs.copyFileSync('src\\environments\\environment.prod.ts', 'src\\environments\\environment.ts');
