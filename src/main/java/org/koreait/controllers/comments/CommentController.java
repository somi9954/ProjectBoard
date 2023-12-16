package org.koreait.controllers.comments;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koreait.commons.ScriptExceptionProcess;
import org.koreait.commons.Utils;
import org.koreait.commons.exceptions.AlertException;
import org.koreait.entities.BoardData;
import org.koreait.models.board.RequiredPasswordCheckException;
import org.koreait.models.comment.CommentDeleteService;
import org.koreait.models.comment.CommentInfoService;
import org.koreait.models.comment.CommentSaveService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController implements ScriptExceptionProcess {

    private final CommentSaveService saveService;
    private final CommentDeleteService deleteService;
    private final CommentInfoService infoService;

    private final Utils utils;

    @PostMapping("/save")
    public String save(@Valid CommentForm form, Errors errors, Model model) {

        saveService.save(form, errors);

        if (errors.hasErrors()) {
            Map<String, List<String>> messages = Utils.getMessages(errors);

            String message = (new ArrayList<List<String>>(messages.values())).get(0).get(0);

            throw new AlertException(message);
        }


        // 댓글 작성 완료 시에 부모창을 새로고침 -> 새로운 목록 갱신
        model.addAttribute("script", "parent.location.reload();");
        return "common/_execute_script";
    }

    @RequestMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq) {

        BoardData boardData = deleteService.delete(seq);

        return "redirect:/board/view/" + boardData.getSeq() + "#comments";
    }

    @ExceptionHandler(RequiredPasswordCheckException.class)
    public String guestPassword() {
        return utils.tpl("board/password");
    }
}