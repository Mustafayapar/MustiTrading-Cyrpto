package com.musti.repository;

import com.musti.modal.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findByUserId(Long userId);
    Asset findByUserIdAndCoinId(Long userId, String coinId);


}

