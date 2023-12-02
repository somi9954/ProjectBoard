package org.koreait.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Entity
@Data
public class Configs {
    @Id
    @Column(length=45, name="_code")
    private String code;

    @Lob
    @Column(name="_value")
    private String value;
}
