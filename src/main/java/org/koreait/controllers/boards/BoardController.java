package org.koreait.controllers.boards;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koreait.commons.ListData;
import org.koreait.commons.MemberUtil;
import org.koreait.commons.ScriptExceptionProcess;
import org.koreait.commons.Utils;
import org.koreait.commons.constants.BoardAuthority;
import org.koreait.commons.exceptions.AlertBackException;
import org.koreait.commons.exceptions.AlertException;
import org.koreait.entities.Board;
import org.koreait.entities.BoardData;
import org.koreait.entities.FileInfo;
import org.koreait.models.board.*;
import org.koreait.models.board.config.BoardConfigInfoService;
import org.koreait.models.board.config.BoardNotFoundException;
import org.koreait.models.file.FileInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller("Controller2")
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController implements ScriptExceptionProcess {
    private final Utils utils;
    private final MemberUtil memberUtil;
    private final BoardSaveService saveService;
    private final BoardInfoService infoService;
    private final BoardDeleteService deleteService;
    private final BoardConfigInfoService configInfoService;
    private final FileInfoService fileInfoService;

    private BoardData boardData;

    @GetMapping("/write/{bId}")
    public String write(@PathVariable("bId") String bId, @ModelAttribute  BoardForm form, Model model) {
        commonProcess(bId, "write", model);

        if (memberUtil.isLogin()) {
            form.setPoster(memberUtil.getMember().getUserNm());
        }

        form.setBId(bId);

        return utils.tpl("board/write");
    }

    @GetMapping("/update/{seq}")
    public String update(@PathVariable("seq") Long seq, Model model) {
        if (!infoService.isMine(seq)) { // 직접 작성한 게시글이 아닌 경우
            throw new AlertBackException(Utils.getMessage("작성한_게시글만_수정할_수_있습니다.", "error"));
        }

        BoardForm form = infoService.getForm(seq);

        commonProcess(form.getBId(), "update", model);

        model.addAttribute("boardForm", form);

        return utils.tpl("board/update");
    }

    @PostMapping("/save")
    public String save(@Valid BoardForm form, Errors errors, Model model) {
        String mode = Objects.requireNonNullElse(form.getMode(), "write");
        String bId = form.getBId();

        commonProcess(bId, mode, model);

        if (mode.equals("update")) {
            Long seq = form.getSeq();
            if (!infoService.isMine(seq)) { // 직접 작성한 게시글이 아닌 경우
                throw new AlertBackException(Utils.getMessage("작성한_게시글만_수정할_수_있습니다.", "error"));
            }
        }


        saveService.save(form, errors);

        if (errors.hasErrors()) {
            String gid = form.getGid();
            List<FileInfo> editorImages = fileInfoService.getListAll(gid, "editor");
            List<FileInfo> attachFiles = fileInfoService.getListAll(gid, "attach");
            form.setEditorImages(editorImages);
            form.setAttachFiles(attachFiles);

            return utils.tpl("board/" + mode);
        }


        return "redirect:/board/list/" + bId;
    }

    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") Long seq,
                       @ModelAttribute  BoardDataSearch search, Model model) {

        // 게시글 조회수 업데이트
        infoService.updateView(seq);

        BoardData data = infoService.get(seq);
        boardData = data;

        String bId = data.getBoard().getBId();
        commonProcess(bId, "view", model);

        search.setBId(bId);
        ListData<BoardData> listData = infoService.getList(search);

        model.addAttribute("boardData", data);
        model.addAttribute("items", listData.getContent());
        model.addAttribute("pagination", listData.getPagination());

        return utils.tpl("board/view");
    }

    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq) {
        if (!infoService.isMine(seq)) {
            throw new AlertBackException(Utils.getMessage("작성한_게시글만_삭제_가능합니다.", "error"));
        }

        BoardData data = infoService.get(seq);
        deleteService.delete(seq);

        return "redirect:/board/list/" + data.getBoard().getBId();
    }

    @GetMapping("/list/{bId}")
    public String list(@PathVariable("bId") String bId, @ModelAttribute BoardDataSearch search, Model model) {
        commonProcess(bId, "list", model);

        search.setBId(bId);
        System.out.println("search : " + search);

        ListData<BoardData> data = infoService.getList(search);
        model.addAttribute("items", data.getContent());
        model.addAttribute("pagination", data.getPagination());

        return utils.tpl("board/list");
    }

    @PostMapping("/guest/password")
    public String guestPasswordCheck(@RequestParam("password") String password, HttpSession session, Model model) {

        Long seq = (Long)session.getAttribute("guest_seq");
        if (seq == null) {
            throw new BoardDataNotFoundException();
        }

        if (!infoService.checkGuestPassword(seq, password)) { // 비번 검증 실패시
            throw new AlertException(Utils.getMessage("비밀번호가_일치하지_않습니다.", "error"));
        }

        // 검증 성공시
        String key = "chk_" + seq;
        session.setAttribute(key, true);
        session.removeAttribute("guest_seq");

        model.addAttribute("script", "parent.location.reload()");
        return "common/_execute_script";
    }

    private void commonProcess(String bId, String mode, Model model) {

        Board board = configInfoService.get(bId);
        if (board == null || (!board.isActive() && !memberUtil.isAdmin())) { // 등록되지 않거나 또는 미사용 중 게시판
            throw new BoardNotFoundException();
        }

        /* 게시판 분류 S */
        String category = board.getCategory();
        List<String> categories = StringUtils.hasText(category) ?
                Arrays.stream(category.trim().split("\\n"))
                        .map(s -> s.replaceAll("\\r", ""))
                        .toList()
                : null;

        model.addAttribute("categories", categories);
        /* 게시판 분류 E */

        String bName = board.getBName();
        String pageTitle = bName;
        if (mode.equals("write")) pageTitle = bName + " 작성";
        else if (mode.equals("update")) pageTitle = bName + " 수정";
        else if (mode.equals("view") && boardData != null) {
            pageTitle = boardData.getSubject() + "||" + bName;
        }


        /* 글쓰기, 수정시 권한 체크 S */
        if (mode.equals("write") || mode.equals("update")) {
            BoardAuthority authority = board.getAuthority();
            if (!memberUtil.isAdmin() && !memberUtil.isLogin()
                    && authority == BoardAuthority.MEMBER) { // 회원 전용
                throw new AlertBackException(Utils.getMessage("MemberOnly.board", "error"));
            }

            if (authority == BoardAuthority.ADMIN && !memberUtil.isAdmin()) { // 관리자 전용
                throw new AlertBackException(Utils.getMessage("AdminOnly.board", "error"));
            }
        }
        /* 글쓰기, 수정시 권한 체크 E */

        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        if (mode.equals("write") || mode.equals("update")) {
            addCommonScript.add("ckeditor/ckeditor");
            addCommonScript.add("fileManager");

            addScript.add("board/form");
        }

        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("board", board);
    }

    @ExceptionHandler(RequiredPasswordCheckException.class)
    public String guestPassword() {

        return utils.tpl("board/password");
    }
}