package org.koreait.models.comment;

import lombok.RequiredArgsConstructor;
import org.koreait.entities.BoardData;
import org.koreait.entities.CommentData;
import org.koreait.repositories.CommentDataRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentDeleteService {
    private final CommentInfoService infoService;
    private final CommentDataRepository repository;

    public BoardData delete(Long seq) {

        infoService.isMine(seq);

        CommentData commentData = infoService.get(seq);
        BoardData boardData = commentData.getBoardData();

        repository.delete(commentData);
        repository.flush();
        
        // 총 댓글 수 업데이트
        Long boardDataSeq = boardData.getSeq();
        infoService.updateCommentCnt(boardDataSeq);

        return boardData;
    }
}
