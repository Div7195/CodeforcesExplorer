package com.div.codeforcesexplorer;

public class Submission {
    private String name;
    private String language;
    private String verdict;
    private Integer time;

    public Submission(String name, String language, String verdict, Integer time){
        this.name = name;
        this.language = language;
        this.verdict = verdict;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public Integer getTime() {
        return time;
    }

    public String getVerdict() {
        return verdict;
    }
}
