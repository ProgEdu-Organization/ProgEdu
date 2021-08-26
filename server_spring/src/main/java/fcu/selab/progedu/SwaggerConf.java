package fcu.selab.progedu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConf {

  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("fcu.selab.progedu.service"))
            .paths(PathSelectors.any())//paths(PathSelectors.regex("/.*"))
            .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
            .title("ProgEdu APIs")
            .version("1.0.0")
            .build();
  }
}
