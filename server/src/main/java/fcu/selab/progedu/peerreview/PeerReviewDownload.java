package fcu.selab.progedu.peerreview;

import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.service.PeerReviewService;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("peerReview")
public class PeerReviewDownload {

  private static final Logger LOGGER = LoggerFactory.getLogger(PeerReviewDownload.class);

  /**
   *
   */
  @GET
  @Path("sourceCode")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response getSourceCode(@QueryParam("username") String username,
                                @QueryParam("assignmentName") String assignmentName) {
    Response response = null;

    try {
      GitlabService gitlabService = GitlabService.getInstance();
      GitlabProject gitlabProject = gitlabService.getProject(username, assignmentName);
      GitlabAPI gitlabApi = gitlabService.getGitlab();
      byte[] buffer = gitlabApi.getFileArchive(gitlabProject);

      response = Response.ok().entity(buffer).type("application/tar.gz")
              .header("Content-Disposition", "inline; filename=\"hw.tar.gz\"").build();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      response = Response.serverError().entity(e.getMessage()).build();
    }

    return response;
  }

}
