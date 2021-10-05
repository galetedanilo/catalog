package com.galete.catalog.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.galete.catalog.entities.Category;
import com.galete.catalog.entities.projections.CategoryProjection;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

	Optional<Category> findByName(String name);
	
	@Query("SELECT category.id AS id, category.name AS name FROM Category category")
	List<CategoryProjection> listAllCategories();
}
