package org.meuprojeto.screenmatch.dto;

import org.meuprojeto.screenmatch.model.Episodio;

public record DadosListagemEpisodio(
        Integer temporada,
        String titulo,
        Integer numeroEpisodio) {

    public DadosListagemEpisodio(Episodio dados) {
        this(dados.getTemporada(), dados.getTitulo(), dados.getNumeroEpisodio());
    }
}