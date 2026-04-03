package br.com.ucsal.olimpiadas;
//
import java.util.Scanner;
import br.com.ucsal.olimpiadas.model.*;
import br.com.ucsal.olimpiadas.repository.*;
import br.com.ucsal.olimpiadas.service.*;

public class App {

	private final ParticipanteRepository participanteRepo = new ParticipanteRepository();
	private final ProvaRepository provaRepo = new ProvaRepository();
	private final TentativaRepository tentativaRepo = new TentativaRepository();
	private final CalculadoraNota calculadora = new CalculadoraNotaPadrao();

	private final Scanner in = new Scanner(System.in);

	public static void main(String[] args) {
		new App().executar();
	}

	public void executar() {
		seed();
		while (true) {
			System.out.println("\n=== OLIMPÍADA DE QUESTÕES (V2 - SOLID) ===");
			System.out.println("1) Cadastrar participante");
			System.out.println("2) Cadastrar prova");
			System.out.println("3) Cadastrar questão (A–E) em uma prova");
			System.out.println("4) Aplicar prova");
			System.out.println("5) Listar tentativas");
			System.out.println("0) Sair");
			System.out.print("> ");

			switch (in.nextLine()) {
				case "1" -> cadastrarParticipante();
				case "2" -> cadastrarProva();
				case "3" -> cadastrarQuestao();
				case "4" -> aplicarProva();
				case "5" -> listarTentativas();
				case "0" -> { return; }
				default -> System.out.println("opção inválida");
			}
		}
	}

	private void cadastrarParticipante() {
		System.out.print("Nome: ");
		var nome = in.nextLine();
		System.out.print("Email: ");
		var email = in.nextLine();

		if (nome == null || nome.isBlank()) return;

		var p = new Participante();
		p.setNome(nome);
		p.setEmail(email);
		participanteRepo.salvar(p);
		System.out.println("Participante cadastrado: " + p.getId());
	}

	private void cadastrarProva() {
		System.out.print("Título da prova: ");
		var titulo = in.nextLine();
		if (titulo == null || titulo.isBlank()) return;

		var prova = new Prova();
		prova.setTitulo(titulo);
		provaRepo.salvarProva(prova);
		System.out.println("Prova criada: " + prova.getId());
	}

	private void cadastrarQuestao() {
		var provas = provaRepo.listarProvas();
		if (provas.isEmpty()) return;

		var provaId = escolherProva();
		if (provaId == null) return;

		System.out.println("Enunciado:");
		var enunciado = in.nextLine();
		var alternativas = new String[5];
		for (int i = 0; i < 5; i++) {
			char letra = (char) ('A' + i);
			System.out.print("Alternativa " + letra + ": ");
			alternativas[i] = letra + ") " + in.nextLine();
		}

		System.out.print("Correta (A–E): ");
		char correta = Questao.normalizar(in.nextLine().trim().charAt(0));

		var q = new Questao();
		q.setProvaId(provaId);
		q.setEnunciado(enunciado);
		q.setAlternativas(alternativas);
		q.setAlternativaCorreta(correta);
		provaRepo.salvarQuestao(q);
	}

	private void aplicarProva() {
		var participanteId = escolherParticipante();
		var provaId = escolherProva();
		if (participanteId == null || provaId == null) return;

		var questoes = provaRepo.buscarQuestoesPorProva(provaId);
		if (questoes.isEmpty()) return;

		var tentativa = new Tentativa();
		tentativa.setParticipanteId(participanteId);
		tentativa.setProvaId(provaId);

		for (var q : questoes) {
			System.out.println("\nQuestão #" + q.getId() + "\n" + q.getEnunciado());
			if (q.getFenInicial() != null) imprimirTabuleiroFen(q.getFenInicial());
			
			for (var alt : q.getAlternativas()) System.out.println(alt);

			System.out.print("Resposta: ");
			char marcada = Questao.normalizar(in.nextLine().trim().charAt(0));

			var r = new Resposta();
			r.setQuestaoId(q.getId());
			r.setAlternativaMarcada(marcada);
			r.setCorreta(q.isRespostaCorreta(marcada));
			tentativa.getRespostas().add(r);
		}

		tentativaRepo.salvar(tentativa);
		System.out.println("Nota: " + calculadora.calcular(tentativa));
	}

	private void listarTentativas() {
		for (var t : tentativaRepo.listarTodas()) {
			System.out.printf("ID: %d | Part: %d | Prova: %d | Nota: %d%n", 
				t.getId(), t.getParticipanteId(), t.getProvaId(), calculadora.calcular(t));
		}
	}

	private Long escolherParticipante() {
		participanteRepo.listarTodos().forEach(p -> System.out.println(p.getId() + ") " + p.getNome()));
		return Long.parseLong(in.nextLine());
	}

	private Long escolherProva() {
		provaRepo.listarProvas().forEach(p -> System.out.println(p.getId() + ") " + p.getTitulo()));
		return Long.parseLong(in.nextLine());
	}

	private void imprimirTabuleiroFen(String fen) {
		
		System.out.println("FEN: " + fen);
	}

	private void seed() {
		var p = new Prova();
		p.setTitulo("Olimpíada Inicial");
		provaRepo.salvarProva(p);
		
		var q = new Questao();
		q.setProvaId(p.getId());
		q.setEnunciado("Mate em 1?");
		q.setAlternativas(new String[]{"A)Qc8#","B)x","C)x","D)x","E)x"});
		q.setAlternativaCorreta('A');
		q.setFenInicial("6k1/5ppp/8/8/8/7Q/6PP/6K1 w - - 0 1");
		provaRepo.salvarQuestao(q);
	}
}
