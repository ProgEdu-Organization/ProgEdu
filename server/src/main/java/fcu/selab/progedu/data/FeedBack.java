package fcu.selab.progedu.data;

import fcu.selab.progedu.status.StatusEnum;

public class FeedBack {

  private StatusEnum statusEnum;

  private String line;

  private String message;

  private String symptom;

  private String suggest;

  public  FeedBack(StatusEnum style, String line, String message, String symptom, String suggest) {
    this.statusEnum = style;
    this.line = line;
    this.message = message;
    this.symptom = symptom;
    this.suggest = suggest;
  }

  public StatusEnum getStyle() {
    return this.statusEnum;
  }

  public void setStyle(StatusEnum statusEnum) {
    this.statusEnum = statusEnum;
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

  public String getSymptom() {
    return this.symptom;
  }

  public void setSymptom(String symptom) {
    this.symptom = symptom;
  }

  public String getSuggest() {
    return this.suggest;
  }

  public void setSuggest(String suggest) {
    this.suggest = suggest;
  }

}
