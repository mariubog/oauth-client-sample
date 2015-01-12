# oauth-client-sample
Sample Ouath2 client app connecting to REST service secured with Oauth2.


It connects to the sample service creted by royclarkson. You can find it here https://github.com/royclarkson/spring-rest-service-oauth.
Royclarkson service is runing on port 8080 and this app is runing on port 8005 which you can change according to your needs so it does not colide with other services.


It is basic spring boot application that can be started with 
`mvn clean package spring-boot:run`

Instructions how to run RESTful service  protected by OAuth 2 required for this client to interact with are posted here.

https://github.com/royclarkson/spring-rest-service-oauth.



Both applications obviously have to be run simultanously. 


