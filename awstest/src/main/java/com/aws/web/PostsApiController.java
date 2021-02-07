package com.aws.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aws.constants.Constants;
import com.aws.service.posts.PostsService;
import com.aws.web.dto.PostsResponseDto;
import com.aws.web.dto.PostsSaveRequestDto;
import com.aws.web.dto.PostsUpdateRequestDto;
import com.aws.web.dto.Result;

import lombok.RequiredArgsConstructor;

@SuppressWarnings({ "unchecked", "rawtypes" })
@RequiredArgsConstructor
@RestController
public class PostsApiController {
	private final PostsService postsService;

	@GetMapping("/api/v1/posts/list")
	public Result index() {
		return new Result(postsService.findAllDesc(), Constants.RESULT_SUCCESS);
	}

	@PostMapping("/api/v1/posts")
	public Long save(@RequestBody PostsSaveRequestDto requestDto) {
		return postsService.save(requestDto);
	}

	@PutMapping("/api/v1/posts/edit/{id}")
	public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
		return postsService.update(id, requestDto);
	}

	@PutMapping("/api/v1/posts/remove/{id}")
	public Result remove(@PathVariable Long id) {
		return new Result(postsService.delete(id), Constants.RESULT_SUCCESS);
	}

	@GetMapping("/api/v1/posts/{id}")
	public PostsResponseDto findById(@PathVariable Long id) {
		return postsService.findById(id);
	}

//	@GetMapping("/posts/update/{id}")
//	public String postsUpdate(@PathVariable Long id, )
}
