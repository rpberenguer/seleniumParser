package es.fantasymanager.data.repository;

import org.springframework.data.repository.CrudRepository;

import es.fantasymanager.data.entity.Season;

public interface SeasonRepository extends CrudRepository<Season, Integer> {

	Season findByIsCurrentSeason(Boolean isCurrentSeason);
}