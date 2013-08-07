package com.gq.meter.object;

import java.util.List;

public class GetAsset {
    List<Asset> assetResult;

    public List<Asset> getAssetResult() {
        return assetResult;
    }

    public void setAssetResult(List<Asset> assetResult) {
        this.assetResult = assetResult;
    }

    public GetAsset(List<Asset> assetResult) {
        super();
        this.assetResult = assetResult;
    }

}
