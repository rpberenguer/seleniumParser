package es.fantasymanager.data.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.fantasymanager.data.entity.News;

public interface NewsRepository extends CrudRepository<News, Integer> {

	List<News> findByDateTime(LocalDateTime dateTime);
}