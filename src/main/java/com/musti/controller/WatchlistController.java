package com.musti.controller;

import com.musti.modal.Coin;
import com.musti.modal.Users;
import com.musti.modal.Watchlist;
import com.musti.service.CoinServiceImpl;
import com.musti.service.UserServiceImpl;
import com.musti.service.WatchListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    @Autowired
    private  WatchListServiceImpl watchListService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private CoinServiceImpl coinService;

    @GetMapping("/user")
    public ResponseEntity<Watchlist> getUserWatchlist(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Users user = userService.findUserProfileByJwt(jwt);
        Watchlist watchlist= watchListService.findUserWatchlist(user.getId());

        return ResponseEntity.ok(watchlist);

    }

    @GetMapping("/{watchlistId}")
    public ResponseEntity<Watchlist> getWatchlistById(
            @PathVariable Long watchlistId) throws Exception {
        Watchlist watchlist =watchListService.findById(watchlistId);

        return ResponseEntity.ok(watchlist);
    }

    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchlist(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String coinId
    ) throws Exception {
        Users user = userService.findUserProfileByJwt(jwt);
        Coin coin=coinService.findById(coinId);
        Coin addedCoin = watchListService.addCoinToWatchlist(coin, user);
        return ResponseEntity.ok(addedCoin);

    }




}
