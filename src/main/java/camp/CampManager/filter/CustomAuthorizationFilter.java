package camp.CampManager.filter;

import camp.CampManager.security.ConfigProperties;
import camp.CampManager.security.UsersDetailsService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private ConfigProperties properties;
    @Autowired
    private UsersDetailsService usersDetailsService;

    private String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/login")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    // TODO Aquí el servidor descodifica el JWT per a obtenir els rols
                    // Canviar això per agafar el JWT proporcionat pel Keycloak per treure la info de l'usuari
                    String username = getUsernameFromKC(token);
                    // Fer un loadUserByUsername per a carregar les authorities
                    User user = (User) usersDetailsService.loadUserByUsername(username);
                    /* OLD
                    assert secret != null;
                    Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                     */
                    // Crear authorities tal com estan aqui
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    user.getAuthorities().forEach(role -> authorities.add((SimpleGrantedAuthority) role));
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception exception) {
                    response.setHeader("error", exception.getMessage());
                    response.sendError(FORBIDDEN.value());
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Map<String, Object> getInfoFromKC(String token) throws AuthenticationException, JsonProcessingException {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://keycloak-container:8080/realms/campmanager/protocol/openid-connect/userinfo"))
                .method("POST", HttpRequest.BodyPublishers.noBody())
                .header("Authorization", "Bearer " + token)
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        assert response != null;
        if (response.statusCode() != 200) {
            throw new AuthenticationException();
        }
        return new ObjectMapper().readValue(response.body(), HashMap.class);
    }

    public String getUsernameFromKC(String token) throws AuthenticationException, JsonProcessingException {
        var result = getInfoFromKC(token);
        if (result.containsKey("given_name")) {
            return (String) result.get("given_name");
        }
        if (result.containsKey("preferred_username")) {
            return (String) result.get("preferred_username");
        }
        throw new AuthenticationException();
    }
}
