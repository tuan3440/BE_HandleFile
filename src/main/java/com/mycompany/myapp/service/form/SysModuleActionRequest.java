package com.mycompany.myapp.service.form;

import java.util.List;

public class SysModuleActionRequest {

    private Long moduleId;
    private List<Long> actionIds;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public List<Long> getActionIds() {
        return actionIds;
    }

    public void setActionIds(List<Long> actionIds) {
        this.actionIds = actionIds;
    }
}
