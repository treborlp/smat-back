package com.inaigem.smatbackend.security;

import com.inaigem.smatbackend.usuario.IUsuarioService;
import com.inaigem.smatbackend.usuario.Usuario;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserInfoToken implements TokenEnhancer {

    @Autowired
    private IUsuarioService usuarioService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        Usuario usuario = usuarioService.findUsuarioByUsername(authentication.getName());

        Map<String, Object> data = new HashMap<>();

            data.put("id", usuario.getIdUsuario());
            data.put("name", usuario.getFullName());
            data.put("avatar", usuario.getAvatar());
            data.put("status", usuario.getStatus());
            data.put("email", authentication.getName());


        Map<String, Object> info = new HashMap<>();
        info.put("user",data);

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);

        return accessToken;
    }
}
