package com.aws.resolver;

import static com.aws.domain.enums.SocialType.GOOGLE;
import static com.aws.domain.enums.SocialType.KAKAO;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.aws.annotation.SocialUser;
import com.aws.domain.enums.SocialType;
import com.aws.domain.user.User;
import com.aws.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserArgResolver implements HandlerMethodArgumentResolver { // HandlerMethodArgumentResolver : AOP 생성 방식 중
																		// 하나
	private final UserRepository userRepository;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// 해당 파라미터를 지원할지말지 결정 true면 resolveArgument실행
		// @SocialUser 어노테이션이 있고 타입이 user인 파라미터만 true
		return parameter.getParameterAnnotation(SocialUser.class) != null
				&& parameter.getParameterType().equals(User.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		// 실제 객체 생성해서 파라미터에 바인딩
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
				.getSession();
		User user = (User) session.getAttribute("user");
		return getUser(user, session); // session에서 가져온 User객체가 없으면 새로 생성, 있으면 바로 사용
	}

	private User getUser(User user, HttpSession session) {
		if (user == null) {
			try {
				OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext()
						.getAuthentication();
				Map<String, String> map = (HashMap<String, String>) authentication.getUserAuthentication().getDetails();
				User convertUser = convertUser(String.valueOf(authentication.getAuthorities().toArray()[0]), map);
				user = userRepository.findByEmail(convertUser.getEmail()).get();
				if (user == null) {
					user = userRepository.save(convertUser);
				}
				setRoleIfNotSame(user, authentication, map);
				session.setAttribute("user", user);
			} catch (ClassCastException e) {
				return user;
			}
		}
		return user;
	}

	private User convertUser(String authority, Map<String, String> map) {
		if (GOOGLE.isEquals(authority))
			return getModernUser(GOOGLE, map);
		else if (KAKAO.isEquals(authority))
			return getKaKaoUser(map);
		return null;
	}

	private User getModernUser(SocialType socialType, Map<String, String> map) {
		return User.builder().name(map.get("name")).email(map.get("email")).principal(map.get("id"))
				.socialType(socialType).build();
	}

	@SuppressWarnings("unchecked")
	private User getKaKaoUser(Map<String, String> map) {
		HashMap<String, String> propertyMap = (HashMap<String, String>) (Object) map.get("properties");
		return User.builder().name(propertyMap.get("nickname")).email(map.get("kaccount_email"))
				.principal(String.valueOf(map.get("id"))).socialType(KAKAO).build();
	}

	// 인증된 애가 권한을 갖고 있는지 체크.
	// 권한이 없으면 SecurityContextHolder 사용해서 해당 소셜 타입으로 권한 저장
	private void setRoleIfNotSame(User user, OAuth2Authentication authentication, Map<String, String> map) {
		if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority(user.getSocialType().getRoleType()))) {
			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(map, "N/A",
					AuthorityUtils.createAuthorityList(user.getSocialType().getRoleType())));
		}
	}
}
