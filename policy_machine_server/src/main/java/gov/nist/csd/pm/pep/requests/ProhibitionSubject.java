package gov.nist.csd.pm.pep.requests;


public class ProhibitionSubject {
	long subjectID;
	String name;
    String subjectType;

	public long getSubjectID() {
		return subjectID;
	}
	public void setSubjectID(long subjectID) {
		this.subjectID = subjectID;
	}
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
