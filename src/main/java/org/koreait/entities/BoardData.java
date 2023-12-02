package org.koreait.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class BoardData extends Base {

    @Id @GeneratedValue
    private Long seq;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="userNo")
    private Member member;

    @Column(length=30, nullable = false)
    private String poster;

    @Column(nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false)
    private String content;
}
