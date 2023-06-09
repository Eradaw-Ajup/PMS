package gov.nist.csd.pm.common.model.obligations.script.rule.event;

import java.util.ArrayList;
import java.util.List;

import gov.nist.csd.pm.common.model.obligations.EvrEntity;

public class EvrTarget {
    private EvrEntity       entity;
    private List<EvrEntity> containers;
    private boolean intersection;

    public EvrTarget() {
        containers = new ArrayList<>();
    }

    public EvrEntity getEntity() {
        return entity;
    }

    public void setEntity(EvrEntity entity) {
        this.entity = entity;
    }

    public List<EvrEntity> getContainers() {
        return containers;
    }

    public void setContainers(List<EvrEntity> container) {
        this.containers = container;
    }

    public void addContainer(EvrEntity evrEntity) {
        this.containers.add(evrEntity);
    }

    public boolean isAnyContainer() {
        return containers.isEmpty();
    }

    public boolean isAnyEntity() {
        return entity == null || entity.isAny();
    }

    public boolean isIntersection() {
        return intersection;
    }

    public void setIntersection(boolean intersection) {
        this.intersection = intersection;
    }
}
