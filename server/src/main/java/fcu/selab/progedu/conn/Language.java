package fcu.selab.progedu.conn;

public class Language {

  private static final String CHINESE = "zh";
  private static final String ENGLISH = "en";

  private static final String FORM_TW = "form_tw";
  private static final String FORM_EN = "form_en";

  /**
   * Get the language basename
   * 
   * @param language The chosen language
   * @return basename
   */
  public String getBaseName(String language) {
    String basename = null;
    if (language.equals(CHINESE)) {
      basename = FORM_TW;
    } else if (language.equals(ENGLISH)) {
      basename = FORM_EN;
    }
    return basename;
  }
}