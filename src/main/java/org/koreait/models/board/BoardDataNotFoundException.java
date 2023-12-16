package org.koreait.models.board;

import org.koreait.commons.Utils;
import org.koreait.commons.exceptions.AlertBackException;
import org.springframework.http.HttpStatus;

public class BoardDataNotFoundException extends AlertBackException {
    public BoardDataNotFoundException() {
        super(Utils.getMessage("NotFound.boardData", "error"));
        setStatus(HttpStatus.NOT_FOUND);
    }
}
