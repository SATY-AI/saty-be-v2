package com.ims.IMS.repository.Journey;

import com.ims.IMS.model.SatyJourney.Data2Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
@RepositoryRestResource
public interface Data2BoardRepository extends JpaRepository<Data2Board, Integer> {
    Optional<Data2Board> findByCustomID(String customID);
}
