package gov.nist.csd.pm.pep.response;

import java.util.ArrayList;
import java.util.List;

public class Setup {
	private List<CurveParams> curve_params;
	private List<MasterPublicKey> master_public_key;

	public List<CurveParams> getCurve_params() {
		if(null == curve_params)
			curve_params = new ArrayList<>();
		return curve_params;
	}

	public void setCurve_params(List<CurveParams> curve_params) {
		this.curve_params = curve_params;
	}

	public List<MasterPublicKey> getMaster_public_key() {
		if(null == master_public_key)
			master_public_key = new ArrayList<>();
		return master_public_key;
	}

	public void setMaster_public_key(List<MasterPublicKey> master_public_key) {
		this.master_public_key = master_public_key;
	}
}
