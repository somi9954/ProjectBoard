package org.koreait.models.board;

import lombok.RequiredArgsConstructor;
import org.koreait.entities.BoardData;
import org.koreait.repositories.BoardDataRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardInfoService {

    private final BoardDataRepository boardDataRepository;

    public BoardData get(Long seq) {

        BoardData data = boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new);

        return data;
    }
}
