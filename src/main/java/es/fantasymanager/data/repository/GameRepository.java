package es.fantasymanager.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.fantasymanager.data.entity.Game;

public interface GameRepository extends CrudRepository<Game, Integer> {

	List<Game> findAllByOrderByDateDesc();
}