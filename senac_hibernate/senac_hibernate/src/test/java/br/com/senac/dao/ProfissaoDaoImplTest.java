/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.senac.dao;

import br.com.senac.entidade.Profissao;
import java.util.List;
import org.hibernate.Session;
import org.junit.Test;
import static org.junit.Assert.*;
import static br.com.senac.util.Gerador.*;
import org.hibernate.query.Query;

/**
 *
 * @author pedro.abreu
 */
public class ProfissaoDaoImplTest {

    private Profissao profissao;
    private ProfissaoDao profissaoDao;
    private Session sessao;

    public ProfissaoDaoImplTest() {
        profissaoDao = new ProfissaoDaoImpl();
    }

    @Test
    public void testSalvar() {
        System.out.println("salvar");
        profissao = new Profissao(gerarCidade(), "descrição bla, bla");
        sessao = HibernateUtil.abrirConexao();
        profissaoDao.salvarOuAlterar(profissao, sessao);
        sessao.close();
        assertNotNull(profissao.getId());
    }

    @Test
    public void testAlterar() {
        System.out.println("alterar");
        buscarProfissaoBD();
        profissao.setNome(gerarCidade());
        sessao = HibernateUtil.abrirConexao();
        profissaoDao.salvarOuAlterar(profissao, sessao);
        sessao.close();
        sessao = HibernateUtil.abrirConexao();
        Profissao profissaoPesquisado = profissaoDao.pesquisarPorId(profissao.getId(), sessao);
        sessao.close();
        assertEquals(profissaoPesquisado.getNome(), profissao.getNome());
    }

    @Test
    public void testExcluir() {
        System.out.println("excluir");
        buscarProfissaoBD();
        sessao = HibernateUtil.abrirConexao();
        profissaoDao.excluir(profissao, sessao);
        Profissao profissaoExcluido = profissaoDao.pesquisarPorId(profissao.getId(), sessao);
        sessao.close();
        assertNull(profissaoExcluido);
    }

    @Test
    public void testPesquisarPorId() {
        System.out.println("pesquisarPorId");
        buscarProfissaoBD();
        sessao = HibernateUtil.abrirConexao();
        Profissao profissaoPesquisado = profissaoDao.pesquisarPorId(profissao.getId(), sessao);
        sessao.close();
        assertNotNull(profissaoPesquisado);
    }

    @Test
    public void testPesquisarPorNome() {
        System.out.println("pesquisarPorNome");
        buscarProfissaoBD();
        sessao = HibernateUtil.abrirConexao();
        List<Profissao> profissoes = profissaoDao.pesquisarPorNome(profissao.getNome(), sessao);
        sessao.close();
        assertTrue(profissoes.size() > 0);
    }

    @Test
    public void testPesquisarTodos() {
        System.out.println("pesquisarTodos");
        buscarProfissaoBD();
        sessao = HibernateUtil.abrirConexao();
        List<Profissao> profissoes = profissaoDao.pesquisarTodos(sessao);
        sessao.close();
        mostrar(profissoes);
        assertTrue(!profissoes.isEmpty());
    }

    private void mostrar(List<Profissao> profissoes) {
        profissoes.stream()
                .forEach(pro -> System.out.println(pro.toStringProfissao()));
    }

    public Profissao buscarProfissaoBD() {
        sessao = HibernateUtil.abrirConexao();
        Query<Profissao> consulta = sessao.createQuery("from Profissao pr");
        consulta.setMaxResults(1);
        List<Profissao> profissoes = consulta.getResultList();
        sessao.close();
        if (profissoes.isEmpty()) {
            testSalvar();
        } else {
            profissao = profissoes.get(0);
        }
        return profissao;
    }

}
