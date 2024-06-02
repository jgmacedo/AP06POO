package br.edu.idp.tech.poo;

import br.edu.idp.tech.poo.dao.CarroJdbcDao;
import br.edu.idp.tech.poo.dao.CarroJpaDao;
import br.edu.idp.tech.poo.entidade.Carro;
import br.edu.idp.tech.poo.ui.CliIavel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Programa {

    private String sufixo;
    private CliIavel ui;

    public Programa(CliIavel cli) {
        this.ui = cli;
    }

    public enum ExecutionMode {
        JDBC, JPA
    }

    public void executar() {
        // passo 1 - criando a "marca" da execução

        // marca criada para diferenciar o que é cadastrado em cada execução
        sufixo = " /" + gerarLetraAletoria();
        ui.exibirInformacao("Sufixo da execução: " + sufixo);

        // passo 2 - criação do banco de dados

        // criação da tabela
        prepararBD();

        // passo 3 e 4 - várias operações com JDBC e JPA

        // Executa via JDBC
        ui.exibirInformacao("execução via JDBC - início");
        executar(ExecutionMode.JDBC);
        ui.exibirInformacao("execução via JDBC - término");

        // Executa via JPA
        ui.exibirInformacao("execução via JPA - início");
        executar(ExecutionMode.JPA);
        ui.exibirInformacao("execução via JPA - término");
    }

    private void executar(ExecutionMode mode) {
        if (mode == ExecutionMode.JDBC) {
            CarroJdbcDao dao = new CarroJdbcDao();
            criarRegistrosPorJdbc(dao);
            List<Carro> carros = dao.findAll();
            ui.exibirListaCarros(carros);
        } else if (mode == ExecutionMode.JPA) {
            CarroJpaDao dao = new CarroJpaDao();
            criarRegistrosPorJpa(dao);
            List<Carro> carros = dao.findAll();
            ui.exibirListaCarros(carros);
        }
    }


    private void criarRegistrosPorJdbc(CarroJdbcDao dao) {
        // gerador aleatório de carros para "economizar" a inclusão de dados pelo usuário
        CarregadorDados carregador = new CarregadorDados(sufixo);

        Carro novoCarro = carregador.gerarCarro();
        // salvar carro criado
        dao.salvar(novoCarro);

        List<Carro> novosCarros = new ArrayList<>();
        novosCarros.addAll(carregador.gerarListaCarros(2));
        // salvar coleção de carros
        dao.salvar(novosCarros);
    }

    private void criarRegistrosPorJpa(CarroJpaDao dao) {
        // gerador aleatório de carros para "economizar" a inclusão de dados pelo usuário
        CarregadorDados carregador = new CarregadorDados(sufixo);

        Carro novoCarro = carregador.gerarCarro();
        // salvar carro criado
        dao.salvar(novoCarro);

        List<Carro> novosCarros = new ArrayList<>();
        novosCarros.addAll(carregador.gerarListaCarros(3));
        // salvar coleção de carros
        dao.salvar(novosCarros);
    }

    private void prepararBD() {
        try {
            CarroJdbcDao.criarTabela();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private char gerarLetraAletoria() {
        final int NUM_LETRA_BASE = 97;
        final int QUANTIDADE_LETRAS = 25;

        int numero = GeradorNumAletorio.gerarInt(QUANTIDADE_LETRAS);

        return (char) (NUM_LETRA_BASE + numero);
    }

}
