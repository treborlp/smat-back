package com.inaigem.smatbackend.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.inaigem.smatbackend.data.Data;
import com.inaigem.smatbackend.token.IResetTokenService;
import com.inaigem.smatbackend.token.ResetToken;
import com.inaigem.smatbackend.usuario.IUsuarioService;
import com.inaigem.smatbackend.usuario.Usuario;
import com.inaigem.smatbackend.util.EmailUtil;
import com.inaigem.smatbackend.util.Mail;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

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

	
	@PostMapping(value = "/enviarCorreo", consumes = MediaType.TEXT_PLAIN_VALUE)
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


	
}
