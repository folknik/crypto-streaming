package com.crypto.streaming.model;

public class Transfer2 {
	private String from;
	private String to;
	private String value;
	private String valueExactBase36;
	private Integer blockNumber;
	private Long timestamp;
	private String transactionHash;
	private String type;
	private Integer primaryKey;
	private Integer transactionPosition;

	public String getFrom() {return from;}
	public String setFrom(String from) {return this.from = from;}

	public String getTo(){return to;}
	public String setTo(String to) {return this.to = to;}

	public String getValue(){return value;}
	public String setValue(String value) {return this.value = value;}

	public String getValueExactBase36(){return valueExactBase36;}
	public String setValueExactBase36(String valueExactBase36) {return this.valueExactBase36 = valueExactBase36;}

	public Integer getBlockNumber(){return blockNumber;}
	public Integer setBlockNumber(Integer blockNumber) {return this.blockNumber = blockNumber;}

	public Long getTimestamp(){return timestamp;}
	public Long setTimestamp(Long timestamp) {return this.timestamp = timestamp;}

	public String getTransactionHash(){return transactionHash;}
	public String setTransactionHash(String transactionHash) {return this.transactionHash = transactionHash;}

	public String getType(){return type;}
	public String setType(String type) {return this.type = type;}

	public Integer getPrimaryKey(){return primaryKey;}
	public Integer setPrimaryKey(Integer primaryKey) {return this.primaryKey = primaryKey;}

	public Integer getTransactionPosition(){return transactionPosition;}
	public Integer setTransactionPosition(Integer transactionPosition) {return this.transactionPosition = transactionPosition;}

}