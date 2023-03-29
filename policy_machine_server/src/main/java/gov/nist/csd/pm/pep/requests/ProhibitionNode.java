package gov.nist.csd.pm.pep.requests;

public class ProhibitionNode {
	long id;
	String name;
    boolean complement;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isComplement() {
		return complement;
	}
	public void setComplement(boolean complement) {
		this.complement = complement;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
