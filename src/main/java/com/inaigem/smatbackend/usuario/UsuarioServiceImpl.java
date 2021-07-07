package com.inaigem.smatbackend.usuario;

import java.util.ArrayList;
import java.util.List;

import com.inaigem.smatbackend.generic.CRUDImpl;
import com.inaigem.smatbackend.generic.IGenericRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import com.inaigem.smatbackend.usuario.Usuario;
import com.inaigem.smatbackend.usuario.IUsuarioRepo;

@Service
public class UsuarioServiceImpl extends CRUDImpl<Usuario, Integer> implements IUsuarioService, UserDetailsService{

	@Autowired
	private IUsuarioRepo repo;	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuario usuario = repo.findOneByUsername(username);
		
		if(usuario == null) {
			throw new UsernameNotFoundException(String.format("Usuario no existe", username));
		}
		
		List<GrantedAuthority> roles = new ArrayList<>();
		
		usuario.getRoles().forEach(rol -> {
			roles.add(new SimpleGrantedAuthority(rol.getNombre()));
		});
		
		UserDetails ud = new User(usuario.getUsername(), usuario.getPassword(), usuario.isEnabled(), true, true, true, roles);
		
		return ud;
	}

	@Override
	protected IGenericRepo<Usuario, Integer> getRepo() {
		return repo;
	}

	@Override
	public Usuario findUsuarioByUsername(String username) {
		Usuario usuario = repo.findOneByUsername(username);

		if(usuario == null) {
			throw new UsernameNotFoundException(String.format("Usuario no existe", username));
		}

		return usuario;
	}
}
