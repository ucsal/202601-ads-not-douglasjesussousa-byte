package br.com.ucsal.olimpiadas.repository;
//Ajuste de estrutura de pacotes e correção do Build Path para execução
import br.com.ucsal.olimpiadas.model.Tentativa;
import java.util.ArrayList;
import java.util.List;

public class TentativaRepository {
    private final List<Tentativa> tentativas = new ArrayList<>();
    private long proximaTentativaId = 1;

    public void salvar(Tentativa t) {
        if (t.getId() == 0) t.setId(proximaTentativaId++);
        tentativas.add(t);
    }

    public List<Tentativa> listarTodas() {
        return new ArrayList<>(tentativas);
    }
}
