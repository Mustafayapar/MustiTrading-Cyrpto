package com.musti.service;

import com.musti.modal.Coin;

import java.util.List;

public interface ICoinService {

    List<Coin> getCoins(int page) throws Exception;

    String getMarketChart(String coinId, int days) throws Exception;

    String getCoinDetail(String coinId) throws Exception;

    Coin findById(String id) throws Exception;

    String searchCoin(String keyword) throws Exception;

    String getTop50CoinsMarketCapRank() throws Exception;

    String getTrendingCoins() throws Exception;





}
