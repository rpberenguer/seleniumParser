package es.fantasymanager.data.repository;

import org.springframework.data.repository.CrudRepository;

import es.fantasymanager.data.entity.FantasyTeam;

public interface FantasyTeamRepository extends CrudRepository<FantasyTeam, Integer> {

	FantasyTeam findByTeamName(String tameName);
}