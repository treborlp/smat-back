package com.inaigem.smatbackend.usuario;

import com.inaigem.smatbackend.exception.ModeloNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping("/{email}")
    public ResponseEntity<IUsuarioView> viewUsuario(@PathVariable("email") String email) throws Exception {
        IUsuarioView user = usuarioService.usuarioView(email);

        if(user.getId() == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO " + email);
        }
        return new ResponseEntity<IUsuarioView>(user, HttpStatus.OK);
    }
}
