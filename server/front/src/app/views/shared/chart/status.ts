export const Status = {
  initial: {
    name: 'Initial',
    color: '#878787',
    status: ['INI']
  },
  compilerFailure: {
    name: 'Compiler Failure',
    color: '#ff6284',
    status: ['CPF']
  },
  codingStyleError: {
    name: 'Coding Style Failure',
    color: '#ffc229',
    status: ['CSF', 'WEF', 'WHF', 'WSF']
  },
  testFailure: {
    name: 'Test Failure',
    color: '#26cedf',
    status: ['UTF']
  },
  success: {
    name: 'Success',
    color: '#1e90ff',
    status: ['BS']
  }
};
export const Color = {
  bubble: 'rgba(30, 144, 255, 0.65)'
}
