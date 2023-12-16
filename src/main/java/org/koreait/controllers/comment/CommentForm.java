package org.koreait.controllers.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentForm {
    private Long seq; // 댓글 등록 번호
    private Long boardDataSeq; //게시글 번호

    @NotBlank
    private String poster;

    private String guestPw;

    @NotBlank
    private String content;
}
