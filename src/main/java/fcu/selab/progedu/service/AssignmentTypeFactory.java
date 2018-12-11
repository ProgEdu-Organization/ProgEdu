package fcu.selab.progedu.service;

public class AssignmentTypeFactory {

  /**
   * getAssignmentType
   * 
   * @param assignmentType
   *          assignmentType
   */
  public static AssignmentTypeSelector getAssignmentType(String assignmentType) {

    if (assignmentType.equals("Javac")) {
      return new JavacAssignment();
    } else if (assignmentType.equals("Maven")) {
      return new MavenAssignment();
    } else if (assignmentType.equals("Web")) {
      return new WebAssignment();
    } else {
      return null;
    }
  }
}
