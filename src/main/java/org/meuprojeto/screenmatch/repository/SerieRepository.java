package org.meuprojeto.screenmatch.repository;

import org.meuprojeto.screenmatch.model.Categoria;
import org.meuprojeto.screenmatch.model.Episodio;
import org.meuprojeto.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SerieRepository extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    @Query("""
            select
                s
            from
                Serie s
            where
                s.totalTemporadas <= :totalTemporadas and s.avaliacao >= :avaliacao
            """)
    List<Serie> seriesPorTemporadaEAvaliacao(Integer totalTemporadas, Double avaliacao);

    @Query("""
            select
                e
            from
                Serie s
            join
                s.episodios e
            where
                e.titulo ilike %:trechoEpisodio%
            """)
    List<Episodio> episodisoPorTrecho(String trechoEpisodio);

    @Query("""
            select
                e
            from
                Serie s
            join
                s.episodios e
            where
                s = :serie
            order by
                e.avaliacao
            desc
                limit 5
            """)
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("""
            select
                e
            from
                Serie s
            join
                s.episodios e
            where
                s = :serie and year(e.dataLancamento) >= :anoLancamento
            """)
    List<Episodio> episodiosPorSerieEAno(Serie serie, Integer anoLancamento);
}