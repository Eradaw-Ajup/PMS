package gov.nist.csd.pm.rap.asset;

import java.util.Set;

import gov.nist.csd.pm.common.constants.AssetStatus;
import gov.nist.csd.pm.common.exceptions.DBException;
import gov.nist.csd.pm.rap.AssetType;
import gov.nist.csd.pm.rap.model.Asset;

public interface AssetMachine {
	Asset createAsset(long id, String name, AssetType type, AssetStatus status, String link, String owner,
			boolean isActive) throws DBException;

	void updateAsset(long id, String name, AssetStatus status, String link, String owner, boolean isActive)
			throws DBException;

	void deleteAsset(long id, String owner) throws DBException;

	Asset getAsset(long id, String owner, boolean isActive) throws DBException;

	Set<Asset> getAssets(String owner, boolean isActive) throws DBException;

	boolean exists(long id, String owner) throws DBException;

	boolean exists(String name, String owner) throws DBException;

	boolean linkAsset(long assetID, long policyID) throws DBException;
}
