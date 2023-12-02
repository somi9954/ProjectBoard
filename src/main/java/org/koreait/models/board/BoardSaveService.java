package org.koreait.models.board;

import lombok.RequiredArgsConstructor;
import org.koreait.controllers.boards.BoardForm;
import org.koreait.entities.BoardData;
import org.koreait.repositories.BoardDataRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoardSaveService {
    private final BoardDataRepository boardDataRepository;

    public void save(BoardForm form) {
        Long seq = form.getSeq();
        String mode = Objects.requireNonNullElse(form.getMode(), "add");

        BoardData data = null;
        if (mode.equals("update") && seq != null) {
            data = boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new);
        } else {
            data = new BoardData();
        }

        data.setSubject(form.getSubject());
        data.setContent(form.getContent());
        data.setPoster(form.getPoster());

        boardDataRepository.saveAndFlush(data);
    }
}
