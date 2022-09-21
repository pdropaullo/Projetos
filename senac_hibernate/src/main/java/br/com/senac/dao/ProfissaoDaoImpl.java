/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.senac.dao;

import br.com.senac.entidade.Profissao;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author pedro.abreu
 */
public class ProfissaoDaoImpl extends BaseDaoImpl<Profissao, Long> implements ProfissaoDao {

    @Override
    public Profissao pesquisarPorId(Long id, Session sessao) throws HibernateException {
        return sessao.find(Profissao.class, id);
    }

    @Override
    public List<Profissao> pesquisarPorNome(String nome, Session sessao) throws HibernateException {
        Query<Profissao> consulta = sessao.createQuery("FROM Profissao pr WHERE p.nome LIKE :nome ORDER BY p.nome");
        consulta.setParameter("nome", "%" + nome + "%");
        return consulta.getResultList();
    }

    @Override
    public List<Profissao> pesquisarTodos(Session sessao) throws HibernateException {
        Query<Profissao> consulta = sessao.createQuery("FROM Profissao pr ORDER BY p.nome ASC");
        return consulta.getResultList();
    }
}
