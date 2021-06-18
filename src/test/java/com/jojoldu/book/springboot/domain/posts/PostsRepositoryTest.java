package com.jojoldu.book.springboot.domain.posts;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.*;


@Rollback(value = true)
@SpringBootTest
class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

//    @After
//    public void cleanup(){
//        postsRepository.deleteAll();
//    }

    @Test
    public void boardsave_load(){

        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";
        Posts posts = Posts.builder()
                .title(title)
                .content(content)
                .author("jojoldu@gmail.com")
                .build();
        postsRepository.save(posts);

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts1 = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);


    }

    @Test
    public void BaseTimeEntity_reg(){

        //given
        LocalDateTime now = LocalDateTime.of(2021,6,17,0,0,0);
        postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build()
        );

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);

        System.out.println("posts.getCreatedDate = " + posts.getCreatedDate());
        System.out.println("posts.getModifiedDate = " + posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }

}