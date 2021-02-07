package com.aws.web;

import java.util.List;
import java.util.Map;

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

	@PostMapping("/api/v1/posts/remove")
	public Result remove(@RequestBody Map<String, List<Long>> ids) {
		return new Result(postsService.delete(ids.get("rowIds")), Constants.RESULT_SUCCESS);
	}

	@GetMapping("/api/v1/posts/get/{id}")
	public Result findById(@PathVariable Long id) {
		return new Result(postsService.findById(id), Constants.RESULT_SUCCESS);
	}
}
