package org.koreait.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(indexes = {
        @Index(name="idx_bd_list", columnList = "notice DESC, createdAt DESC"),
        @Index(name="idx_bd_category", columnList = "category")
})
public class BoardData extends Base {

    @Id @GeneratedValue
    private Long seq;

    @Column(length=50, nullable = false)
    private String gid = UUID.randomUUID().toString();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="bId")
    private Board board;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="userNo")
    private Member member;

    @Column(length=50)
    private String category;

    @Column(length=30, nullable = false)
    private String poster;

    @Column(length=65)
    private String guestPw; // 비회원 비밀번호

    @Column(nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false)
    private String content;

    private boolean notice; // 공지사항 여부
    
    private int viewCnt; // 조회수

    private int commentCnt; // 댓글 수

    @Transient
    private List<CommentData> comments; // 댓글 목록

    @Transient
    private List<FileInfo> editorImages;

    @Transient
    private List<FileInfo> attachFiles;
}
