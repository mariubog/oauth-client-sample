package oauth.client.demo.config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/* This is only for testing purposes.  DO NOT USE FOR PRODUCTION !!!
 * It guarantees ssl handshake with unverified server with self created certificate
 * If you are trying to connect to Oauth server 
 * that you started on your localhost operating on https 
 * and using self-signed certificate you have to uncomment below annotations 
 */
@Configuration
@Profile("https_connection")
public class AcceptAllHttpsConfig {

	@Bean
	public HostnameVerifier hostnameVerifier() {
		return new javax.net.ssl.HostnameVerifier() {

			public boolean verify(String hostname,
					javax.net.ssl.SSLSession sslSession) {
				if (hostname.equals("localhost")) {
					return true;
				}
				return false;
			}
		};
	}

	@Bean
	public ClientHttpRequestFactory clientHttpRequestFactory(
			HostnameVerifier hostNameVerifier) {
		ClientHttpRequestFactory clientHttpRequestFactory = new ClientHttpsAcceptLoaclahostRequestFactory(
				hostNameVerifier);
		return clientHttpRequestFactory;
	}

	class ClientHttpsAcceptLoaclahostRequestFactory extends
			SimpleClientHttpRequestFactory {

		private final HostnameVerifier hostNameVerifier;

		@Autowired
		public ClientHttpsAcceptLoaclahostRequestFactory(
				HostnameVerifier hostNameVerifier) {
			this.hostNameVerifier = hostNameVerifier;
		}

		@Override
		protected void prepareConnection(final HttpURLConnection connection,
				final String httpMethod) throws IOException {
			if (connection instanceof HttpsURLConnection) {

				((HttpsURLConnection) connection)
						.setHostnameVerifier(hostNameVerifier);
				((HttpsURLConnection) connection)
						.setSSLSocketFactory(initSSLContext()
								.getSocketFactory());
			}
			super.prepareConnection(connection, httpMethod);
		}

		private SSLContext initSSLContext() {
			try {
				System.setProperty("https.protocols", "TLSv1");
				SSLContext sc = SSLContext.getInstance("TLSv1");
				sc.init(null, getTrustManager(), null);

				return sc;
			} catch (final Exception ex) {
				return null;
			}
		}

		private TrustManager[] getTrustManager() {
			return new TrustManager[] { new X509TrustManager() {

				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}

				public void checkClientTrusted(
						java.security.cert.X509Certificate[] certs,
						String authType) {
				}

				public void checkServerTrusted(
						java.security.cert.X509Certificate[] certs,
						String authType) {

				}
			} };
		}
	}
}
