package com.zht.entity;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 学生约车信息
 * 
 * @author admin
 * 
 */
public class StuInfo {
	private String fchrStudentName;
	private String fchrStudentID;
	private String fchrPassWord;
	private String fchrIDCardNO;
	private String fdtmSchedule;
	private String fintType;
	private String fchrLessonName;
	private String fchrLessonID;
	private String fchrLessonTypeName;
	private String fchrRegistrationID;
	private String fchrTrainPriceID;
	private String fchrLessonTypeID;
	private String fchrCarTypeID;
	private String stuTotal;
	private String trainTotal;

	@JsonProperty(value = "ContractTotal")
	private String contractTotal;
	private String fpsucess;
	private String zksucess;
	private String messager;
	private String fpsucessdate;
	private String fchrLessonID1;
	private String fchrLessonName1;
	private String fchrRegToCoachID;
	private String fchrRegToCoachName;

	public String getFchrStudentName() {
		return fchrStudentName;
	}

	public void setFchrStudentName(String fchrStudentName) {
		this.fchrStudentName = fchrStudentName;
	}

	public String getFchrStudentID() {
		return fchrStudentID;
	}

	public void setFchrStudentID(String fchrStudentID) {
		this.fchrStudentID = fchrStudentID;
	}

	public String getFchrPassWord() {
		return fchrPassWord;
	}

	public void setFchrPassWord(String fchrPassWord) {
		this.fchrPassWord = fchrPassWord;
	}

	public String getFchrIDCardNO() {
		return fchrIDCardNO;
	}

	public void setFchrIDCardNO(String fchrIDCardNO) {
		this.fchrIDCardNO = fchrIDCardNO;
	}

	public String getFdtmSchedule() {
		return fdtmSchedule;
	}

	public void setFdtmSchedule(String fdtmSchedule) {
		this.fdtmSchedule = fdtmSchedule;
	}

	public String getFintType() {
		return fintType;
	}

	public void setFintType(String fintType) {
		this.fintType = fintType;
	}

	public String getFchrLessonName() {
		return fchrLessonName;
	}

	public void setFchrLessonName(String fchrLessonName) {
		this.fchrLessonName = fchrLessonName;
	}

	public String getFchrLessonID() {
		return fchrLessonID;
	}

	public void setFchrLessonID(String fchrLessonID) {
		this.fchrLessonID = fchrLessonID;
	}

	public String getFchrLessonTypeName() {
		return fchrLessonTypeName;
	}

	public void setFchrLessonTypeName(String fchrLessonTypeName) {
		this.fchrLessonTypeName = fchrLessonTypeName;
	}

	public String getFchrRegistrationID() {
		return fchrRegistrationID;
	}

	public void setFchrRegistrationID(String fchrRegistrationID) {
		this.fchrRegistrationID = fchrRegistrationID;
	}

	public String getFchrTrainPriceID() {
		return fchrTrainPriceID;
	}

	public void setFchrTrainPriceID(String fchrTrainPriceID) {
		this.fchrTrainPriceID = fchrTrainPriceID;
	}

	public String getFchrLessonTypeID() {
		return fchrLessonTypeID;
	}

	public void setFchrLessonTypeID(String fchrLessonTypeID) {
		this.fchrLessonTypeID = fchrLessonTypeID;
	}

	public String getFchrCarTypeID() {
		return fchrCarTypeID;
	}

	public void setFchrCarTypeID(String fchrCarTypeID) {
		this.fchrCarTypeID = fchrCarTypeID;
	}

	public String getStuTotal() {
		return stuTotal;
	}

	public void setStuTotal(String stuTotal) {
		this.stuTotal = stuTotal;
	}

	public String getTrainTotal() {
		return trainTotal;
	}

	public void setTrainTotal(String trainTotal) {
		this.trainTotal = trainTotal;
	}

	public String getContractTotal() {
		return contractTotal;
	}

	public void setContractTotal(String contractTotal) {
		this.contractTotal = contractTotal;
	}

	public String getFpsucess() {
		return fpsucess;
	}

	public void setFpsucess(String fpsucess) {
		this.fpsucess = fpsucess;
	}

	public String getZksucess() {
		return zksucess;
	}

	public void setZksucess(String zksucess) {
		this.zksucess = zksucess;
	}

	public String getMessager() {
		return messager;
	}

	public void setMessager(String messager) {
		this.messager = messager;
	}

	public String getFpsucessdate() {
		return fpsucessdate;
	}

	public void setFpsucessdate(String fpsucessdate) {
		this.fpsucessdate = fpsucessdate;
	}

	public String getFchrLessonID1() {
		return fchrLessonID1;
	}

	public void setFchrLessonID1(String fchrLessonID1) {
		this.fchrLessonID1 = fchrLessonID1;
	}

	public String getFchrLessonName1() {
		return fchrLessonName1;
	}

	public void setFchrLessonName1(String fchrLessonName1) {
		this.fchrLessonName1 = fchrLessonName1;
	}

	public String getFchrRegToCoachID() {
		return fchrRegToCoachID;
	}

	public void setFchrRegToCoachID(String fchrRegToCoachID) {
		this.fchrRegToCoachID = fchrRegToCoachID;
	}

	public String getFchrRegToCoachName() {
		return fchrRegToCoachName;
	}

	public void setFchrRegToCoachName(String fchrRegToCoachName) {
		this.fchrRegToCoachName = fchrRegToCoachName;
	}

}
