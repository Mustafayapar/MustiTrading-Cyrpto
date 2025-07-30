package com.musti.service;

import com.musti.modal.Coin;
import com.musti.modal.Users;
import com.musti.modal.Watchlist;

public interface IWatchlistService {

    Watchlist findUserWatchlist(Long userId) throws Exception;

    Watchlist createWatchlist(Users user);

    Watchlist findById(Long id) throws Exception;

    Coin addCoinToWatchlist(Coin coin, Users user) throws Exception;
}
