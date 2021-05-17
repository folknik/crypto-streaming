package com.crypto.streaming.model;

public class Balance {
    private String address;
    private Long balance;
    private Integer blockNumber;

    public String getAddress() {return address;}
    public String setAddress(String address) {return this.address = address;}

    public Long getBalance() {return balance;}
    public Long setBalance(Long balance) {return this.balance = balance;}

    public Integer getBlockNumber() {return blockNumber;}
    public Integer setBlockNumber(Integer blockNumber) {return this.blockNumber = blockNumber;}
}
