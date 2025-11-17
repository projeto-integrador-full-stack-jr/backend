package com.mentoria.back_end_mentoria.meta.vo;

public enum StatusMeta {

    AGUARDANDO(1),
    EM_ANDAMENTO(2),
    CONCLUIDO(3),
    EXPIRADA(4);

    private int status;

    private StatusMeta(int code) {
        this.status = code;
    }

    public int getCode(){
        return status;
    }

    public static StatusMeta valueOf(int code) {
        for (StatusMeta status : StatusMeta.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status invalido");
    }
}

