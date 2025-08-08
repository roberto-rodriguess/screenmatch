package org.meuprojeto.screenmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.meuprojeto.screenmatch.service.traducao.DadosTraducao;

import java.net.URLEncoder;

public class ConsultaMyMemory {

    private ConsultaMyMemory() {}

    public static String obterTraducao(String texto) {
        ObjectMapper mapper = new ObjectMapper();
        ConsumoAPI consumo = new ConsumoAPI();

        String textoUrl = URLEncoder.encode(texto);
        String langpair = URLEncoder.encode("en|pt-br");
        String url = "https://api.mymemory.translated.net/get?q=" + textoUrl + "&langpair=" + langpair;

        String json = consumo.obterDados(url);
        DadosTraducao traducao;
        try {
            traducao = mapper.readValue(json, DadosTraducao.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return traducao.dadosResposta().textoTraduzido();
    }
}