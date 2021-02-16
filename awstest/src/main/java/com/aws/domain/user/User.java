package com.aws.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.aws.domain.BaseTimeEntity;
import com.aws.domain.enums.SocialType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	@Column
	private String principal;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SocialType socialType;

	private String password;

	@Builder
	public User(String name, String email, String password, String picutre, String principal, SocialType socialType) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.socialType = socialType;
		this.principal = principal;
	}

	public User update(String name, String picture) {
		this.name = name;
		return this;
	}

}
