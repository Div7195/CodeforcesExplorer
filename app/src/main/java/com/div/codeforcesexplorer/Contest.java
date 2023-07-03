package com.div.codeforcesexplorer;

public class Contest {
    private Integer id;
    private String name;
    private String type;
    private String phase;
    private Boolean frozen;
    private Integer durationSeconds;
    private Integer startTimeSeconds;
    private Integer relativeTimeSeconds;

    public Contest (Integer id, String name, String type, String phase, Boolean frozen, Integer durationSeconds, Integer startTimeSeconds, Integer relativeTimeSeconds){
        this.id = id;
        this.name = name;
        this.type = type;
        this.phase = phase;
        this.frozen = frozen;
        this.durationSeconds = durationSeconds;
        this.startTimeSeconds = startTimeSeconds;
        this.relativeTimeSeconds = relativeTimeSeconds;
    }

    public String getName() {
        return name;
    }

    public Boolean getFrozen() {
        return frozen;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRelativeTimeSeconds() {
        return relativeTimeSeconds;
    }

    public Integer getStartTimeSeconds() {
        return startTimeSeconds;
    }

    public String getPhase() {
        return phase;
    }

    public String getType() {
        return type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public void setRelativeTimeSeconds(Integer relativeTimeSeconds) {
        this.relativeTimeSeconds = relativeTimeSeconds;
    }

    public void setStartTimeSeconds(Integer startTimeSeconds) {
        this.startTimeSeconds = startTimeSeconds;
    }

    public void setType(String type) {
        this.type = type;
    }

}
