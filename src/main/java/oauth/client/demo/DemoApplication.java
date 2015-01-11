package oauth.client.demo;

import oauth.client.demo.config.ClientOnlyResourceOwnerPasswordResourceDetails;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:resource.properties")
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

	}

	@Bean
	@Qualifier("myRestTemplate")
	public OAuth2RestOperations restTemplate(
			@Value("${oauth.token}") String tokenUrl) {
		AccessTokenRequest atr = new DefaultAccessTokenRequest();
		return new OAuth2RestTemplate(fullAccessresourceDetails(tokenUrl),
				new DefaultOAuth2ClientContext(atr));
	}

	@Bean
	@Qualifier("myClientOnlyRestTemplate")
	public OAuth2RestOperations restClientOnlyTemplate(
			@Value("${oauth.token}") String tokenUrl) {
		AccessTokenRequest atr = new DefaultAccessTokenRequest();
		return new OAuth2RestTemplate(
				fullAccessresourceDetailsClientOnly(tokenUrl),
				new DefaultOAuth2ClientContext(atr));
	}

	@Bean
	@Qualifier("myFullAcessDetails")
	protected OAuth2ProtectedResourceDetails fullAccessresourceDetails(
			String tokenUrl) {
		ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
		resource.setAccessTokenUri(tokenUrl);
		resource.setClientId("clientapp");
		resource.setClientSecret("123456");
		resource.setGrantType("password");
		resource.setScope(DemoApplicationUtils.getScopesList("read", "write"));
		resource.setUsername("roy");
		resource.setPassword("spring");
		return resource;
	}

	@Bean
	@Qualifier("myClientOnlyFullAcessDetails")
	protected OAuth2ProtectedResourceDetails fullAccessresourceDetailsClientOnly(
			String tokenUrl) {
		// using overriden
		// OnlyResourceOwnerPasswordResourceDetails.isClientOnly()
		ClientOnlyResourceOwnerPasswordResourceDetails resource = new ClientOnlyResourceOwnerPasswordResourceDetails();
		resource.setAccessTokenUri(tokenUrl);
		resource.setClientId("clientapp");
		resource.setClientSecret("123456");
		resource.setGrantType("password");
		resource.setScope(DemoApplicationUtils.getScopesList("read", "write"));
		resource.setUsername("roy");
		resource.setPassword("spring");
		return resource;
	}

}
