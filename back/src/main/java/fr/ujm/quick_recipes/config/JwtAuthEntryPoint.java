package fr.ujm.quick_recipes.config;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Class to handle unauthorized requests.
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -7207232652931065551L;

    /**
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Accès non autorisé");
    }

}