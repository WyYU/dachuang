package com.example.dell.dachuang.Java;
/*
 * version:1.0
 * class name:StepVO
 * author:鏉庤嫢鍑�	
 * description:step瀹炰綋鐨勫瓨鍌ㄧ被
 */
/**
 * @author 98192
 *
 */
public class StepVO {
	
	private String stepID;
	private int serialNumber;
	private LocationVO location;
	private String description;
	private boolean hasNext;
	private ProcessVO processVO;
	
	
	public ProcessVO getProcessVO() {
		return processVO;
	}

	public void setProcessVO(ProcessVO processVO) {
		this.processVO = processVO;
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public LocationVO getLocation() {
		return location;
	}

	public void setLocation(LocationVO location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStepID() {
		return stepID;
	}

	public void setStepID(String stepID) {
		this.stepID = stepID;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	

}
