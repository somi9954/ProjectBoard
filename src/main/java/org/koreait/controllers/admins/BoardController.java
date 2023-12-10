package org.koreait.controllers.admins;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koreait.commons.ListData;
import org.koreait.commons.ScriptExceptionProcess;
import org.koreait.commons.constants.BoardAuthority;
import org.koreait.commons.menus.Menu;
import org.koreait.entities.Board;
import org.koreait.models.board.config.BoardConfigDeleteService;
import org.koreait.models.board.config.BoardConfigInfoService;
import org.koreait.models.board.config.BoardConfigSaveService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller("adminBoardController")
@RequestMapping("/admin/board")
@RequiredArgsConstructor
public class BoardController implements ScriptExceptionProcess {

    private final HttpServletRequest request;
    private final BoardConfigSaveService saveService;
    private final BoardConfigInfoService infoService;
    private final BoardConfigDeleteService deleteService;

    @GetMapping
    public String list(@ModelAttribute BoardSearch search, Model model) {
        commonProcess("list", model);

        ListData<Board> data = infoService.getList(search);

        model.addAttribute("items", data.getContent());
        model.addAttribute("pagination", data.getPagination());

        return "admin/board/list";
    }

    @PatchMapping
    public String updateList(@RequestParam(name="idx", required = false) List<Integer> idxes, Model model) {

        saveService.update(idxes);


        // 수정 완료시 부모창을 새로고침.
        model.addAttribute("script", "parent.location.reload();");

        return "common/_execute_script";
    }

    @DeleteMapping
    public String deleteList(@RequestParam(name="idx", required = false) List<Integer> idxes, Model model) {

        deleteService.delete(idxes);

        // 삭제 성공시 부모창 새로고침
        model.addAttribute("script", "parent.location.reload();");
        return "common/_execute_script";
    }

    @GetMapping("/add")
    public String register(@ModelAttribute BoardConfigForm form, Model model) {
        commonProcess("add", model);

        return "admin/board/add";
    }

    @GetMapping("/edit/{bId}")
    public String update(@PathVariable("bId") String bId, Model model) {
        commonProcess("edit", model);

        BoardConfigForm form = infoService.getForm(bId);
        model.addAttribute("boardConfigForm", form);

        return "admin/board/edit";
    }

    @PostMapping("/save")
    public String save(@Valid BoardConfigForm form, Errors errors, Model model) {

        String mode = Objects.requireNonNullElse(form.getMode(), "add");
        commonProcess(mode, model);

        if (errors.hasErrors()) {
            return "admin/board/" + mode;
        }

        saveService.save(form);

        return "redirect:/admin/board";
    }
    
    private void commonProcess(String mode, Model model) {
        String pageTitle = "게시판 목록";
        mode = Objects.requireNonNullElse(mode, "list");
        if (mode.equals("add")) pageTitle = "게시판 등록";
        else if (mode.equals("edit")) pageTitle = "게시판 수정";

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("menuCode", "board");
        model.addAttribute("submenus", Menu.gets("board"));
        model.addAttribute("subMenuCode", Menu.getSubMenuCode(request));

        model.addAttribute("authorities", BoardAuthority.getList());
    }
}
