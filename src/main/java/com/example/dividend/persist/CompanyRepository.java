package com.example.dividend.persist;


import com.example.dividend.persist.entity.CompanyEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

	boolean existsByTicker(String ticker);

	Optional<CompanyEntity> findByName(String name);

	Page<CompanyEntity> findByNameStartingWithIgnoreCase(String s, Pageable pageable);

	Optional<CompanyEntity> findByTicker(String ticker);

}
