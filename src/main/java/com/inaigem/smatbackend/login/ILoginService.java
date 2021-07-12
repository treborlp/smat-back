package com.inaigem.smatbackend.login;


import com.inaigem.smatbackend.usuario.Usuario;

public interface ILoginService {

	Usuario verificarNombreUsuario(String usuario);
	void cambiarClave(String clave, String nombre);
}
