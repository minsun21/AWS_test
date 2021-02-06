package com.aws.web.dto;

import java.time.LocalDateTime;

import com.aws.domain.posts.Posts;

import lombok.Getter;

@Getter
public class PostsListResponseDto {
	private Long id;
	private String title;
	private String autor;
	private LocalDateTime modifiedDate;

	public PostsListResponseDto(Posts entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.autor = entity.getAuthor();
		this.modifiedDate = entity.getModifedDate();
	}
}
