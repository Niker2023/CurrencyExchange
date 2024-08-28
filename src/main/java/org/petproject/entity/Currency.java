package org.petproject.entity;

import lombok.Data;
import lombok.Value;

@Data
public class Currency {

    private Integer id;
    private String name;
    private String code;
    private String sign;

    public Currency(int id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.sign = sign;
        this.name = fullName;
    }
}
