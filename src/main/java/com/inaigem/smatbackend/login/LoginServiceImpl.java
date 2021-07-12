package com.inaigem.smatbackend.login;

import com.inaigem.smatbackend.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class LoginServiceImpl implements ILoginService{

	@Lazy
	@Autowired
	private BCryptPasswordEncoder encriptar;
	
	@Autowired
	private ILoginRepo repo;

	@Override
	public Usuario verificarNombreUsuario(String usuario) {
		return repo.verificarNombreUsuario(usuario);
	}

	@Override
	public void cambiarClave(String clave, String nombre) {
		repo.cambiarClave(encriptar.encode(clave), nombre);
	}



}
