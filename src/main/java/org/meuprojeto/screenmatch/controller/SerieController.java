package org.meuprojeto.screenmatch.controller;

import org.meuprojeto.screenmatch.dto.DadosListagemEpisodio;
import org.meuprojeto.screenmatch.dto.DadosListagemSerie;
import org.meuprojeto.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {
    private final SerieService serieService;

    @Autowired
    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }

    @GetMapping()
    public List<DadosListagemSerie> obterSeries() {
        return serieService.obterTodasAsSeries();
    }

    @GetMapping("/top5")
    public List<DadosListagemSerie> obterTop5Series() {
        return serieService.obterTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<DadosListagemSerie> obterLancamentos() {
        return serieService.obterLancamentos();
    }

    @GetMapping("/{id}")
    public DadosListagemSerie obterSeriePorId(@PathVariable Long id) {
        return serieService.obterSeriePorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<DadosListagemEpisodio> obterTodasAsTemporadas(@PathVariable Long id) {
        return serieService.obterTodasAsTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<DadosListagemEpisodio> obterTemporadasPorNumero(@PathVariable Long id, @PathVariable Integer numero) {
        return serieService.obterTemporadasPorNumero(id, numero);
    }

    @GetMapping("/categoria/{nomeCategoria}")
    public List<DadosListagemSerie> obterSeriesPorCategoria(@PathVariable String nomeCategoria) {
        return serieService.obterSeriesPorCategoria(nomeCategoria);
    }
}