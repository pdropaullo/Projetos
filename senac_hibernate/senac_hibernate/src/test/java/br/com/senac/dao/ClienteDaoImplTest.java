/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.senac.dao;

import br.com.senac.entidade.Cliente;
import static br.com.senac.util.Gerador.*;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author pedro.abreu
 */
public class ClienteDaoImplTest {

    private Cliente cliente;
    private ClienteDao clienteDao;
    private Session sessao;

    public ClienteDaoImplTest() {
        clienteDao = new ClienteDaoImpl();
    }

    @Test
    public void testSalvar() {
        System.out.println("salvar");
        cliente = new Cliente(gerarNome2(), gerarCpf(), gerarCep(), Double.valueOf(gerarNumero(5)));
        sessao = HibernateUtil.abrirConexao();
        clienteDao.salvarOuAlterar(cliente, sessao);
        sessao.close();
        assertNotNull(cliente.getId());
    }

    @Test
    public void testAlterar() {
        System.out.println("alterar");
        buscarClienteBD();
        cliente.setNome(gerarNome2());
        sessao = HibernateUtil.abrirConexao();
        clienteDao.salvarOuAlterar(cliente, sessao);
        sessao.close();
        sessao = HibernateUtil.abrirConexao();
        Cliente clientePesquisado = clienteDao.pesquisarPorId(cliente.getId(), sessao);
        sessao.close();
        assertEquals(clientePesquisado.getNome(), cliente.getNome());
    }

    @Test
    public void testExcluir() {
        System.out.println("excluir");
        buscarClienteBD();
        sessao = HibernateUtil.abrirConexao();
        clienteDao.excluir(cliente, sessao);
        Cliente clienteExcluido = clienteDao.pesquisarPorId(cliente.getId(), sessao);
        sessao.close();
        assertNull(clienteExcluido);
    }

    @Test
    public void testPesquisarPorId() {
        System.out.println("pesquisarPorId");
        buscarClienteBD();
        sessao = HibernateUtil.abrirConexao();                              //abre conexão                 
        Cliente clientePesquisado = clienteDao.pesquisarPorId(cliente.getId(), sessao);                   //instancia clienteDao para usar método pesquisar
        sessao.close();
        assertNotNull(clientePesquisado);
    }

    @Test
    public void testPesquisarPorNome() {
        System.out.println("pesquisarPorNome");
        buscarClienteBD();
        sessao = HibernateUtil.abrirConexao();
        List<Cliente> clientes = clienteDao.pesquisarPorNome(cliente.getNome(), sessao);
        sessao.close();
        assertTrue(clientes.size() > 0);
    }

    @Test
    public void testPesquisarTodos() {
        System.out.println("pesquisarTodos");
        buscarClienteBD();
        sessao = HibernateUtil.abrirConexao();
        List<Cliente> clientes = clienteDao.pesquisarTodos(sessao);
        sessao.close();
        mostrar(clientes);
        assertTrue(!clientes.isEmpty());
    }

    private void mostrarSorted(List<Cliente> clientes) {
        clientes.stream()
                .sorted((cli1, cli2) -> cli1.getNome().compareTo(cli2.getNome()))
                .forEach(cli -> System.out.println(cli.toStringCliente()));
    }

    private void mostrar(List<Cliente> clientes) {
        clientes.stream()
                .forEach(cli -> System.out.println(cli.toStringCliente()));
    }

    public Cliente buscarClienteBD() {
        sessao = HibernateUtil.abrirConexao();
        Query<Cliente> consulta = sessao.createQuery("from Cliente c");     //HQL    cliente é a classe java.        
        List<Cliente> clientes = consulta.getResultList();
        sessao.close();                                                     //fecha conexão
        if (clientes.isEmpty()) {
            testSalvar();                                                   //se vazia, chama o teste salvar e cria cliente
        } else {
            cliente = clientes.get(0);                                      //busca o primeiro da lista
        }
        return cliente;
    }
}
