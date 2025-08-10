package org.meuprojeto.screenmatch.principal;

import org.meuprojeto.screenmatch.model.Categoria;
import org.meuprojeto.screenmatch.model.DadosSerie;
import org.meuprojeto.screenmatch.model.DadosTemporada;
import org.meuprojeto.screenmatch.model.Episodio;
import org.meuprojeto.screenmatch.model.Serie;
import org.meuprojeto.screenmatch.repository.SerieRepository;
import org.meuprojeto.screenmatch.service.ConsumoAPI;
import org.meuprojeto.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private final Scanner scanner = new Scanner(System.in);
    private final ConsumoAPI consumo = new ConsumoAPI();
    private final ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=" + System.getenv("API_KEY");

    private final SerieRepository repositorio;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        int opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por título
                    5 - Buscar séries por ator
                    6 - Top 5 séries
                    7 - Buscar séries por categoria
                    8 - Filtrar séries
                    9 - Buscar episódios por trecho
                    10 - Top 5 episódios por série
                    11 - Buscar episódios a partir de uma data
                    
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> buscarSerieWeb();
                case 2 -> buscarEpisodioPorSerie();
                case 3 -> listarSeriesBuscadas();
                case 4 -> buscarSeriePorTitulo();
                case 5 -> buscarSeriesPorAtor();
                case 6 -> buscarTop5Series();
                case 7 -> buscarSeriesPorCategoria();
                case 8 -> filtrarSeriesPorTemporadaEAvaliacao();
                case 9 -> buscarEpisodioPorTrecho();
                case 10 -> topEpisodiosPorSerie();
                case 11 -> buscarEpisodiosDepoisDeUmaData();

                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie);
        System.out.printf("A série %s foi salva no banco de dados!%n", serie.getTitulo());
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.print("Digite o nome da série para busca: ");
        String nomeSerie = scanner.nextLine();

        String nomeSerieForamatado = nomeSerie.replace(" ", "+");
        String json = consumo.obterDados(ENDERECO + nomeSerieForamatado + API_KEY);
        return conversor.obterDados(json, DadosSerie.class);
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        Optional<Serie> serie = buscarSerie();
        if (serie.isPresent()) {
            Serie serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                String json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }

            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .toList();

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);

        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void listarSeriesBuscadas() {
        List<Serie> series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        Optional<Serie> serieBuscada = buscarSerie();
        if (serieBuscada.isPresent()) {
            System.out.println("Dados da série: " + serieBuscada.get());
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarSeriesPorAtor() {
        System.out.print("Digite o nome do ator: ");
        String nomeAtor = scanner.nextLine();
        System.out.print("Digite o valor da avaliação da série: ");
        double avaliacao = scanner.nextDouble();

        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.printf("Séries em que o ator %s trabalhou:%n", nomeAtor);
        seriesEncontradas.forEach(s ->
                System.out.printf("%s | avaliação: %.2f%n%n", s.getTitulo(), s.getAvaliacao())
        );
    }

    private void buscarTop5Series() {
        List<Serie> top5Series = repositorio.findTop5ByOrderByAvaliacaoDesc();
        top5Series.forEach(s ->
                System.out.printf("%s | avaliação: %.2f%n", s.getTitulo(), s.getAvaliacao()));
    }

    private void buscarSeriesPorCategoria() {
        System.out.print("Digite o nome do categoria: ");
        String nomeCategoria = scanner.nextLine();

        Categoria categoria = Categoria.fromPortugues(nomeCategoria);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Séries da categoria: " + nomeCategoria);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void filtrarSeriesPorTemporadaEAvaliacao() {
        System.out.print("Digite a quantidade de temporadas: ");
        int totalTemporadas = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Digite o valor da avaliação: ");
        double avaliacao = scanner.nextDouble();
        scanner.nextLine();

        List<Serie> seriesFiltradas = repositorio.seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        System.out.println("Séries filtradas");
        seriesFiltradas.forEach(s ->
                System.out.printf("%s | avaliação: %.2f%n", s.getTitulo(), s.getAvaliacao()));
    }

    private void buscarEpisodioPorTrecho() {
        System.out.print("Digite o nome do episódio: ");
        String trechoEpisodio = scanner.nextLine();

        List<Episodio> episodiosEncontrados = repositorio.episodisoPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Série: %s | Temporada %d | Episódio %d - %s%n",
                        e.getSerie().getTitulo(), e.getTemporada(),
                        e.getNumeroEpisodio(), e.getTitulo()));
    }

    private void topEpisodiosPorSerie() {
        Optional<Serie> serieBuscada = buscarSerie();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);

            topEpisodios.forEach(e ->
                    System.out.printf("Série: %s | Temporada %d | Episódio %d - %s | %.2f%n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarEpisodiosDepoisDeUmaData() {
        Optional<Serie> serieBuscada = buscarSerie();
        if (serieBuscada.isPresent()) {
            System.out.println("Dados da série: " + serieBuscada.get());
            System.out.print("Digite o ano limite de lançamento: ");
            int anoLancamento = scanner.nextInt();
            scanner.nextLine();

            Serie serie = serieBuscada.get();
            List<Episodio> episodiosPorAno = repositorio.episodiosPorSerieEAno(serie, anoLancamento);
            episodiosPorAno.forEach(System.out::println);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private Optional<Serie> buscarSerie() {
        System.out.print("Digite o nome da série: ");
        String nomeSerie = scanner.nextLine();
        return repositorio.findByTituloContainingIgnoreCase(nomeSerie);
    }
}