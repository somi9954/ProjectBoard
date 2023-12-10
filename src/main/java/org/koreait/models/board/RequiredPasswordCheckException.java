package org.koreait.models.board;

import org.koreait.commons.Utils;
import org.koreait.commons.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class RequiredPasswordCheckException extends CommonException {

    public RequiredPasswordCheckException() {
        super(Utils.getMessage("Required.guestPw.check", "validation"), HttpStatus.UNAUTHORIZED);
    }
}
