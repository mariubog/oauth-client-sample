package oauth.client.demo.oauth;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;

@Service
public class OauthConnectionService {
	@Autowired
	@Qualifier("myRestTemplate")
	private OAuth2RestOperations restTemplate;

	@Async
	public <T> Future<T> getResults(String resourceUrl, Class<T> resultType) {
		return new AsyncResult<T>(getForObject(resourceUrl, resultType));
	}

	protected<T> T getForObject(String resourceUrl, Class<T> responseType) {
		return restTemplate.getForObject(resourceUrl, responseType);
	}
}