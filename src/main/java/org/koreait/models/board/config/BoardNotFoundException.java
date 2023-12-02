package org.koreait.models.board.config;

import org.koreait.commons.Utils;
import org.koreait.commons.exceptions.AlertBackException;
import org.springframework.http.HttpStatus;

public class BoardNotFoundException extends AlertBackException {
    public BoardNotFoundException() {
        super(Utils.getMessage("NotFound.board", "error"));
        setStatus(HttpStatus.NOT_FOUND);
    }
}
