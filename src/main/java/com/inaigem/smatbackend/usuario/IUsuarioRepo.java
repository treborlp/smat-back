package com.inaigem.smatbackend.usuario;

import com.inaigem.smatbackend.generic.IGenericRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioRepo extends IGenericRepo<Usuario, Integer> {

    //select * from usuario where username = ?
    Usuario findOneByUsername(String username);

    @Query("SELECT u.idUsuario as id, u.fullName as name, u.username as email, u.avatar as avatar, u.status as status from  Usuario u where u.username=?1")
    IUsuarioView viewUsuario(String email);
}
