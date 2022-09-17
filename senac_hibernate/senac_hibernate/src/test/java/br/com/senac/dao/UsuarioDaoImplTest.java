/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.senac.dao;

import br.com.senac.entidade.Perfil;
import br.com.senac.entidade.Usuario;
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
public class UsuarioDaoImplTest {

    private Usuario usuario;
    private UsuarioDao usuarioDao;
    private Session sessao;

    public UsuarioDaoImplTest() {
        usuarioDao = new UsuarioDaoImpl();
    }

    @Test
    public void testSalvar() {
        System.out.println("salvar");
        PerfilDaoImplTest pdit = new PerfilDaoImplTest();
        Perfil perfil = pdit.buscarPerfilBD();
        usuario = new Usuario(gerarNome2(), gerarLogin() + gerarNumero(3), gerarSenha(6));
        usuario.setPerfil(perfil);
        sessao = HibernateUtil.abrirConexao();
        usuarioDao.salvarOuAlterar(usuario, sessao);
        sessao.close();
        assertNotNull(usuario.getId());
    }

    @Test
    public void testAlterar() {
        System.out.println("alterar");
        buscarUsuarioBD();
        usuario.setNome(gerarNome2());
        sessao = HibernateUtil.abrirConexao();
        usuarioDao.salvarOuAlterar(usuario, sessao);
        sessao.close();
        sessao = HibernateUtil.abrirConexao();
        Usuario usuarioPesquisado = usuarioDao.pesquisarPorId(usuario.getId(), sessao);
        sessao.close();
        assertEquals(usuarioPesquisado.getNome(), usuario.getNome());
    }

    @Test
    public void testExcluir() {
        System.out.println("excluir");
        buscarUsuarioBD();
        sessao = HibernateUtil.abrirConexao();
        usuarioDao.excluir(usuario, sessao);
        Usuario usuarioExcluido = usuarioDao.pesquisarPorId(usuario.getId(), sessao);
        sessao.close();
        assertNull(usuarioExcluido);
    }

    @Test
    public void testPesquisarPorId() {
        System.out.println("pesquisarPorId");
        buscarUsuarioBD();
        sessao = HibernateUtil.abrirConexao();                              //abre conexão                 
        Usuario usuarioPesquisado = usuarioDao.pesquisarPorId(usuario.getId(), sessao);                   //instancia usuarioDao para usar método pesquisar
        sessao.close();
        assertNotNull(usuarioPesquisado);
    }

    @Test
    public void testPesquisarPorNome() {
        System.out.println("pesquisarPorNome");
        buscarUsuarioBD();
        sessao = HibernateUtil.abrirConexao();
        List<Usuario> usuarios = usuarioDao.pesquisarPorNome(usuario.getNome(), sessao);
        sessao.close();
        assertTrue(usuarios.size() > 0);
    }

    @Test
    public void testPesquisarTodos() {
        System.out.println("pesquisarTodos");
        buscarUsuarioBD();
        sessao = HibernateUtil.abrirConexao();
        List<Usuario> usuarios = usuarioDao.pesquisarTodos(sessao);
        sessao.close();
        mostrar(usuarios);
        assertTrue(!usuarios.isEmpty());
    }

    private void mostrarSorted(List<Usuario> usuarios) {
        usuarios.stream()
                .sorted((usu1, usu2) -> usu1.getNome().compareTo(usu2.getNome()))
                .forEach(usu -> System.out.println(usu.toStringUsuario()));
    }

    private void mostrar(List<Usuario> usuarios) {
        usuarios.stream()
                .forEach(usu -> System.out.println(usu.toStringUsuario()));
    }

    @Test
    public void testLogar() {
        System.out.println("logar");
        buscarUsuarioBD();
        sessao = HibernateUtil.abrirConexao();
        Usuario usuarioLogado = usuarioDao.logar(usuario.getLogin(), usuario.getSenha(), sessao);
        sessao.close();
        assertNotNull(usuarioLogado);
    }

    public Usuario buscarUsuarioBD() {
        sessao = HibernateUtil.abrirConexao();
        Query<Usuario> consulta = sessao.createQuery("from Usuario u");     //HQL    usuario é a classe java.        
        List<Usuario> usuarios = consulta.getResultList();
        sessao.close();                                                     //fecha conexão
        if (usuarios.isEmpty()) {
            testSalvar();                                                   //se vazia, chama o teste salvar e cria usuario
        } else {
            usuario = usuarios.get(0);                                      //busca o primeiro da lista
        }
        return usuario;
    }
}
