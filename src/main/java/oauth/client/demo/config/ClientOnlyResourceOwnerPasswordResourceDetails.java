package oauth.client.demo.config;

import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

public class ClientOnlyResourceOwnerPasswordResourceDetails extends
		ResourceOwnerPasswordResourceDetails {
	@Override
	public boolean isClientOnly() {
		return true;
	}
}
