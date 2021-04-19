package fcu.selab.progedu.service;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class PoweredByResponseFilter implements ContainerResponseFilter {

  @Override
  public void filter(ContainerRequestContext requestContext,
                     ContainerResponseContext responseContext)
          throws IOException {
//    responseContext.getHeaders().add("X-Powered-By", "Jersey :-)");
    responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");

//    responseContext.getHeaders().add("Access-Control-Expose-Headers",
//            "authorization");

    responseContext.getHeaders().add("Access-Control-Allow-Methods",
            "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    responseContext.getHeaders().add("Access-Control-Max-Age", "1209600");

    System.out.println("franky-test PoweredByResponseFilter over");
  }
}
