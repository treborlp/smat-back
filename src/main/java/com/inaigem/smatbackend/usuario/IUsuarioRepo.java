package com.inaigem.smatbackend.usuario;

import com.inaigem.smatbackend.generic.IGenericRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioRepo extends IGenericRepo<Usuario, Integer> {

    //select * from usuario where username = ?
    Usuario findOneByUsername(String username);
}
