package org.meuprojeto.screenmatch.dto;

import org.meuprojeto.screenmatch.model.Categoria;
import org.meuprojeto.screenmatch.model.Serie;

public record DadosListagemSerie(
        Long id,
        String titulo,
        Integer totalTemporadas,
        Double avaliacao,
        Categoria genero,
        String atores,
        String poster,
        String sinopse) {

    public DadosListagemSerie(Serie dados) {
        this(dados.getId(), dados.getTitulo(), dados.getTotalTemporadas(), dados.getAvaliacao(),
                dados.getGenero(), dados.getAtores(), dados.getPoster(), dados.getSinopse());
    }
}