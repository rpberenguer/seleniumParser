package es.fantasymanager.data.repository;

import org.springframework.data.repository.CrudRepository;

import es.fantasymanager.data.entity.Parameter;

public interface ParameterRepository extends CrudRepository<Parameter, Integer> {

	Parameter findByCode(String code);
}