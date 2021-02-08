package com.aws.web;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aws.service.posts.PostsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class IndexController {

	private final PostsService postsService;
	private final HttpSession httpSession;
	
	@GetMapping("/")
	public String index() {
		return null;
	}
	

}
