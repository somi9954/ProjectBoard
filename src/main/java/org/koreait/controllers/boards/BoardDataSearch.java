package org.koreait.controllers.boards;

import lombok.Data;

@Data
public class BoardDataSearch {
    private String bId;
    private int page = 1;
    private int limit = 20;

    private String category;
    private String sopt;
    private String skey;
}
