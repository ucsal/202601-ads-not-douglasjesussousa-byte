package br.com.ucsal.olimpiadas.repository;
//Criação dos Repositórios para isolar a persistência de dados
import br.com.ucsal.olimpiadas.model.Participante;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParticipanteRepository {
    private final List<Participante> participantes = new ArrayList<>();
    private long proximoId = 1;

    public void salvar(Participante p) {
        if (p.getId() == 0) p.setId(proximoId++);
        participantes.add(p);
    }

    public List<Participante> listarTodos() {
        return new ArrayList<>(participantes);
    }

    public Optional<Participante> buscarPorId(long id) {
        return participantes.stream().filter(p -> p.getId() == id).findFirst();
    }
}
