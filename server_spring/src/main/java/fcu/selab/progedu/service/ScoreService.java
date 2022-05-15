package fcu.selab.progedu.service;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@RequestMapping(value ="/score")
public class ScoreService {

  private static ScoreService instance = new ScoreService();
  public static ScoreService getInstance() {
    return instance;
  }

  @GetMapping(
      value ="getScoreCsvFile",
      produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
  )
  public ResponseEntity<Object> getScoreCsvFile() throws Exception{

    HttpHeaders headers = new HttpHeaders();

    //
    headers.add("Content-Disposition", "attachment;filename=" + "ScoreTemplate.csv");

    InputStream targetStream = this.getClass().getResourceAsStream("/sample/ScoreTemplate.csv");


    byte[] file = IOUtils.toByteArray(targetStream);
    return new ResponseEntity<Object>(file, headers, HttpStatus.OK);

  }

}
