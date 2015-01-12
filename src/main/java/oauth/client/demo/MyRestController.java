package oauth.client.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletResponse;

import oauth.client.demo.service.OauthConnectionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("rawtypes")
@RestController
public class MyRestController {
	@Autowired
	@Qualifier("myRestTemplate")
	private OAuth2RestOperations restTemplate;
	@Autowired
	@Qualifier("myClientOnlyRestTemplate")
	private OAuth2RestOperations clientOnlyrestTemplate;
	@Autowired
	OauthConnectionService oauthConnectionService;

	@Value("${oauth.resource.greeting}")
	private String resourceUrl;

	/*
	 * In this method if token is not obtained exception is ***NOT*** thrown and
	 * access token is obtrained by template. It bypasses requirement for
	 * associating request with authenticated user ? *
	 */
	@RequestMapping(value = "/results-asynch")
	@ResponseBody
	public Map resultsAsynch(HttpServletResponse response) throws Exception {
		try {
			Future<Map> futureMap = oauthConnectionService
					.getAsynchronousResults(resourceUrl, Map.class,
							restTemplate);
			while (futureMap.isDone()) {
				Thread.sleep(10);
			}
			return futureMap.get();
		} catch (InsufficientAuthenticationException e) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * If token is not obtained exception is thrown
	 * InsufficientAuthenticationException This endpoint does not require
	 * authentication
	 */
	@RequestMapping(value = "/results-nonauthorized")
	@ResponseBody
	public Map nonAuthorizedResultsLoginNotRequired(HttpServletResponse response)
			throws Exception {
		Map results = null;
		try {
			results = oauthConnectionService.getResults(resourceUrl, Map.class,
					restTemplate);
		} catch (InsufficientAuthenticationException e) {
			results = null;
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			e.printStackTrace();
		}
		return results;
	}

	/*
	 * Template used has clientOnly() method returning true so user
	 * authorization is not necessary. Uses {@link
	 * ClientOnlyResourceOwnerPasswordResourceDetails} This endpoint does not
	 * require authentication
	 */
	@RequestMapping(value = "/results")
	@ResponseBody
	public Map results() throws Exception {
		return oauthConnectionService.getClientOnlyResults(resourceUrl,
				Map.class, clientOnlyrestTemplate);
	}

	/*
	 * User is being redirected to login page for required authorization This
	 * endpoint REQUIRES authentication
	 */
	@RequestMapping(value = "/authorized-results")
	@ResponseBody
	public Map authorized() throws Exception {
		return oauthConnectionService.getResults(resourceUrl, Map.class,
				restTemplate);
	}

}