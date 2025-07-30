package com.musti.controller;

import com.musti.modal.Asset;
import com.musti.modal.Users;
import com.musti.service.AssetServiceImpl;
import com.musti.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset")
public class AssetController {

    @Autowired
    private   AssetServiceImpl assetService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("{assetId}")
    public ResponseEntity<Asset> getAssetById(
            @PathVariable Long assetId) throws Exception {
        Asset asset= assetService.getAssetById(assetId);
        return ResponseEntity.ok().body(asset);
    }
    @GetMapping("/coin/{coinId}/user")
    public ResponseEntity<Asset> getAssetByUserIdAndCoinId(
            @PathVariable String coinId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Users user =userService.findUserProfileByJwt(jwt);
        Asset asset = assetService.findAssetByUSerIdAndCoinId(user.getId(), coinId);
        return ResponseEntity.ok().body(asset);
    }

    @GetMapping
    public ResponseEntity<List<Asset>> getAssetForUser(
            @RequestHeader("Authorization") String jwt) throws Exception {
        Users user =userService.findUserProfileByJwt(jwt);
        List<Asset> assets =assetService.getAssetsByUserId(user.getId());
        return ResponseEntity.ok().body(assets);

    }

}
