package com.aws.config;

import static com.aws.domain.enums.SocialType.GOOGLE;
import static com.aws.domain.enums.SocialType.KAKAO;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CompositeFilter;

import com.aws.auth.ClientResources;
import com.aws.auth.UserTokenService;
import com.aws.domain.enums.SocialType;

@EnableOAuth2Client
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private OAuth2ClientContext oAuth2ClientContext;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		http.authorizeRequests().antMatchers("/", "/login/**").permitAll() // 누구나 접속 가능
		//소셜미디어용 경로
		.antMatchers("/google").hasAuthority(GOOGLE.getRoleType())
		.antMatchers("/kakao").hasAuthority(KAKAO.getRoleType())
		.anyRequest().authenticated()
				.and().headers().frameOptions().disable().and().exceptionHandling()
				.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")) // 혀용되지 않은 리퀘스트 요청 시 /login으로
																							// 넘김
				.and().formLogin().successForwardUrl("/api/v1/posts/list").and().logout().logoutUrl("/logout")
				.logoutSuccessUrl("/").deleteCookies("JSESSIONID").invalidateHttpSession(true).and()
				.addFilterBefore(filter, CsrfFilter.class) // 모든 요청에 토큰을 요구함
				.addFilterBefore(oauth2Filter(), BasicAuthenticationFilter.class) // HttpBasic 방식. 매번 오는 요청 Header에서 username,
																					// password를 읽음. 저장은 안해서 stateless
				.csrf().disable();
	}

	@Bean
	public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(filter);
		registration.setOrder(-100);
		return registration;
	}

	private Filter oauth2Filter() {
		CompositeFilter filter = new CompositeFilter();
		List<Filter> filters = new ArrayList<>();
		filters.add(oauth2Filter(google(), "/login/google", GOOGLE));
		filters.add(oauth2Filter(google(), "/login/kakao", KAKAO));
		filter.setFilters(filters);
		return filter;
	}

	private Filter oauth2Filter(ClientResources client, String path, SocialType socialType) {
		// 인증이 수행될 path 넣고 필터 생성
		OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(path);
		// 권한 서버와의 통신을 위한 템플릿.
		OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oAuth2ClientContext);
		filter.setRestTemplate(template);
		filter.setTokenServices(new UserTokenService(client, socialType));
		// 인증성공시 리다이렉트
		filter.setAuthenticationSuccessHandler((request, response, authentication) -> response
				.sendRedirect("/" + socialType.getValue() + "/complete"));
		// 인증 실패시 리다이렉트
		filter.setAuthenticationFailureHandler((request, response, exception) -> response.sendRedirect("/error"));
		return filter;
	}

	@Bean
	@ConfigurationProperties("google")
	public ClientResources google() {
		return new ClientResources();
	}

	@Bean
	@ConfigurationProperties("kakao")
	public ClientResources kakao() {
		return new ClientResources();
	}

}
