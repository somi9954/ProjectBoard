package org.koreait.commons.exceptions;

import org.koreait.commons.Utils;

public class BadRequestException extends AlertBackException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException() {
        super(Utils.getMessage("BadRequest", "error"));
    }
}