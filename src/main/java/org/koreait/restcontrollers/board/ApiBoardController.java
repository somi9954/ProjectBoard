package org.koreait.restcontrollers.board;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koreait.commons.exceptions.BadRequestException;
import org.koreait.commons.rest.JSONData;
import org.koreait.controllers.boards.BoardForm;
import org.koreait.entities.BoardData;
import org.koreait.models.board.BoardInfoService;
import org.koreait.models.board.BoardSaveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class ApiBoardController {

    private final BoardSaveService saveService;
    private final BoardInfoService infoService;

    @PostMapping("/write/{bId}")
    public ResponseEntity write(@PathVariable("bId") String bId, @RequestBody @Valid BoardForm form, Errors errors) {

        if (errors.hasErrors()) {
            String message = errors.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
            throw new BadRequestException(message);
        }

        saveService.save(form);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/view/{seq}")
    public JSONData<BoardData> view(@PathVariable("seq") Long seq) {

        BoardData data = infoService.get(seq);

        return new JSONData<>(data);
    }
}
