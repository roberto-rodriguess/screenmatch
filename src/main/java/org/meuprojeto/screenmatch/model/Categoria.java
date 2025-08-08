package org.meuprojeto.screenmatch.model;

public enum Categoria {
    ACAO("Action"),
    ROMANCE("Romance"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    CRIME("Crime"),
    ANIMACAO("Animation");

    private final String cetegoriaOmdb;

    Categoria(String cetegoriaOmdb) {
        this.cetegoriaOmdb = cetegoriaOmdb;
    }

    public static Categoria fromString(String texto) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.cetegoriaOmdb.equalsIgnoreCase(texto)) return categoria;
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + texto);
    }
}