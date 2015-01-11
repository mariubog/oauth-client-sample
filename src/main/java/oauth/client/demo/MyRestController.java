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
	public String resultsAsynch() throws Exception {

		Future<Map> futureMap = oauthConnectionService.getAsynchronousResults(
				resourceUrl, Map.class, restTemplate);
		while (futureMap.isDone()) {
			Thread.sleep(10);
		}
		return "Results retrived: \n" + futureMap.get().toString();
	}

	@RequestMapping(value = "/results")
	@ResponseBody
	public String results() throws Exception {

		Map map = oauthConnectionService.getClientOnlyResults(resourceUrl,
				Map.class, clientOnlyrestTemplate);
		return "Results retrived: \n" + map.toString();
	}
}