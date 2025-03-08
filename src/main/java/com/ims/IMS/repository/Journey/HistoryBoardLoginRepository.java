package com.ims.IMS.repository.Journey;

import com.ims.IMS.model.SatyJourney.HistoryBoardLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface HistoryBoardLoginRepository extends JpaRepository<HistoryBoardLogin, Integer> {
    // Custom method to find history by MaTaiXe and BienSoXe
    List<HistoryBoardLogin> findByMaTaiXeAndBienSoXe(String MaTaiXe, String BienSoXe);
}
