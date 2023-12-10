package org.koreait.controllers.boards;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.koreait.entities.FileInfo;

import java.util.List;
import java.util.UUID;

@Data
public class BoardForm {

    private String mode = "write";

    private Long seq;

    private String bId;

    private String gid = UUID.randomUUID().toString();

    private String category;

    @NotBlank
    private String subject;

    @NotBlank
    private String poster;

    @NotBlank
    private String content;

    private boolean notice;

    private String guestPw;

    private List<FileInfo> editorImages;
    private List<FileInfo> attachFiles;
}
