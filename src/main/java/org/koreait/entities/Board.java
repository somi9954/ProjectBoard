package org.koreait.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.koreait.commons.constants.BoardAuthority;

@Entity
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class Board extends BaseMember {
    @Id
    @Column(length=30)
    private String bId;

    @Column(length=60, nullable = false)
    private String bName;

    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column(length=10, nullable = false)
    private BoardAuthority authority = BoardAuthority.ALL;

    @Lob
    private String category;
}
