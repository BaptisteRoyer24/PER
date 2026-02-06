package com.airfrance.admin.repository;

import com.airfrance.admin.domain.Vol;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vol entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VolRepository extends JpaRepository<Vol, Long>, JpaSpecificationExecutor<Vol> {}
