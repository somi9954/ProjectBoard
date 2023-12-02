package org.koreait.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.profiles.active=test")
public class BoardSaveTest {


    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("게시글 저장 테스트 - 성공시 /board/list/게시글 아이디")
    void saveTest() throws Exception {
        String url = mockMvc.perform(post("/board/save")
                .param("mode", "write")
                .param("subject", "제목")
                .param("poster", "작성자")
                .param("content", "내용")
                .with(csrf())
        ).andReturn().getResponse().getRedirectedUrl();

        assertTrue(url.contains("/board/list"));


    }
}
