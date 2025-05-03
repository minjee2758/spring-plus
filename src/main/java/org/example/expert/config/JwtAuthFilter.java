package org.example.expert.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String bearerToken = request.getHeader("Authorization");
        String url = request.getRequestURI();

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = jwtUtil.substringToken(bearerToken); // Bearer 이후로 추출

        try {
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT");
                return;
            }

            UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));

            if (url.startsWith("/admin")) {
                // 관리자 권한이 없는 경우 403을 반환합니다.
                if (!UserRole.ADMIN.equals(userRole)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 없습니다.");
                    return;
                }
                chain.doFilter(request, response);
                return;
            }


            // 토큰에서 꺼낸 회원 정보 저장
            AuthUser authUser = new AuthUser(
                    Long.parseLong(claims.getSubject()),
                    (String) claims.get("email"),
                    userRole
            );

            request.setAttribute("userId", Long.parseLong(claims.getSubject()));
            request.setAttribute("email", claims.get("email"));
            request.setAttribute("userRole", claims.get("userRole"));

            // 인가를 위한 권한 정보 저장
            Collection<? extends GrantedAuthority> authorities =
                    List.of(new SimpleGrantedAuthority(authUser.getUserRole().toString()));

            // Spring Security 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(authUser,
                    null, authorities);

            // SecurityContextHolder(세션)에 토큰 담기
            SecurityContextHolder.getContext().setAuthentication(authToken);


            chain.doFilter(request, response);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
        } catch (Exception e) {
            log.error("Internal server error", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
