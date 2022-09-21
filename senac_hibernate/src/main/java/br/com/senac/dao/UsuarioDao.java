/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.senac.dao;

import br.com.senac.entidade.Usuario;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author pedro.abreu
 */
public interface UsuarioDao extends BaseDao<Usuario, Long> { //<o quer salvar, tipo da chave primaria>

    List<Usuario> pesquisarPorNome(String nome, Session sessao) throws HibernateException;

    List<Usuario> pesquisarTodos(Session sessao) throws HibernateException;

    public Usuario logar(String login, String senha, Session sessao) throws HibernateException;
    // metodo logar, com parametro login e senha além do session
}
