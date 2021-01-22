package fcu.selab.progedu.project;

public class ProjectTypeFactory {

  /**
   * get projectType
   *
   * @param projectType assignmentType
   */
  public static ProjectType getProjectType(String projectType) {
    ProjectTypeEnum projectTypeEnum = ProjectTypeEnum.getProjectTypeEnum(projectType);

    switch (projectTypeEnum) {
      case JAVAC:
        return new JavacAssignment();
      case MAVEN:
        return new MavenAssignment();
      case WEB:
        return new WebAssignment();
      case ANDROID:
        return new AndroidAssignment();
      default:
        return null;
    }
  }
}
