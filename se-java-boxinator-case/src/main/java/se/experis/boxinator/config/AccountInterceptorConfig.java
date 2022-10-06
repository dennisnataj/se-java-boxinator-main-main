package se.experis.boxinator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import se.experis.boxinator.util.AccountInterceptor;

//config for registering middleware
@EnableWebMvc
@Configuration
public class AccountInterceptorConfig implements WebMvcConfigurer {

    private final AccountInterceptor accountInterceptor;

    public AccountInterceptorConfig(AccountInterceptor accountInterceptor) {
        super();
        this.accountInterceptor = accountInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accountInterceptor);
    }
}
