package es.fantasymanager.data.repository;

import org.springframework.data.repository.CrudRepository;

import es.fantasymanager.data.entity.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

}