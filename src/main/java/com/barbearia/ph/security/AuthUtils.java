package com.barbearia.ph.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

// Ponte entre a autenticação via Keycloak (JWT) e a identidade "celular" que
// o resto do sistema usa pra achar o Cliente/Profissional dono da requisição.
// Assume que o username provisionado no Keycloak (claim "preferred_username")
// é o celular da pessoa — combinar com quem estiver configurando o realm.
public class AuthUtils {

    public static String celular(Authentication auth) {
        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) return null;
        return jwt.getClaimAsString("preferred_username");
    }

    public static boolean isAdmin(Authentication auth) {
        return hasRole(auth, "ROLE_ADMIN");
    }

    public static boolean isBarbeiro(Authentication auth) {
        return hasRole(auth, "ROLE_BARBEIRO");
    }

    private static boolean hasRole(Authentication auth, String role) {
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }
}
