/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.senac.dao;

import br.com.senac.entidade.Perfil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author pedro.abreu
 */
public class PerfilDaoImpl extends BaseDaoImpl<Perfil, Long> implements PerfilDao {

    @Override
    public Perfil pesquisarPorId(Long id, Session sessao) throws HibernateException {
        return sessao.find(Perfil.class, id);
    }

    @Override
    public List<Perfil> pesquisarPorNome(String nome, Session sessao) throws HibernateException {
        Query<Perfil> consulta = sessao.createQuery("FROM Perfil p WHERE p.nome LIKE :nome ORDER BY p.nome");
        consulta.setParameter("nome", "%" + nome + "%");
        return consulta.getResultList();
    }

    @Override
    public List<Perfil> pesquisarTodos(Session sessao) throws HibernateException {
        Query<Perfil> consulta = sessao.createQuery("FROM Perfil p ORDER BY p.nome ASC");
        return consulta.getResultList();
    }

}
