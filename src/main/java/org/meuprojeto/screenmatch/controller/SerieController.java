package org.meuprojeto.screenmatch.controller;

import org.meuprojeto.screenmatch.dto.DadosListagemSerie;
import org.meuprojeto.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {
    private final SerieRepository repository;

    @Autowired
    public SerieController(SerieRepository repository) {
        this.repository = repository;
    }

    @GetMapping()
    public List<DadosListagemSerie> obterSeries() {
        return repository.findAll().stream()
                .map(DadosListagemSerie::new)
                .toList();
    }
}