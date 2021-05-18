package com.crypto.streaming.model;

import java.math.BigInteger;

public class Balance {
    private String address;
    private BigInteger balance;
    private Integer blockNumber;

    public String getAddress() {return address;}
    public String setAddress(String address) {return this.address = address;}

    public BigInteger getBalance() {return balance;}
    public BigInteger setBalance(BigInteger balance) {return this.balance = balance;}

    public Integer getBlockNumber() {return blockNumber;}
    public Integer setBlockNumber(Integer blockNumber) {return this.blockNumber = blockNumber;}
}
