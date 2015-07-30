package oauth.client.demo.config;

import oauth.client.demo.DemoApplicationUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

@Configuration
public class OauthClientConfig {

	@Autowired(required = false)
	ClientHttpRequestFactory clientHttpRequestFactory;

	/*
	 * ClientHttpRequestFactory is autowired and checked in case somewhere in
	 * your configuration you provided {@link ClientHttpRequestFactory}
	 * implementation Bean where you defined specifics of your connection, if
	 * not it is instantiated here with {@link SimpleClientHttpRequestFactory}
	 */
	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		if (clientHttpRequestFactory == null) {
			clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		}
		return clientHttpRequestFactory;
	}

	@Bean
	@Qualifier("myRestTemplate")
	public OAuth2RestOperations restTemplate(@Value("${oauth.token}") String tokenUrl) {

		OAuth2RestTemplate template = new OAuth2RestTemplate(fullAccessresourceDetails(tokenUrl), new DefaultOAuth2ClientContext(
				new DefaultAccessTokenRequest()));
		return prepareTemplate(template, false);
	}

	@Bean
	@Qualifier("myClientOnlyRestTemplate")
	public OAuth2RestOperations restClientOnlyTemplate(@Value("${oauth.token}") String tokenUrl) {

		OAuth2RestTemplate template = new OAuth2RestTemplate(fullAccessresourceDetailsClientOnly(tokenUrl), new DefaultOAuth2ClientContext(
				new DefaultAccessTokenRequest()));
		return prepareTemplate(template, true);
	}

	public OAuth2RestTemplate prepareTemplate(OAuth2RestTemplate template, boolean isClient) {
		template.setRequestFactory(getClientHttpRequestFactory());
		if (isClient) {
			template.setAccessTokenProvider(clientAccessTokenProvider());
		} else {
			template.setAccessTokenProvider(userAccessTokenProvider());
		}
		return template;
	}

	/**
	 * {@link AccessTokenProviderChain} throws
	 * InsufficientAuthenticationException in
	 * obtainAccessToken(OAuth2ProtectedResourceDetails resource,
	 * AccessTokenRequest request) if user is not authorized, but since we are
	 * setting our own accessTokenProvider() on OAuth2RestTemplate this
	 * condition is not being checked, thus exception is not being thrown and
	 * requirement for user to be logged in is skipped
	 */
	@Bean
	public AccessTokenProvider userAccessTokenProvider() {
		ResourceOwnerPasswordAccessTokenProvider accessTokenProvider = new ResourceOwnerPasswordAccessTokenProvider();
		accessTokenProvider.setRequestFactory(getClientHttpRequestFactory());
		return accessTokenProvider;
	}

	@Bean
	public AccessTokenProvider clientAccessTokenProvider() {
		ClientCredentialsAccessTokenProvider accessTokenProvider = new ClientCredentialsAccessTokenProvider();
		accessTokenProvider.setRequestFactory(getClientHttpRequestFactory());
		return accessTokenProvider;
	}

	@Bean
	@Qualifier("myFullAcessDetails")
	public OAuth2ProtectedResourceDetails fullAccessresourceDetails(String tokenUrl) {
		ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
		resource.setAccessTokenUri(tokenUrl);
		resource.setClientId("user_member");
		resource.setGrantType("password");
		resource.setScope(DemoApplicationUtils.getScopesList("read", "write"));
		resource.setUsername("roy");
		resource.setPassword("spring");
		return resource;
	}

	@Bean
	@Qualifier("myClientOnlyFullAcessDetails")
	public OAuth2ProtectedResourceDetails fullAccessresourceDetailsClientOnly(String tokenUrl) {
		ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
		resource.setAccessTokenUri(tokenUrl);
		resource.setClientId("clientapp");
		resource.setClientSecret("123456");
		resource.setGrantType("client_credentials");
		resource.setScope(DemoApplicationUtils.getScopesList("read", "write"));
		return resource;
	}
}
