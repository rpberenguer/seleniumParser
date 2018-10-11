package es.fantasymanager.data.repository;

import org.springframework.data.repository.CrudRepository;

import es.fantasymanager.data.entity.Team;

public interface TeamRepository extends CrudRepository<Team, Integer> {

	Team findByShortCode(String shortCode);
}