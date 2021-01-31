package fcu.selab.progedu.status;

import fcu.selab.progedu.data.ProjectTypeEnum;

public class StatusAnalysisFactory {

// ProjectTypeEnum
//    JAVAC("javac"),
//    MAVEN("maven"),
//    WEB("web"),
//    ANDROID("android");

// StatusEnum
//    BUILD_SUCCESS("bs"), CHECKSTYLE_FAILURE("csf"), COMPILE_FAILURE("cpf"), INITIALIZATION("ini"),
//    UNIT_TEST_FAILURE("utf"), UI_TEST_FAILURE("uitf"), WEB_HTMLHINT_FAILURE("whf"),
//    WEB_STYLELINT_FAILURE("wsf"), WEB_ESLINT_FAILURE("wef"), ANDROID_LINT_FAILURE("alf"),
//    COMPILE_FAILURE_OF_UNIT_TEST("cpfout");

  /**
   * getStatusAnalysis
   * @param projectTypeEnum projectTypeEnum
   * @param statusType  StatusEnum string
   * @return Status
   */
  public static Status getStatusAnalysis(ProjectTypeEnum projectTypeEnum, String statusType) {

    switch (projectTypeEnum) {
      case JAVAC:
        JavacStatusFactory javacStatusFactory = new JavacStatusFactory();
        return javacStatusFactory.getStatus(statusType);

      case MAVEN:
        MavenStatusFactory mavenStatusFactory = new MavenStatusFactory();
        return mavenStatusFactory.getStatus(statusType);

      case WEB:
        WebStatusFactory webStatusFactory = new WebStatusFactory();
        return webStatusFactory.getStatus(statusType);

      case ANDROID:
        AndroidStatusFactory androidStatusFactory = new AndroidStatusFactory();
        return androidStatusFactory.getStatus(statusType);

      default: {
        return null;
      }
    }
  }
}
