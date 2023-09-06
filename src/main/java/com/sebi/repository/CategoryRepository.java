package com.sebi.repository;

import com.sebi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("Select c from Category c WHERE c.name =:name")
    public Category findByName(@Param("name") String name);
    @Query("Select c from Category c WHERE c.name =:name AND c.parentCategory.name =:parentCategoryName")

    public Category findByNameAndParant(@Param("name") String name, @Param("parentCategoryName") String parentCategoryName);
}
