package com.miranda.voting.assemblies.v1.enums;

public enum ValidateVoteStatus {

    ABLE_TO_VOTE(0, "Able to vote"),
    UNABLE_TO_VOTE(1, "Unable to vote");

    private Integer cod;

    private String description;

    ValidateVoteStatus(Integer cod, String description) {
        this.cod = cod;
        this.setDescription(description);
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
