package edu.java.domain.repository.jpa;

import edu.java.domain.dto.jpa.LinkEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLinkRepository extends JpaRepository<LinkEntity, String> {
    LinkEntity findByUrl(String url);

    @Query(value = "select * from link where checked_date < now() - interval '60 seconds'", nativeQuery = true)
    List<LinkEntity> findOld();
}
