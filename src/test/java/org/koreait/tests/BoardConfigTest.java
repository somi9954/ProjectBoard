package org.koreait.tests;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.koreait.entities.Board;
import org.koreait.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.profiles.active=test")
public class BoardConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("게시판 설정 저장 테스트 - 유효성 검사")
    void boardConfigTest() throws Exception {
        String body = mockMvc.perform(
                    post("/admin/board/save")
                            .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString(Charset.forName("UTF-8"));

        assertTrue(body.contains("게시판 아이디"));
        assertTrue(body.contains("게시판 이름"));
    }

    @Test
    @DisplayName("게시판 설정 저장 테스트 - 성공시 302")
    void boardConfigTest2() throws Exception {
        mockMvc.perform(post("/admin/board/save")
                .param("bId", "notice")
                .param("bName", "공지사항")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/admin/board"));

        // 실제 DB에도 설정 값이 있는지 체크
        Board board = boardRepository.findById("notice").orElse(null);
        assertNotNull(board);

        assertTrue(board.getBName().contains("공지사항"));
    }
}
