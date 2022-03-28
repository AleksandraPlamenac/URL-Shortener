package io.twodigits.urlshortener.repository;

import io.twodigits.urlshortener.model.URLStatistics;
import org.springframework.data.repository.CrudRepository;

public interface StatisticsRepository extends CrudRepository<URLStatistics, String> {
}
