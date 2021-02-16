package com.aws;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.aws.domain.posts.PostsRepository;
import com.aws.domain.user.User;
import com.aws.domain.user.UserRepository;
import com.aws.resolver.UserArgResolver;

@EnableJpaAuditing
@SpringBootApplication
public class AwstestApplication extends WebMvcConfigurerAdapter {
	@Autowired
	private UserArgResolver userArgResolver;

	public static void main(String[] args) {
		SpringApplication.run(AwstestApplication.class, args);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(userArgResolver);
	}

//	@Bean
//	public CommandLineRunner runner(UserRepository userRepository, PostsRepository postsRepository) {
//		return (args) -> {
//			User user = userRepository.save(User.builder()
//					.name("havi")
//					.password("test")
//					.email("havi@gmail.com")
//					.build());
//
////			IntStream.rangeClosed(1, 200).forEach(index ->
////				boardRepository.save(Board.builder()
////						.title("게시글"+index)
////						.subTitle("순서"+index)
////						.content("컨텐츠")
////						.boardType(BoardType.free)
////						.createdDate(LocalDateTime.now())
////						.updatedDate(LocalDateTime.now())
////						.user(user).build())
////			);
//		};
//	}
}
