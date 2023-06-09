package gov.nist.csd.pm.common.model.obligations.script.rule.event;

import java.util.ArrayList;
import java.util.List;

import gov.nist.csd.pm.common.model.obligations.EvrEntity;

public class EvrSubject {
    private List<EvrEntity> entities;

    public EvrSubject() {
        entities = new ArrayList<>();
    }

    public void addEntity(EvrEntity entity) {
        this.entities.add(entity);
    }

    public List<EvrEntity> getEntities() {
        return this.entities;
    }

    public boolean isAny() {
        return entities.isEmpty();
    }

    @Override
	public boolean equals(Object o) {
        if (!(o instanceof EvrSubject)) {
            return false;
        }

        EvrSubject subject = (EvrSubject) o;

        if(this.isAny() && subject.isAny()) {
            return true;
        } else if(!this.isAny() && !this.isAny()) {
            if(this.getEntities().size() != subject.getEntities().size()) {
                return false;
            }

            //check each entity
            for(EvrEntity thisEntity : this.getEntities()) {
                for(EvrEntity subjectEntity : subject.getEntities()) {
                    if(!thisEntity.equals(subjectEntity)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
