package com.musti.service;

import com.musti.modal.Asset;
import com.musti.modal.Coin;
import com.musti.modal.Users;

import java.util.List;

public interface IAssetService {

    Asset createAsset(Users user, Coin coin, double quantity);
    Asset getAssetById(Long assetId) throws Exception;
    Asset getAssetByUserIdAndId(Long userId, Long assetId);
    List<Asset> getAssetsByUserId(Long userId);
    Asset updateAsset(Long assetId,     double quantity) throws Exception;

    Asset findAssetByUSerIdAndCoinId(Long userId, String coinId);

    void deleteAsset(Long assetId);


}
