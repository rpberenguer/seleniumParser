package es.fantasymanager.data.repository;

import org.springframework.data.repository.CrudRepository;

import es.fantasymanager.data.entity.Player;

public interface PlayerRepository extends CrudRepository<Player, Integer> {

	Player findPlayerByNbaId(String nbaId);
	Player findPlayerByName(String name);
}