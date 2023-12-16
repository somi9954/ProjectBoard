package org.koreait.controllers.comment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @PostMapping("/save")
    public String save(Model model){

        // 댓글 작성 완료 시에 부모창을 새로고침 -> 새로운 목록 갱신
        model.addAttribute("script", "parent.location.reload();");

        return "common/_execute_script";
    }

    @RequestMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq) {


        return "redirect:/board/view/게시글번호";
    }
}
