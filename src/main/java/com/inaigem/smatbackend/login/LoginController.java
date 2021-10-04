package com.inaigem.smatbackend.login;

import java.util.*;

import com.inaigem.smatbackend.data.Data;
import com.inaigem.smatbackend.security.KeyCloakConfig;
import com.inaigem.smatbackend.token.IResetTokenService;
import com.inaigem.smatbackend.token.ResetToken;
import com.inaigem.smatbackend.usuario.IUsuarioService;
import com.inaigem.smatbackend.usuario.Usuario;
import com.inaigem.smatbackend.util.EmailUtil;
import com.inaigem.smatbackend.util.Mail;
import org.json.JSONException;
import org.json.JSONObject;

import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private ILoginService service;
	
	@Autowired	
	private IResetTokenService tokenService;
	
	@Autowired
	private EmailUtil emailUtil;

	
	/*@PostMapping(value = "/enviarCorreo", consumes = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Integer> enviarCorreo(@RequestBody String correo) throws Exception {
		int rpta = 0;

		Usuario us = service.verificarNombreUsuario(correo);
		if(us != null && us.getIdUsuario() > 0) {
			ResetToken token = new ResetToken();
			token.setToken(UUID.randomUUID().toString());
			token.setUser(us);
			token.setExpiracion(90);
			tokenService.guardar(token);
			
			Mail mail = new Mail();
			mail.setFrom("treborlp@gmail.com");
			mail.setTo(us.getUsername());
			mail.setSubject("RESTABLECER CONTRASEÑA  MEDIAPP");
			
			Map<String, Object> model = new HashMap<>();
			String url = "http://localhost:4200/reset-password/" + token.getToken();
			model.put("user", token.getUser().getUsername());
			model.put("resetUrl", url);
			mail.setModel(model);
			
			emailUtil.enviarMail(mail);
			
			rpta = 1;			
		}
		return new ResponseEntity<Integer>(rpta, HttpStatus.OK);
	}*/

	//KeyCloak enviar correo
	@PostMapping(value = "/enviarCorreo", consumes = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Integer> enviarCorreoKeycloak(@RequestBody String correo) throws Exception {
		UsersResource usersResource = KeyCloakConfig.getInstance().realm(KeyCloakConfig.realm).users();
		List<UserRepresentation> lista = usersResource.search(correo, true);
		boolean rpta = lista.isEmpty();

		if (!rpta) {
			//Si lista no vacia, significa que usuario existe, entonces enviar correo
			UserRepresentation user = lista.get(0);
			System.out.println( user);
 			usersResource.get(user.getId()).executeActionsEmail(Arrays.asList("UPDATE_PASSWORD")); //.resetPasswordEmail();
			return new ResponseEntity<Integer>(1, HttpStatus.OK);
		}
		return new ResponseEntity<Integer>(0, HttpStatus.OK);
	}

	//KeyCloak enviar correo
	@PostMapping(value = "/verificarCorreo", consumes = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Integer> verificarCorreoKeycloak(@RequestBody String correo) throws Exception {
		UsersResource usersResource = KeyCloakConfig.getInstance().realm(KeyCloakConfig.realm).users();
		List<UserRepresentation> lista = usersResource.search(correo, true);
		boolean rpta = lista.isEmpty();

		if (!rpta) {
			//Si lista no vacia, significa que usuario existe, entonces enviar correo
			UserRepresentation user = lista.get(0);
			usersResource.get(user.getId()).executeActionsEmail(Arrays.asList("VERIFY_EMAIL")); //.resetPasswordEmail();
			return new ResponseEntity<Integer>(1, HttpStatus.OK);
		}
		return new ResponseEntity<Integer>(0, HttpStatus.OK);
	}
	
	@GetMapping(value = "/verificar/{token}")
	public ResponseEntity<Integer> verificarToken(@PathVariable("token") String token) {
		int rpta = 0;
		try {
			if (token != null && !token.isEmpty()) {
				ResetToken rt = tokenService.findByToken(token);
				if (rt != null && rt.getId() > 0) {
					if (!rt.estaExpirado()) {
						rpta = 1;
					}
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<Integer>(rpta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Integer>(rpta, HttpStatus.OK);
	}

	
	@PostMapping(value = "/restablecer/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> restablecerClave(@PathVariable("token") String token, @RequestBody String clave) {
		int rpta = 0;
		try {
			JSONObject json = new JSONObject(clave);
			ResetToken rt = tokenService.findByToken(token);			
			service.cambiarClave(json.get("clave").toString(), rt.getUser().getUsername());
			tokenService.eliminar(rt);
			rpta = 1;
		} catch (Exception e) {
			return new ResponseEntity<Integer>(rpta,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Integer>(rpta,HttpStatus.OK);
	}

	// KeyCloaks
	@PostMapping("/logout")
	public void cerrarSesion(@RequestBody String correo) {
		UsersResource usersResource = KeyCloakConfig.getInstance().realm(KeyCloakConfig.realm).users();
		UserRepresentation user = usersResource.search(correo, true).get(0);
		usersResource.get(user.getId()).logout();

		//Cerrar sesion al iniciar y luego poder iniciar, con eso limito a 1 sesion activa, es decir mato a todos para permitir al nuevo
		//RealmResource realmResource = KeyCloakConfig.getInstance("").realm(KeyCloakConfig.realm).clients().get("mediapp-backend").getUserSessions(firstResult, maxResults)
	}


	
}
