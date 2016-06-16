# oauth-client-sample
#####Sample Ouath2 client app connecting to REST service secured with Oauth2.


It connects to the sample service created by royclarkson. You can find it here https://github.com/royclarkson/spring-rest-service-oauth.
Royclarkson service is runing on port 8080 and this app is runing on port 8005 which you can change according to your needs so it does not colide with other services.

!!!  IMPORTANT !!!

Afte I made changes in order to add clientOnly template authentication   it does not work properly with above Roys' example, I made proper changes in my fork for his project and this example suppoesed to be used in order
to get desirable results
https://github.com/mariubog/spring-rest-service-oauth
If I have a chance and Roy agrees I ll try to pull request for these changes in his example, but for now please just use my fork. Otherwise it will not work unless you make changes to comply with his authentication settings.
 
It is basic spring boot application that can be started with 
`mvn clean package spring-boot:run`

Instructions how to run RESTful service  protected by OAuth 2 required for this client to interact with are posted here.

https://github.com/royclarkson/spring-rest-service-oauth.



<i>Both applications obviously have to be run simultanously. </i>

After starting application you can use following links either with curl or simple web browser.
  
Link to access resource protected by Oauth with not synchronazed method, uri does NOT require login on client side.
Resource is acquired.

`http://localhost:8005/results-asynch`





Link to access resource protected by Oauth , uri does NOT require login on client side.
Resource is not acquired.Authorization is required.(If acces token had been acquired earlier this method completes and returns desired results)

`http://localhost:8005/results-nonauthorized`






Link to access resource protected by Oauth using ResourceDetails clientOnly() == true, uri does NOT require login on client side.
Resource is acquired.

`http://localhost:8005/results`






Link to access resource protected by Oauth with synchronazed method, uri  REQUIRES login on client side.
Resource is acquired after authentication.

`http://localhost:8005/authorized-results`




