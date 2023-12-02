package org.koreait.models.board.config;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.codehaus.groovy.util.StringUtil;
import org.koreait.commons.ListData;
import org.koreait.commons.Pagination;
import org.koreait.commons.Utils;
import org.koreait.commons.constants.BoardAuthority;
import org.koreait.controllers.admins.BoardConfigForm;
import org.koreait.controllers.admins.BoardSearch;
import org.koreait.entities.Board;
import org.koreait.entities.QBoard;
import org.koreait.repositories.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class BoardConfigInfoService {

    private final BoardRepository repository;
    private final HttpServletRequest request;

    public Board get(String bId) {
        Board data = repository.findById(bId).orElseThrow(BoardNotFoundException::new);

        return data;
    }

    public BoardConfigForm getForm (String bId) {
        Board board = get(bId) ;

        BoardConfigForm  form = new ModelMapper().map(board, BoardConfigForm.class);
        form.setAuthority(board.getAuthority().name());
        form.setMode("edit");

        return form;
    }

    public ListData<Board> getList(BoardSearch search) {
        BooleanBuilder andBuilder = new BooleanBuilder();

        int page = Utils.getNumber(search.getPage(), 1);
        int limit = Utils.getNumber(search.getLimit(), 20);

        /* 검색 처리 S */
        QBoard board = QBoard.board;

        // 통합 검색
        String sopt = Objects.requireNonNullElse(search.getSopt(), "ALL");
        String skey = search.getSkey();
        if (StringUtils.hasText(skey)) {
            skey = skey.trim();

            if (sopt.equals("bId")) { // 게시판 아이디
                andBuilder.and(board.bId.contains(skey));
            } else if (sopt.equals("bName")) { // 게시판 이름
                andBuilder.and(board.bName.contains(skey));
            } else { // 통합검색
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(board.bId.contains(skey))
                         .or(board.bName.contains(skey));

                andBuilder.and(orBuilder);
            }
        }

        // 사용여부
        List<Boolean> active = search.getActive();
        if (active != null && !active.isEmpty()) {
            andBuilder.and(board.active.in(active));
        }

        // 글쓰기 권한
        List<BoardAuthority> authorites = search.getAuthority() == null ? null : search.getAuthority().stream().map(BoardAuthority::valueOf).toList();
        if (authorites != null && !authorites.isEmpty()) {

            andBuilder.and(board.authority.in(authorites));
        }

        /* 검색 처리 E */



        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));

        Page<Board> data = repository.findAll(andBuilder, pageable);


        Pagination pagination = new Pagination(page, (int)data.getTotalElements(), 10, limit, request);

        ListData<Board> listData = new ListData<>();
        listData.setContent(data.getContent());
        listData.setPagination(pagination);

        return listData;
    }
}
