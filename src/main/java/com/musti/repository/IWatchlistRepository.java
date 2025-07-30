package com.musti.repository;

import com.musti.modal.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWatchlistRepository extends JpaRepository<Watchlist, Long> {


    Watchlist findByUserId(Long userId);
}
