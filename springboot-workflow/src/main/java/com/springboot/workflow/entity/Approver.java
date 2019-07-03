package com.springboot.workflow.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Approver extends Operator {

    public Approver(String id, String name) {
        setId(id);
        setName(name);
    }

    Approver bailor;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        final Approver other = (Approver) obj;

        return this.getId().equals(other.getId());
    }
}

