package oauth.client.demo;

import java.util.Map;
import java.util.concurrent.Future;

import oauth.client.demo.service.OauthConnectionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

	@RequestMapping(value = "/results-asynch")
	@ResponseBody
	public Map resultsAsynch() throws Exception {

		Future<Map> futureMap = oauthConnectionService.getAsynchronousResults(
				resourceUrl, Map.class, restTemplate);
		while (futureMap.isDone()) {
			Thread.sleep(10);
		}
		return futureMap.get();
	}

	@RequestMapping(value = "/results")
	@ResponseBody
	public Map results() throws Exception {

		Map results = oauthConnectionService.getClientOnlyResults(resourceUrl,
				Map.class, clientOnlyrestTemplate);
		return results;
	}

	@RequestMapping(value = "/authorized-results")
	@ResponseBody
	public Map authorized() throws Exception {
		Map results = oauthConnectionService.getResults(resourceUrl, Map.class,
				restTemplate);

		return results;
	}
}