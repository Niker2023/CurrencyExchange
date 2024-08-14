package org.petproject.model;

public class Currency {

    private String name;
    private String code;
    private String sign;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency(String name, String code, String sign) {
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
