package com.crypto.streaming.utils;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import com.crypto.streaming.model.Balance;
import com.crypto.streaming.model.Transfer;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

public class CryptoWindowAggregator extends RichFlatMapFunction<Tuple2<Integer, Transfer>, List<Balance>> {

    private transient ListState<Balance> sum;

    @Override
    public void flatMap(Tuple2<Integer, Transfer> transfer, Collector<List<Balance>> out) throws Exception {
        // all current list of balances
        List<Balance> currentBalances = new ArrayList<Balance>((Collection<? extends Balance>) sum.get());
        List<String> currentAddresses = currentBalances.stream().map(Balance::getAddress).collect(Collectors.toList());

        // get info from new transfer such as from, to, value, block
        String addressFrom = transfer.f1.getFrom();
        String addressTo = transfer.f1.getTo();
        BigInteger value = new BigInteger(transfer.f1.getValue());
        Integer block = transfer.f1.getBlockNumber();

        // create new sample of Balance class for addition them to collector
        List<Balance> balancesToOut = new ArrayList<Balance>();

        Balance newBalanceFrom = new Balance();
        Balance newBalanceTo = new Balance();

        if (currentAddresses.contains(addressFrom)) {
            Optional<Balance> accountFrom = currentBalances.stream().filter(item -> item.getAddress().equals(addressFrom)).findFirst();
            newBalanceFrom.setAddress(addressFrom);
            assert accountFrom.orElse(null) != null;
            newBalanceFrom.setBalance(accountFrom.get().getBalance().subtract(value));
            newBalanceFrom.setBlockNumber(block);
            currentBalances.set(currentBalances.indexOf(accountFrom.get()), newBalanceFrom);
        } else {
            newBalanceFrom.setAddress(addressFrom);
            newBalanceFrom.setBalance(value.multiply(new BigInteger("-1")));
            newBalanceFrom.setBlockNumber(block);
            currentBalances.add(newBalanceFrom);
        }

        if (currentAddresses.contains(addressTo)) {
            Optional<Balance> accountTo = currentBalances.stream().filter(item -> item.getAddress().equals(addressTo)).findFirst();
            newBalanceTo.setAddress(addressTo);
            assert accountTo.orElse(null) != null;
            newBalanceTo.setBalance(accountTo.get().getBalance().subtract(value));
            newBalanceTo.setBlockNumber(block);
            currentBalances.set(currentBalances.indexOf(accountTo.get()), newBalanceTo);
        } else {
            newBalanceTo.setAddress(addressTo);
            newBalanceTo.setBalance(value);
            newBalanceTo.setBlockNumber(block);
            currentBalances.add(newBalanceTo);
        }

        sum.update(currentBalances);

        balancesToOut.add(newBalanceFrom);
        balancesToOut.add(newBalanceTo);
        out.collect(balancesToOut);
    }

    @Override
    public void open(Configuration config) {
        sum = getRuntimeContext().getListState(
                new ListStateDescriptor<>("cryptoAggregation", Balance.class)
        );
    }
}
