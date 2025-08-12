package org.meuprojeto.screenmatch.service;

import org.meuprojeto.screenmatch.dto.DadosListagemSerie;
import org.meuprojeto.screenmatch.model.Serie;
import org.meuprojeto.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SerieService {
    private final SerieRepository repository;

    @Autowired
    public SerieService(SerieRepository repository) {
        this.repository = repository;
    }

    public List<DadosListagemSerie> obterTodasAsSeries() {
        return converteDados(repository.findAll());
    }

    public List<DadosListagemSerie> obterTop5Series() {
        return converteDados(repository.findTop5ByOrderByAvaliacaoDesc());
    }

    public List<DadosListagemSerie> obterLancamentos() {
        return converteDados(repository.encontrarEpisodiosMaisRecentes());
    }

    public DadosListagemSerie obterSeriePorId(Long id) {
        Optional<Serie> serie = repository.findById(id);
        return serie.map(DadosListagemSerie::new).orElse(null);
    }

    private List<DadosListagemSerie> converteDados(List<Serie> series) {
        return series.stream()
                .map(DadosListagemSerie::new)
                .toList();
    }
}