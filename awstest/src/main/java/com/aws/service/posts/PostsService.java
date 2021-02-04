package com.aws.service.posts;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aws.domain.posts.Posts;
import com.aws.domain.posts.PostsRepository;
import com.aws.web.dto.PostsResponseDto;
import com.aws.web.dto.PostsSaveRequestDto;
import com.aws.web.dto.PostsUpdateRequestDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostsService {
	private final PostsRepository postsRepository;

	@Transactional
	public Long save(PostsSaveRequestDto requestDto) {
		return postsRepository.save(requestDto.toEntity()).getId();
	}

	@Transactional
	public Long update(Long id, PostsUpdateRequestDto dto) {
		Posts posts = postsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
		posts.update(dto.getTitle(), dto.getContent());
		return id;
	}

	public PostsResponseDto findById(Long id) {
		Posts entity = postsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
		return new PostsResponseDto(entity);
	}
}
