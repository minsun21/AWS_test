package com.aws.auth;

import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.aws.domain.enums.SocialType;

public class UserTokenService extends UserInfoTokenServices {
	//UserInfoTokenServices는 Oauth2에서 제공. 소셜 서버와 통신하여 User 정보 가져오는 로직이 이미 있음. URI,ClientId필요.
	
	public UserTokenService(ClientResources resources, SocialType socialType) {
		super(resources.getResource().getUserInfoUri(), resources.getClient().getClientId());
		setAuthoritiesExtractor(new OAuth2AuthoritiesExtractor(socialType));
	}

	public static class OAuth2AuthoritiesExtractor implements AuthoritiesExtractor { // 권한 네이밍 처리 위함. 

		private String socialType;

		public OAuth2AuthoritiesExtractor(SocialType socialType) {
			this.socialType = socialType.getRoleType();
		}

		@Override
		public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
			return AuthorityUtils.createAuthorityList(this.socialType);
		}
	}
}
