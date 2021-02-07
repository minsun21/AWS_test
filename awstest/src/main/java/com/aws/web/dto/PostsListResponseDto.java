package com.aws.web.dto;

import java.time.LocalDateTime;

import com.aws.domain.posts.Posts;

import lombok.Getter;

@Getter
public class PostsListResponseDto {
	private Long id;
	private String title;
	private String author;
	private String content;
	private String viewCount;
	private LocalDateTime modifedDate;

	public PostsListResponseDto(Posts entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.content = entity.getContent();
		this.author = entity.getAuthor();
		this.viewCount = String.valueOf(entity.getViewCount());
		this.modifedDate = entity.getModifedDate();
	}
}
