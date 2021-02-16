package com.aws.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aws.annotation.SocialUser;
import com.aws.domain.user.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class LoginController {
	@GetMapping("/{google|kakao}/complete")
	public String loginComplete(@SocialUser User user) {
		return null;
	}
}
