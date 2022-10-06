package se.experis.boxinator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import se.experis.boxinator.services.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AccountInterceptor implements HandlerInterceptor {
    private static Logger log = LoggerFactory.getLogger(AccountInterceptor.class);

    private final AccountService accountService;

    public AccountInterceptor(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("[preHandle][" + request + "][" + request.getMethod() + "]" + request.getRequestURI());

        // check if user is authenticated
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Jwt principal = (Jwt) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            log.warn("[preHandle][" + principal + "]");
            // create account
            accountService.getAccountWithJwt(principal);
            return true;
        }

        return true;
    }

}
