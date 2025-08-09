package org.meuprojeto.screenmatch.model;

public enum Categoria {
    ACAO("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comédia"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crime"),
    ANIMACAO("Animation", "Animação");

    private final String categoriaOmdb;
    private final String categoriaEmPortugues;

    Categoria(String categoriaOmdb, String categoriaEmPortugues) {
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaEmPortugues = categoriaEmPortugues;
    }

    public static Categoria fromString(String texto) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(texto)) return categoria;
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + texto);
    }

    public static Categoria fromPortugues(String texto) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaEmPortugues.equalsIgnoreCase(texto)) return categoria;
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + texto);
    }
}