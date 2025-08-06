package org.meuprojeto.screenmatch;

import org.meuprojeto.screenmatch.model.DadosSerie;
import org.meuprojeto.screenmatch.service.ConsumoAPI;
import org.meuprojeto.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
    private static final String API_KEY = System.getenv("API_KEY");

    public static void main(String[] args) {
        SpringApplication.run(ScreenmatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ConsumoAPI consumoAPI = new ConsumoAPI();
        String endereco = String.format("https://www.omdbapi.com/?t=gilmore+girls&apikey=%s", API_KEY);
        String json = consumoAPI.obterDados(endereco);
        System.out.println(json);

        ConverteDados conversor = new ConverteDados();
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);
    }
}