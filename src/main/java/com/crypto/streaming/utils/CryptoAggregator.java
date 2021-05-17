package com.crypto.streaming.utils;

import com.crypto.streaming.model.Balance;
import com.crypto.streaming.model.Transfer;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple3;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CryptoAggregator implements AggregateFunction<Transfer, Tuple3<Integer, List<Balance>, List<Balance>>, String> {

	private static final long serialVersionUID = 1L;

	@Override
	public Tuple3<Integer, List<Balance>, List<Balance>> createAccumulator() {
		return new Tuple3<Integer, List<Balance>, List<Balance>>(0, new ArrayList<Balance>(), new ArrayList<Balance>());
	}

	@Override
	public Tuple3<Integer, List<Balance>, List<Balance>> add(Transfer transfer, Tuple3<Integer, List<Balance>, List<Balance>> accumulator) {

		List<Balance> transferByAccount = new ArrayList<Balance>();
		Balance newAccountFrom = new Balance();
		Balance newAccountTo = new Balance();

		List<Balance> balanceList = accumulator.f2;
		List<String> allAddresses = balanceList.stream().map(Balance::getAddress).collect(Collectors.toList());

		String addressFrom = transfer.getFrom();
		String addressTo = transfer.getTo();
		Long balance = transfer.getValue();
		Integer block = transfer.getBlockNumber();

		if (allAddresses.contains(addressFrom)) {
			if (allAddresses.contains(addressTo)) {
				Optional<Balance> accountFrom = balanceList.stream().filter(item -> item.getAddress().equals(addressFrom)).findFirst();
				newAccountFrom.setAddress(Objects.requireNonNull(accountFrom.orElse(null)).getAddress());
				newAccountFrom.setBalance(accountFrom.orElse(null).getBalance() - balance);
				newAccountFrom.setBlockNumber(block);

				Optional<Balance> accountTo = balanceList.stream().filter(item -> item.getAddress().equals(addressTo)).findFirst();
				newAccountTo.setAddress(Objects.requireNonNull(accountTo.orElse(null)).getAddress());
				newAccountTo.setBalance(accountTo.orElse(null).getBalance() + balance);
				newAccountTo.setBlockNumber(block);

				accumulator.f0 = block;
				balanceList.set(balanceList.indexOf(accountFrom), newAccountFrom);
				balanceList.set(balanceList.indexOf(accountTo), newAccountTo);
				accumulator.f2 = balanceList;

			} else {
				Optional<Balance> accountFrom = balanceList.stream().filter(item -> item.getAddress().equals(addressFrom)).findFirst();
				newAccountFrom.setAddress(Objects.requireNonNull(accountFrom.orElse(null)).getAddress());
				newAccountFrom.setBalance(accountFrom.orElse(null).getBalance() - balance);
				newAccountFrom.setBlockNumber(block);

				newAccountTo.setAddress(addressTo);
				newAccountTo.setBalance(balance);
				newAccountTo.setBlockNumber(block);

				accumulator.f0 = block;
				balanceList.set(balanceList.indexOf(accountFrom), newAccountFrom);
				balanceList.add(newAccountTo);
				accumulator.f2 = balanceList;

			}
		} else {
			if (allAddresses.contains(addressTo)) {
				newAccountFrom.setAddress(addressFrom);
				newAccountFrom.setBalance(-balance);
				newAccountFrom.setBlockNumber(block);

				Optional<Balance> accountTo = balanceList.stream().filter(item -> item.getAddress().equals(addressTo)).findFirst();
				newAccountTo.setAddress(Objects.requireNonNull(accountTo.orElse(null)).getAddress());
				newAccountTo.setBalance(accountTo.orElse(null).getBalance() + balance);
				newAccountTo.setBlockNumber(block);

				accumulator.f0 = block;
				balanceList.add(newAccountFrom);
				balanceList.set(balanceList.indexOf(accountTo), newAccountTo);
				accumulator.f2 = balanceList;

			} else {
				newAccountFrom.setAddress(addressFrom);
				newAccountFrom.setBalance(-balance);
				newAccountFrom.setBlockNumber(block);

				newAccountTo.setAddress(addressTo);
				newAccountTo.setBalance(balance);
				newAccountTo.setBlockNumber(block);

				accumulator.f0 = block;
				balanceList.add(newAccountFrom);
				balanceList.add(newAccountTo);
				accumulator.f2 = balanceList;

			}
		}

		transferByAccount.add(newAccountFrom);
		transferByAccount.add(newAccountTo);
		accumulator.f1 = transferByAccount;

		return accumulator;
	}

	@Override
	public String getResult(Tuple3<Integer, List<Balance>, List<Balance>> accumulator) {
		return accumulator.f1.stream().map(Object::toString).reduce("\n", String::concat);
	}

	@Override
	public Tuple3<Integer, List<Balance>, List<Balance>> merge(
			Tuple3<Integer, List<Balance>, List<Balance>> acc1, Tuple3<Integer, List<Balance>, List<Balance>> acc2) {
		return new Tuple3<Integer, List<Balance>, List<Balance>>(
				(acc1.f0 > acc2.f0) ? acc1.f0 : acc2.f0,
				Stream.concat(acc1.f1.stream(), acc2.f1.stream()).collect(Collectors.toList()),
				Stream.concat(acc1.f2.stream(), acc2.f2.stream()).collect(Collectors.toList())
		);
	}
}
