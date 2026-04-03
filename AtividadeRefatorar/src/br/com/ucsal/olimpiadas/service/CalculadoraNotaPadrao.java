package br.com.ucsal.olimpiadas.service;
//Implementação de CalculadoraNota utilizando Interface
import br.com.ucsal.olimpiadas.model.Tentativa;
import br.com.ucsal.olimpiadas.model.Resposta;

public class CalculadoraNotaPadrao implements CalculadoraNota {
    @Override
    public int calcular(Tentativa tentativa) {
        int acertos = 0;
        for (Resposta r : tentativa.getRespostas()) {
            if (r.isCorreta()) {
                acertos++;
            }
        }
        return acertos;
    }
}
