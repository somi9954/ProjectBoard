package org.koreait.models.comment;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.koreait.commons.MemberUtil;
import org.koreait.commons.Utils;
import org.koreait.commons.exceptions.AlertBackException;
import org.koreait.entities.BoardData;
import org.koreait.entities.CommentData;
import org.koreait.entities.Member;
import org.koreait.entities.QCommentData;
import org.koreait.models.board.RequiredPasswordCheckException;
import org.koreait.repositories.BoardDataRepository;
import org.koreait.repositories.CommentDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentInfoService {

    private final CommentDataRepository commentDataRepository;
    private final BoardDataRepository boardDataRepository;
    private final MemberUtil memberUtil;
    private final EntityManager em;
    private final HttpSession session;

    public CommentData get(Long seq) {
        CommentData comment = commentDataRepository.findById(seq).orElseThrow(CommentNotFoundException::new);

        return comment;
    }

    /**
     * 댓글 목록
     *
     * @param boardDataSeq : 게시글 번호
     * @return
     */
    public List<CommentData> getList(Long boardDataSeq) {
        QCommentData commentData = QCommentData.commentData;

        PathBuilder<CommentData> pathBuilder = new PathBuilder<>(CommentData.class, "commentData");
        List<CommentData> items = new JPAQueryFactory(em)
                .selectFrom(commentData)
                .where(commentData.boardData.seq.eq(boardDataSeq))
                .leftJoin(commentData.member)
                .fetchJoin()
                .orderBy(new OrderSpecifier(Order.ASC, pathBuilder.get("createdAt")))
                .fetch();

        return items;
    }

    /**
     * 댓글 수 업데이트
     *
     * @param seq
     */
    public void updateCommentCnt(Long seq) {
        CommentData comment = get(seq);
        BoardData boardData = comment.getBoardData();
        Long boardDataSeq = boardData.getSeq();
        boardData.setCommentCnt(commentDataRepository.getTotal(boardDataSeq));

        boardDataRepository.flush();
    }

    public void isMine(Long seq) {
        if (memberUtil.isAdmin()) {
            return;
        }

        CommentData data = get(seq);
        Member commentMember = data.getMember();
        if (commentMember == null) { // 비회원 작성
            String key = "chk_comment_" + seq;
            if (session.getAttribute(key) == null) { // 비회원 비밀번호 확인 전
                session.setAttribute("comment_seq", seq);
                throw new RequiredPasswordCheckException();
            }

        } else { // 로그인 상태 작성
            if (!memberUtil.isLogin()
                    || commentMember.getUserNo().longValue() != memberUtil.getMember().getUserNo().longValue()) {
                throw new AlertBackException(Utils.getMessage("작성한_댓글만_수정_삭제_가능합니다.", "error"));
            }
        }
    }

}