package oauth.client.demo.service;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

@Service
public class OauthConnectionServiceImpl implements OauthConnectionService {

	@Async
	public <T> Future<T> getAsynchronousResults(String resourceUrl,
			Class<T> resultType, RestOperations restTemplate) {
		return new AsyncResult<T>(getForObject(resourceUrl, resultType,
				restTemplate));
	}

	@Override
	public <T> T getClientOnlyResults(String resourceUrl, Class<T> resultType,
			RestOperations clientOnlyrestTemplate) {
		return getForObject(resourceUrl, resultType, clientOnlyrestTemplate);
	}

	@Override
	public <T> T getResults(String resourceUrl, Class<T> resultType,
			RestOperations restTemplate) {
		return getForObject(resourceUrl, resultType, restTemplate);
	}

}