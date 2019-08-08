package fcu.selab.progedu.service;

import fcu.selab.progedu.project.JavacAssignment;
import fcu.selab.progedu.project.MavenAssignment;
import fcu.selab.progedu.project.ProjectTypeEnum;
import fcu.selab.progedu.project.WebAssignment;

public class AssignmentTypeFactory {

  /**
   * getAssignmentType
   * 
   * @param assignmentType assignmentType
   */
  public static AssignmentTypeSelector getAssignmentType(String assignmentType) {
    ProjectTypeEnum assignmentTypeEnum = ProjectTypeEnum
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
