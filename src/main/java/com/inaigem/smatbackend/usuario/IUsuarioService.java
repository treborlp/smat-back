package com.inaigem.smatbackend.usuario;

import com.inaigem.smatbackend.generic.ICRUD;

public interface IUsuarioService extends ICRUD<Usuario, Integer> {
    Usuario findUsuarioByUsername(String username);
}
