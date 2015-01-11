package oauth.client.demo;

import java.util.Map;
import java.util.concurrent.Future;

import oauth.client.demo.oauth.OauthConnectionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestController {

	@Autowired
	OauthConnectionService oauthConnectionService;
	@Value("${oauth.resource:http://localhost:8080/greeting}")
	private String resourceUrl;

	@RequestMapping(value = "/results")
	@ResponseBody
	public String home() throws Exception {

		Future<Map> futureMap = oauthConnectionService.getResults(resourceUrl,
				Map.class);
		while (futureMap.isDone()) {
			Thread.sleep(10);
		}

		return "Results retrived: \n" + futureMap.get().toString();
	}
}