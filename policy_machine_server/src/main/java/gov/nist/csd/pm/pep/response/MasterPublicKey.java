package gov.nist.csd.pm.pep.response;

public class MasterPublicKey {
	private String point_P_x;
	private String point_P_y;
	private String point_P_z;
	private String point_Q_x;
	private String point_Q_y;
	private String point_Q_z;
	private String master_public_key_x;
	private String master_public_key_y;
	private String master_public_key_z;
	private String attribute_public_key_a;
	private String attribute_public_key_b;

	public String getPoint_P_x() {
		return point_P_x;
	}

	public void setPoint_P_x(String point_P_x) {
		this.point_P_x = point_P_x;
	}

	public String getPoint_P_y() {
		return point_P_y;
	}

	public void setPoint_P_y(String point_P_y) {
		this.point_P_y = point_P_y;
	}

	public String getPoint_P_z() {
		return point_P_z;
	}

	public void setPoint_P_z(String point_P_z) {
		this.point_P_z = point_P_z;
	}

	public String getPoint_Q_x() {
		return point_Q_x;
	}

	public void setPoint_Q_x(String point_Q_x) {
		this.point_Q_x = point_Q_x;
	}

	public String getPoint_Q_y() {
		return point_Q_y;
	}

	public void setPoint_Q_y(String point_Q_y) {
		this.point_Q_y = point_Q_y;
	}

	public String getPoint_Q_z() {
		return point_Q_z;
	}

	public void setPoint_Q_z(String point_Q_z) {
		this.point_Q_z = point_Q_z;
	}

	public String getMaster_public_key_x() {
		return master_public_key_x;
	}

	public void setMaster_public_key_x(String master_public_key_x) {
		this.master_public_key_x = master_public_key_x;
	}

	public String getMaster_public_key_y() {
		return master_public_key_y;
	}

	public void setMaster_public_key_y(String master_public_key_y) {
		this.master_public_key_y = master_public_key_y;
	}

	public String getMaster_public_key_z() {
		return master_public_key_z;
	}

	public void setMaster_public_key_z(String master_public_key_z) {
		this.master_public_key_z = master_public_key_z;
	}

	public String getAttribute_public_key_a() {
		return attribute_public_key_a;
	}

	public void setAttribute_public_key_a(String attribute_public_key_a) {
		this.attribute_public_key_a = attribute_public_key_a;
	}

	public String getAttribute_public_key_b() {
		return attribute_public_key_b;
	}

	public void setAttribute_public_key_b(String attribute_public_key_b) {
		this.attribute_public_key_b = attribute_public_key_b;
	}
}
