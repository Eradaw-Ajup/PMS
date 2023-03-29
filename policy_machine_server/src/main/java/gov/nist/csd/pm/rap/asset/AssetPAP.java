package gov.nist.csd.pm.rap.asset;

import java.util.Set;

import gov.nist.csd.pm.common.constants.AssetStatus;
import gov.nist.csd.pm.common.exceptions.DBException;
import gov.nist.csd.pm.rap.AssetType;
import gov.nist.csd.pm.rap.model.Asset;

public class AssetPAP implements AssetMachine {
	private AssetMachine dbAsset;

	public AssetPAP(AssetMachine assetMachine) {
		this.dbAsset = assetMachine;
	}

	@Override
	public Asset createAsset(long id, String name, AssetType type, AssetStatus status, String link, String owner, boolean isActive)
			throws DBException {
		return dbAsset.createAsset(id, name, type, status, link, owner, isActive);
	}

	@Override
	public void updateAsset(long id, String name, AssetStatus status, String link, String owner, boolean isActive) throws DBException {
		dbAsset.updateAsset(id, name, status, link, owner, isActive);
	}

	@Override
	public void deleteAsset(long id, String owner) throws DBException {
		dbAsset.deleteAsset(id, owner);
	}

	@Override
	public Asset getAsset(long id, String owner, boolean isActive) throws DBException {
		return dbAsset.getAsset(id, owner, isActive);
	}

	@Override
	public Set<Asset> getAssets(String owner, boolean isActive) throws DBException {
		return dbAsset.getAssets(owner, isActive);
	}

	@Override
	public boolean exists(long id, String owner) throws DBException {
		return dbAsset.exists(id, owner);
	}

	@Override
	public boolean exists(String name, String owner) throws DBException {
		return dbAsset.exists(name, owner);
	}

	@Override
	public boolean linkAsset(long assetID, long policyID) throws DBException {
		return dbAsset.linkAsset(assetID, policyID);
	}
}
