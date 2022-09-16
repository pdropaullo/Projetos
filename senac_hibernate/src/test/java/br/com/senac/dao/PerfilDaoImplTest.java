/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.senac.dao;

import br.com.senac.entidade.Perfil;
import static br.com.senac.util.Gerador.*;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pedro.abreu
 */
public class PerfilDaoImplTest {

    private Perfil perfil;
    private PerfilDao perfilDao;
    private Session sessao;

    public PerfilDaoImplTest() {
        perfilDao = new PerfilDaoImpl();
    }

    @Test
    public void testSalvar() {
        System.out.println("salvar");
        perfil = new Perfil(gerarSenha(7), "descrição bla, bla");
        sessao = HibernateUtil.abrirConexao();
        perfilDao.salvarOuAlterar(perfil, sessao);
        sessao.close();
        assertNotNull(perfil.getId());
    }

    @Test
    public void testAlterar() {
        System.out.println("alterar");
        buscarPerfilBD();
        perfil.setNome(gerarNome2());
        sessao = HibernateUtil.abrirConexao();
        perfilDao.salvarOuAlterar(perfil, sessao);
        sessao.close();
        sessao = HibernateUtil.abrirConexao();
        Perfil perfilPesquisado = perfilDao.pesquisarPorId(perfil.getId(), sessao);
        sessao.close();
        assertEquals(perfilPesquisado.getNome(), perfil.getNome());
    }

    @Test
    public void testExcluir() {
        System.out.println("excluir");
        buscarPerfilBD();
        sessao = HibernateUtil.abrirConexao();
        perfilDao.excluir(perfil, sessao);
        Perfil perfilExcluido = perfilDao.pesquisarPorId(perfil.getId(), sessao);
        sessao.close();
        assertNull(perfilExcluido);
    }

    @Test
    public void testPesquisarPorId() {
        System.out.println("pesquisarPorId");
        buscarPerfilBD();
        sessao = HibernateUtil.abrirConexao();                              //abre conexão                 
        Perfil perfilPesquisado = perfilDao.pesquisarPorId(perfil.getId(), sessao);                   //instancia perfilDao para usar método pesquisar
        sessao.close();
        assertNotNull(perfilPesquisado);
    }

    @Test
    public void testPesquisarPorNome() {
        System.out.println("pesquisarPorNome");
        buscarPerfilBD();
        sessao = HibernateUtil.abrirConexao();
        List<Perfil> perfis = perfilDao.pesquisarPorNome(perfil.getNome(), sessao);
        sessao.close();
        assertTrue(perfis.size() > 0);
    }

    @Test
    public void testPesquisarTodos() {
        System.out.println("pesquisarTodos");
        buscarPerfilBD();
        sessao = HibernateUtil.abrirConexao();
        List<Perfil> perfis = perfilDao.pesquisarTodos(sessao);
        sessao.close();
        mostrar(perfis);
        assertTrue(!perfis.isEmpty());
    }

    private void mostrar(List<Perfil> perfis) {
        perfis.stream()
                .forEach(per -> System.out.println(per.toStringPerfil()));
    }

    public Perfil buscarPerfilBD() {
        sessao = HibernateUtil.abrirConexao();
        Query<Perfil> consulta = sessao.createQuery("from Perfil p");     //HQL    perfil é a classe java.        
        List<Perfil> perfis = consulta.getResultList();
        sessao.close();                                                     //fecha conexão
        if (perfis.isEmpty()) {
            testSalvar();                                                   //se vazia, chama o teste salvar e cria perfil
        } else {
            perfil = perfis.get(0);                                      //busca o primeiro da lista
        }
        return perfil;
    }
}
