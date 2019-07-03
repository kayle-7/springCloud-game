package com.springboot.workflow.constants;

/**
 * Created by zx
 */
public enum ProcessDefEnum {
    EXPENSE_ACCOUNT("expenseAccount");

    public String processDef;

    public String getProcessDef() {
        return processDef;
    }

    ProcessDefEnum(String processDef) {
        this.processDef = processDef;
    }

    public static ProcessDefEnum getProcessDef(String processDef) {
        switch (processDef) {
            case "expenseAccount":
                return EXPENSE_ACCOUNT;
            default:
                return null;
        }
    }
}
