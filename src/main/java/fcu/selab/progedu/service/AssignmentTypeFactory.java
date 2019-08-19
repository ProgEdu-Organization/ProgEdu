package fcu.selab.progedu.service;

public class AssignmentTypeFactory {

  /**
   * getAssignmentType
   * 
   * @param assignmentType assignmentType
   */
  public static AssignmentTypeSelector getAssignmentType(String assignmentType) {
    AssignmentTypeEnum assignmentTypeEnum = AssignmentTypeEnum
        .getStatusProjectTypeEnum(assignmentType);

    switch (assignmentTypeEnum) {
      case JAVAC: {
        return new JavacAssignment();
      }
      case MAVEN: {
        return new MavenAssignment();
      }
      case WEB: {
        return new WebAssignment();
      }
      case APP: {
        return null;
      }
      default:
        return null;
    }

  }
}
