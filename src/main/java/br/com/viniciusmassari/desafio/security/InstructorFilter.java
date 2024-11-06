package br.com.viniciusmassari.desafio.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.viniciusmassari.desafio.utils.TokenUtil;
import br.com.viniciusmassari.desafio.utils.ValidateTokenDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class InstructorFilter extends OncePerRequestFilter {

    @Autowired
    private TokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        ValidateTokenDTO decodedToken = ValidateTokenDTO.builder().build();

        if (this.doesNotNeedAuthenticationRoute(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        if (header != null) {
            try {
                decodedToken = this.tokenUtil.validateToken(header);

                if (decodedToken.getSubject().equals(null)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            request.setAttribute("instructor_id", decodedToken.getSubject());
            var roles = decodedToken.getToken().getClaim("roles").asList(Object.class);
            var grants = roles.stream().map(role -> new SimpleGrantedAuthority(role.toString())).toList();
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    decodedToken.getSubject(), null,
                    grants);
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        }
    }

    private boolean doesNotNeedAuthenticationRoute(String requestUri) {
        return requestUri.endsWith("show/") || requestUri.contains("auth")
                || requestUri.contains("/instructor/create");
    }
}
