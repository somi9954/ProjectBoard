package org.koreait.models.file;

import org.koreait.commons.Utils;
import org.koreait.commons.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class FileNotFoundException extends CommonException {

    public FileNotFoundException() {
        super(Utils.getMessage("NotFound.file", "error"), HttpStatus.NOT_FOUND);
    }
}