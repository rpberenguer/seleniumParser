package es.fantasymanager.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import es.fantasymanager.data.entity.Player;

public interface PlayerRepository extends CrudRepository<Player, Integer> {

	Player findPlayerByNbaId(String nbaId);

	Player findPlayerByName(String name);

	@Query("Select p from Player p where LOWER(p.name) like LOWER(concat('%', ?1, '%', ?2, '%'))")
	List<Player> findPlayerByNameContaining(@Param("name") String name, @Param("surname") String surname);
}