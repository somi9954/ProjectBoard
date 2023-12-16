package org.koreait.models.comment;

import org.koreait.commons.Utils;
import org.koreait.commons.exceptions.AlertBackException;
import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends AlertBackException {

    public CommentNotFoundException() {
        super(Utils.getMessage("NotFound.comment", "error"));
        setStatus(HttpStatus.NOT_FOUND);
    }
}
