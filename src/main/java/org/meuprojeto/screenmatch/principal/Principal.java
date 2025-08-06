package org.meuprojeto.screenmatch.principal;

import org.meuprojeto.screenmatch.model.DadosEpisodio;
import org.meuprojeto.screenmatch.model.DadosSerie;
import org.meuprojeto.screenmatch.model.DadosTemporada;
import org.meuprojeto.screenmatch.service.ConsumoAPI;
import org.meuprojeto.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private final Scanner scanner = new Scanner(System.in);
    private final ConsumoAPI consumo = new ConsumoAPI();
    private final ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=" + System.getenv("API_KEY");

    public void exibeMenu() {
        System.out.print("Digite o nome da série para busca: ");
        String nomeSerie = scanner.nextLine().replaceAll(" ", "+");

        String json = consumo.obterDados(ENDERECO + nomeSerie + API_KEY);
        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie + "&Season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

//        for (int i = 0; i < dadosSerie.totalTemporadas(); i++) {
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.printf("Temp %01d Ep %d: %s%n", (i + 1), (j + 1), episodiosTemporada.get(j).titulo());
//            }
//            System.out.println();
//        }

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
    }
}