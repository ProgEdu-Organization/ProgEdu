package fcu.selab.progedu.project;

public class AssignmentFactory {

  /**
   * getAssignmentType
   * 
   * @param projectType assignmentType
   */
  public static AssignmentType getAssignmentType(String projectType) {
    ProjectTypeEnum projectTypeEnum = ProjectTypeEnum.getProjectTypeEnum(projectType);

    switch (projectTypeEnum) {
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
