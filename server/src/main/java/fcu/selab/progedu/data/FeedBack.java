package fcu.selab.progedu.data;

public class FeedBack {

  private String style;

  private String line;

  private String message;

  private String errorStyle;

  private String suggest;

  /**
   *
   * @param style (To Do)
   * @param line  (To Do)
   * @param message (To Do)
   * @param errorStyle (To Do)
   * @param suggest (To Do)
   */
  public  FeedBack(String style, String line, String message, String errorStyle, String suggest) {
    this.style = style;
    this.line = line;
    this.message = message;
    this.errorStyle = errorStyle;
    this.suggest = suggest;
  }

  public String getStyle() {
    return this.style;
  }

  public void setStyle(String style) {
    this.style = style;
  }

  public String getLine() {
    return this.line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getErrorStyle() {
    return this.errorStyle;
  }

  public void setErrorStyle(String errorStyle) {
    this.errorStyle = errorStyle;
  }

  public String getSuggest() {
    return this.suggest;
  }

  public void setSuggest(String suggest) {
    this.suggest = suggest;
  }

}
