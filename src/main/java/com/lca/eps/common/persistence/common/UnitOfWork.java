package com.lca.eps.common.persistence.common;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class UnitOfWork {

	private SessionFactory sessionFactory;

	private Transaction tx;

	private Session session;

	public UnitOfWork(SessionFactory sessionFact) {
		this.sessionFactory = sessionFact;
		this.init();
	}

	public void init() {
		this.session = sessionFactory.openSession();
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void beginTransaction() {
		this.tx = session.beginTransaction();
	}

	public void commit() {
		if (this.tx != null)
			this.tx.commit();
	}

	public void rollback() {
		if (this.tx != null)
			this.tx.rollback();
	}

	public void endSession() {
		if (this.session != null)
			this.session.close();
	}
}
