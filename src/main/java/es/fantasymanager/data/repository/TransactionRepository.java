package es.fantasymanager.data.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.fantasymanager.data.entity.FantasyTeam;
import es.fantasymanager.data.entity.Player;
import es.fantasymanager.data.entity.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

	List<Transaction> findByDateAndFantasyTeamAndPlayerAddedAndPlayerDropped(Date date, FantasyTeam fantasyTeam,
			Player playerAdded, Player playerDropped);

	List<Transaction> findByDateAndFantasyTeamAndPlayerAdded(Date date, FantasyTeam fantasyTeam, Player playerAdded);

	List<Transaction> findByDateAndFantasyTeamAndPlayerDropped(Date date, FantasyTeam fantasyTeam,
			Player playerDropped);
}