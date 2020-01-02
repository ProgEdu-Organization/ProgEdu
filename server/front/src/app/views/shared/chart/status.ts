export const Status = {
    notBuild: {
      name: 'Not Build',
      color: '#878787',
      status: ['INI']
    },
    compilerFailure: {
      name: 'Compiler Failure',
      color: '#ff6284',
      status: ['CPF']
    },
    codingStyleError: {
      name: 'Coding Style Error',
      color: '#ffc229',
      status: ['CSF','WEF','WHF','WSF']
    },
    testFailure: {
      name: 'Test Failure',
      color: '#26cedf',
      status: ["UTF"]
    },
    success: {
      name: 'Success',
      color: '#1e90ff',
      status: ['BS']
    }
  };
export const Color = {
  bubble: 'rgba(30, 144, 255, 1)'
}