package com.alurachallenges.forohub.dto;

public class JWTDatos {
    private String jwt;

    public JWTDatos(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
