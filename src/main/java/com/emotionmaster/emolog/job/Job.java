package com.emotionmaster.emolog.job;

public enum Job {
    STUDENT("student"),
    OFFICE("office"),
    NONE("none");

    private final String jobType;

    Job(String jobType) {
        this.jobType = jobType;
    }

    public String getJobType() {
        return jobType;
    }

    @Override
    public String toString() {
        return this.jobType;
    }
}
