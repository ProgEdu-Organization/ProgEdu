export interface EmitStudentEvent {
    emitStudentEvent(event: StudentEvent);
  }

export interface StudentEvent {
  page: string;
  name: string;
  event: any;
}
