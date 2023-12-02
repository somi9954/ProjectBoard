package org.koreait.models.board.config;

import lombok.RequiredArgsConstructor;
import org.koreait.commons.constants.BoardAuthority;
import org.koreait.controllers.admins.BoardConfigForm;
import org.koreait.entities.Board;
import org.koreait.repositories.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoardConfigSaveService {
    private final BoardRepository boardRepository;

    public void save(BoardConfigForm form) {

        String bId = form.getBId();
        String mode = Objects.requireNonNullElse(form.getMode(), "add");
        Board board = null;
        if (mode.equals("edit") && StringUtils.hasText(bId)) {
            board = boardRepository.findById(bId).orElseThrow(BoardNotFoundException::new);
        } else { // 추가
            board = new Board();
            board.setBId(bId);
        }

        board.setBName(form.getBName());
        board.setActive(form.isActive());
        board.setAuthority(BoardAuthority.valueOf(form.getAuthority()));
        board.setCategory(form.getCategory());

        boardRepository.saveAndFlush(board);
    }
}
