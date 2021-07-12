package com.inaigem.smatbackend.login;

import com.inaigem.smatbackend.generic.IGenericRepo;
import com.inaigem.smatbackend.usuario.Usuario;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface ILoginRepo extends IGenericRepo<Usuario, Integer> {

	@Query("FROM Usuario us where us.username =:usuario")
	Usuario verificarNombreUsuario(@Param("usuario")String usuario);
	
	@Transactional
	@Modifying
	@Query("UPDATE Usuario us SET us.password =:clave WHERE us.username =:nombre")
	void cambiarClave(@Param("clave") String clave, @Param("nombre") String nombre);
	
}
