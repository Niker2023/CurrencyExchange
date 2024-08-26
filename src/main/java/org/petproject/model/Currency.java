package org.petproject.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Currency {

    private String name;
    private String code;
    private String sign;

    public Currency(String name, String code, String sign) {
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

}
