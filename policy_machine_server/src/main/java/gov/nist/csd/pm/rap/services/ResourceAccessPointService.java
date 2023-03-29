package gov.nist.csd.pm.rap.services;

import java.util.Map;
import java.util.Set;

import gov.nist.csd.pm.common.constants.AssetStatus;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.services.Service;
import gov.nist.csd.pm.rap.AssetType;
import gov.nist.csd.pm.rap.model.Asset;

public class ResourceAccessPointService extends Service {

	public ResourceAccessPointService(String owner) throws PMException {
		super(owner);
	}

	public static <K, V> K getKey(Map<K, V> map, V value) {
		return map.entrySet().stream().filter(entry -> value.equals(entry.getValue())).findFirst()
				.map(Map.Entry::getKey).orElse(null);
	}

	public Asset createAsset(Asset asset) throws PMException {
		if (null == asset.getName() && asset.getName().isEmpty()) {
			throw new IllegalArgumentException("Asset cannot created with null or empty name");
		} else if (null == AssetType.toType(asset.getType())) {
			throw new IllegalArgumentException("Asset type not found");
		} else if (exists(asset.getName())) {
			throw new IllegalArgumentException("Asset already exists");
		}

		// name, type, status, link, owner
		return getAssetPAP().createAsset(asset.getId(), asset.getName(), AssetType.toType(asset.getType()),
				AssetStatus.UNSECURED, null, getOwner(), true);
	}

	public void updateAsset(Asset asset) throws PMException {
		if (0 == asset.getId()) {
			throw new IllegalArgumentException(String.format("Asset with %d does not exists", asset.getId()));
		} else if (null == asset.getName() && asset.getName().isEmpty()) {
			throw new IllegalArgumentException("Asset cannot created with null or empty name");
		} else if (null == AssetType.toType(asset.getType())) {
			throw new IllegalArgumentException("Asset type not found");
		} else if (!exists(asset.getId())) {
			throw new IllegalArgumentException("Asset already exists");
		} else if (null == AssetStatus.toAssetStatus(asset.getStatus())) {
			throw new IllegalArgumentException("Asset status not found!");
		}
		getAssetPAP().updateAsset(asset.getId(), asset.getName(), AssetStatus.toAssetStatus(asset.getStatus()),
				asset.getLink(), getOwner(), true);
	}

	public void deleteAsset(long id) throws PMException {
		if (0 == id) {
			throw new IllegalArgumentException(String.format("Asset with %d does not exists", id));
		} else if (!exists(id)) {
			throw new IllegalArgumentException("Asset does not exists");
		}
		getAssetPAP().deleteAsset(id, getOwner());
	}

	public Asset getAsset(long id) throws PMException {
		if (0 == id) {
			throw new IllegalArgumentException(String.format("Asset with %d does not exists", id));
		} else if (!exists(id)) {
			throw new IllegalArgumentException("Asset does not exists");
		}
		return getAssetPAP().getAsset(id, getOwner(), true);
	}

	public Set<Asset> getAssets() throws PMException {
		return getAssetPAP().getAssets(getOwner(), true);
	}

	private boolean exists(long id) throws PMException {
		if (0 == id) {
			throw new IllegalArgumentException(String.format("Asset with %d does not exists", id));
		}
		return getAssetPAP().exists(id, getOwner());
	}

	private boolean exists(String name) throws PMException {
		if (null == name || name.isEmpty()) {
			throw new IllegalArgumentException(String.format("Asset with %s does not exists", name));
		}
		return getAssetPAP().exists(name, getOwner());
	}

	public boolean linkAsset(long assetID, long policyID) throws PMException {
		if (0 == assetID) {
			throw new IllegalArgumentException(String.format("Asset with %d does not exists", assetID));
		} else if (0 == policyID) {
			throw new IllegalArgumentException(String.format("Policy with %d does not exists", policyID));
		}
		return getAssetPAP().linkAsset(assetID, policyID);
	}
}
