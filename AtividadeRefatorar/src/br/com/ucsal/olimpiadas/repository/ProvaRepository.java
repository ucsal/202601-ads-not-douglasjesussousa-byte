package br.com.ucsal.olimpiadas.repository;

import br.com.ucsal.olimpiadas.model.Prova;
import br.com.ucsal.olimpiadas.model.Questao;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProvaRepository {
    private final List<Prova> provas = new ArrayList<>();
    private final List<Questao> questoes = new ArrayList<>(); // Questão pertence à prova
    private long proximaProvaId = 1;
    private long proximaQuestaoId = 1;

    public void salvarProva(Prova p) {
        if (p.getId() == 0) p.setId(proximaProvaId++);
        provas.add(p);
    }

    public void salvarQuestao(Questao q) {
        if (q.getId() == 0) q.setId(proximaQuestaoId++);
        questoes.add(q);
    }

    public List<Prova> listarProvas() { return new ArrayList<>(provas); }

    public List<Questao> buscarQuestoesPorProva(long provaId) {
        return questoes.stream().filter(q -> q.getProvaId() == provaId).toList();
    }
    
    public Optional<Prova> buscarProvaPorId(long id) {
        return provas.stream().filter(p -> p.getId() == id).findFirst();
    }
}
