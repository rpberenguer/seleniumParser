package es.fantasymanager.data.repository;

import org.springframework.data.repository.CrudRepository;

import es.fantasymanager.data.entity.News;

public interface NewsRepository extends CrudRepository<News, Integer> {

}