package com.mentoria.back_end_mentoria.usuario.vo;

public class Senha {

    private String valor;

    public Senha(String valor) {
        if (validarSenha(valor)) {
            this.valor = valor;
        } else {
            throw new IllegalArgumentException("Senha inválida. " +
                    "Deve ter no mínimo 8 caracteres, " +
                    "pelo menos uma letra maiúscula," +
                    " um número e um caractere especial.");
        }
    }

    public String getValor() {
        return valor;
    }

    private boolean validarSenha(String senha) {
        if (senha == null) return false;
        // Regex:
        // ^                 -> início
        // (?=.*[A-Z])       -> pelo menos uma letra maiúscula
        // (?=.*[0-9])       -> pelo menos um número
        // (?=.*[^a-zA-Z0-9])-> pelo menos um caractere especial
        // .{8,}             -> no mínimo 8 caracteres
        // $                 -> fim
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$";
        return senha.matches(regex);
    }
}

