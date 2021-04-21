package com.hillclimbing.nqueen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NQueenModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private boolean goalflag;
	private float noOfTries;
	private List<Integer> successcount;
	private List<Integer> failedcounts;
	
	public boolean isGoalReached() {
		return goalflag;
	}
	public void setGoalReached(boolean goalReached) {
		this.goalflag = goalReached;
	}
	public float getNoOfTries() {
		return noOfTries;
	}
	public void setNoOfTries(float noOfTries) {
		this.noOfTries = noOfTries;
	}
	public List<Integer> getSuccessSteps() {
		if(null == successcount || successcount.isEmpty()) {
			successcount = new ArrayList<>();
		}
		return successcount;
	}
	public void setSuccessSteps(List<Integer> successSteps) {
		this.successcount = successSteps;
	}
	public List<Integer> getFailureSteps() {
		if(null == failedcounts || failedcounts.isEmpty()) {
			failedcounts = new ArrayList<>();
		}
		return failedcounts;
	}
	public void setFailureSteps(List<Integer> failureSteps) {
		this.failedcounts = failureSteps;
	}
	
}