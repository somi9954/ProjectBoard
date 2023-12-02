package org.koreait.models.board;

import org.koreait.commons.exceptions.AlertBackException;
import org.springframework.http.HttpStatus;

public class BoardDataNotFoundException extends AlertBackException {
    public BoardDataNotFoundException() {
        super("등록되지 않은 게시글 입니다.");
        setStatus(HttpStatus.NOT_FOUND);
    }
}
