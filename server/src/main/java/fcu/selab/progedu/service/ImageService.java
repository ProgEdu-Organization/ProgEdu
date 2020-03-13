package fcu.selab.progedu.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.sql.Timestamp;
import fcu.selab.progedu.conn.TomcatService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONObject;

@Path("image/")
public class ImageService {
  private TomcatService tomcatService = TomcatService.getInstance();
  private final String projectDir = System.getProperty("catalina.base");
  private final String imageTempName = "/temp_images/";
  private final String imageTempDir = projectDir + imageTempName;

  public ImageService() {
  }

  /**
   * @param file       abc
   * @param fileDetail abc
   * @throws Exception abc
   */
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public Response uploadImageToTemp(
      @FormDataParam("upload") InputStream file,
      @FormDataParam("upload") FormDataContentDisposition fileDetail,
      @FormDataParam("ckCsrfToken") String token) {

    // Add timestamp to rename image
    Timestamp ts = new Timestamp(System.currentTimeMillis());
    String fileName = fileDetail.getFileName().replace(".PNG", "")
        .replace(".png", "") + "_" + ts.getTime() + ".PNG";
    // Store image to temp_images folder
    tomcatService.storeDescriptionImage(imageTempDir, fileName, file);
    /*
    CKEditor Response
    https://ckeditor.com/docs/ckeditor4/latest/guide/dev_file_upload.html
    */
    JSONObject ob = new JSONObject();
    ob.put("uploaded", 1);
    ob.put("fileName", fileName);
    ob.put("url", imageTempName + fileName);
    return Response.ok(ob.toString()).build();
  }

}
