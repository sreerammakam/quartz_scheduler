package com.lca.eps.common.persistence.dao;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lca.eps.common.persistence.common.UnitOfWork;
import com.lca.eps.common.persistence.entity.PropertyValueEntity;

import jakarta.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * IMPORTANT: For complex queries (involving multiple joins, unions, etc.),
 * please ensure to add logs at both the start and end of the method for
 * enhanced traceability and debugging.
 * <p>
 * Example:
 * Start of the method:
 * logger.info("<method_name> started...");
 * End of the method:
 * logger.info("<method_name> finished");
 * <p>
 * Replace <method_name> with the actual name of the method.
 */
@Repository
public class PropertyValueDao {

	private final SessionFactory sessionFactory;

	@Autowired
	public PropertyValueDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Map<String, String> findPropertyNameToPropertyValue() {
		UnitOfWork uow = new UnitOfWork(sessionFactory);
		try {
			String hql = """
					Select p
					from PropertyValueEntity p
					""";
			List<PropertyValueEntity> props = uow.getSession()
					.createQuery(hql, PropertyValueEntity.class)
					.getResultList();
			return Optional.ofNullable(props)
					.orElse(new ArrayList<>())
					.stream()
					.filter(p -> (StringUtils.isNoneBlank(p.getPropertyName(), p.getPropertyValue())))
					.collect(Collectors.toMap(PropertyValueEntity::getPropertyName, PropertyValueEntity::getPropertyValue));
		} finally {
			uow.endSession();
		}
	}

	public PropertyValueEntity findByPropertyName(String propertyName, UnitOfWork uow) {
		try {
			String hql = """
					select p
					from PropertyValueEntity p
					where p.propertyName=:name
					""";
			return uow.getSession()
					.createSelectionQuery(hql, PropertyValueEntity.class)
					.setParameter("name", propertyName)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;    //backward compatibility
		}
	}

	public List<PropertyValueEntity> findByPropertyName(List<String> propertyNames, UnitOfWork uow) {
		String hql = """
				select p
				from PropertyValueEntity p
				where p.propertyName in (:propNames)
				""";
		return uow.getSession()
				.createSelectionQuery(hql, PropertyValueEntity.class)
				.setParameter("propNames", propertyNames)
				.getResultList();
	}

	public void updatePropertyValueByName(String propertyName, String propertyValue, UnitOfWork uow) {
		String hql = """
				update PropertyValueEntity p
				set p.propertyValue=:propertyValue,
					p.userUpdated=:userupdated,
					p.dateUpdated=:dateUpdated
				where p.propertyName=:name
				""";
		uow.getSession().createMutationQuery(hql)
				.setParameter("name", propertyName)
				.setParameter("propertyValue", propertyValue)
				.setParameter("userupdated", "System")
				.setParameter("dateUpdated", new Date())
				.executeUpdate();
	}
}
