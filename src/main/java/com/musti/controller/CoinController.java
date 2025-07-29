package com.musti.controller;

 import com.fasterxml.jackson.core.JsonProcessingException;
 import com.fasterxml.jackson.databind.JsonNode;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.musti.modal.Coin;
 import com.musti.service.CoinServiceImpl;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;

 import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinController {

    @Autowired
    private CoinServiceImpl coinService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    ResponseEntity<List<Coin>> getCoinList(@RequestParam("page") int page) throws Exception {

        List<Coin> coins = coinService.getCoins(page);

        return new ResponseEntity<>(coins, HttpStatus.OK);
    }
    @GetMapping("/{coinId}/chart")
    ResponseEntity<JsonNode> getMarketChart(
            @PathVariable String coinId,
            @RequestParam("days") int days

            ) throws Exception {

        String response = coinService.getMarketChart(coinId,days);
        JsonNode node = objectMapper.readTree(response);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    @GetMapping("/search")
    ResponseEntity<JsonNode> searchCoin(@RequestParam("q") String keyword) throws Exception {
        String response = coinService.searchCoin(keyword);
        JsonNode node = objectMapper.readTree(response);
        return new ResponseEntity<>(node, HttpStatus.OK);

    }

    @GetMapping("/top50")
    ResponseEntity<JsonNode> getTop50CoinByMarketCapRank(String market) throws Exception {

        String coin = coinService.getTop50CoinsMarketCapRank();

        JsonNode node = objectMapper.readTree(coin);
        return new ResponseEntity<>(node, HttpStatus.OK);

    }


    @GetMapping("/trading")
    ResponseEntity<JsonNode> getTradingCoin() throws Exception {

        String coin = coinService.getTradingCoins();

        JsonNode node = objectMapper.readTree(coin);
        return new ResponseEntity<>(node, HttpStatus.OK);

    }

    @GetMapping("/details/{coinId}")
    ResponseEntity<JsonNode> getCoinDetails(@PathVariable String coinId) throws Exception {

        String coin = coinService.getCoinDetail(coinId);


        JsonNode node = objectMapper.readTree(coin);
        return new ResponseEntity<>(node, HttpStatus.OK);

    }


}
