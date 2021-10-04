package com.inaigem.smatbackend.security;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

public class KeyCloakConfig {
	public static Keycloak keycloak = null;
	public final static String serverUrl = "http://localhost:9999/auth";
	public final static String realm = "smatapp";
	public final static String clientId = "smat-backend";
	public final static String clientSecret = ""; //necesario en confidencial
	public final static String userName = "treborlp@gmail.com"; //Usuario root o con privilegios necesarios si quiero uno fijo
	public final static String password = "123";

	public KeyCloakConfig() {
	}

	public static Keycloak getInstance(){
        if(keycloak == null){
           
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)                    
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(userName)
                    .password(password)
                    .clientId(clientId)                                      
                    //.authorization("authorization")                    
                    //.clientSecret(clientSecret)
                    .resteasyClient(new ResteasyClientBuilder()
                                   .connectionPoolSize(10)
                                   .build()
                                   )
                    .build();
        }
        return keycloak;
    }
}
