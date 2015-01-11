package oauth.client.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableOAuth2Client
@EnableAsync
public class DemoApplication {

	@Value("${oauth.resource:http://localhost:8080}")
	private String baseUrl;
	@Value("${oauth.authorize:http://localhost:8080/oauth/authorize}")
	private String authorizeUrl;
	@Value("${oauth.token:http://localhost:8080/oauth/token}")
	private String tokenUrl;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

	}

	@Bean
	@Qualifier("myRestTemplate")
	public OAuth2RestOperations restTemplate() {
		AccessTokenRequest atr = new DefaultAccessTokenRequest();

		return new OAuth2RestTemplate(fullAccessresourceDetails(),
				new DefaultOAuth2ClientContext(atr));
	}

	@Bean
	@Qualifier("myFullAcessDetails")
	protected OAuth2ProtectedResourceDetails fullAccessresourceDetails() {
		ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
		List<String> scopes = new ArrayList<String>(2);
		scopes.add("write");
		scopes.add("read");
		resource.setAccessTokenUri(tokenUrl);
		resource.setClientId("clientapp");
		resource.setClientSecret("123456");
		resource.setGrantType("password");
		resource.setScope(scopes);
		resource.setUsername("roy");
		resource.setPassword("spring");
		return resource;
	}
}
