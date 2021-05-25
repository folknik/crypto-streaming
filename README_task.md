To use as input you would receive a text file with ETH transfers. Each
line of text is one ETH transfer, different transfers are separate by
a new line. The contents of each line is a JSON object. Here is one
example line of input:

```json
{"from":"0x6747f922385c9a6f9cfdf095f34345257dd597be","to":"0x3222ba3f3e79ea0e31467eee7639b57923558ccd","value":1050000000000000,"valueExactBase36":"ac6zc9sx6o","blockNumber":1,"timestamp":1439283071,"transactionHash":"0x5e4ff3b28521faf3031a8122a6267307ed63a0ab63aef789d26a0da201b1ca92","type":"fee","primaryKey":99996}
```

The properties `from`, `to` and `value` describe a transfer ordered by
`from` address in favour of `to` address for the amount of `value`
Satoshi. The action took place at `blockNumber`, which got minted at
`timestamp`. `from` and `to` can be arbitrary strings, `blockNumber`
and `timestamp` are non negative integers, `value` is a positive
integer.

As output you need to produce a text file with address balances. Each
line of text needs to be a JSON object in the following format:
```json
{"address": "0x", balance: 00000, "blockNumber: 0"}
```

Using just the above line as input we would have the following two
lines as output file:

```json
{"address": "0x6747f922385c9a6f9cfdf095f34345257dd597be", balance: -1050000000000000, "blockNumber: 1"}
{"address": "0x3222ba3f3e79ea0e31467eee7639b57923558ccd", balance: 1050000000000000, "blockNumber: 1"}
```

Note that this is a streaming computation, we need to keep track of
the balance for each address as we handle new input transfers. Each
transfer modifies the balance for the addresses involved in the
transfer. To better illustrate this here is another example with three
lines of input.

```json
{"from":"GENESIS","to":"0x001762430ea9c3a26e5749afdb70da5f78ddbb8c","value":"200000000000000000000","valueExactBase36":"167i830vk1gbnk","blockNumber":0,"timestamp":1438269973,"transactionHash":"GENESIS_001762430ea9c3a26e5749afdb70da5f78ddbb8c","type":"genesis","primaryKey":1}
{"from":"GENESIS","to":"0x001d14804b399c6ef80e64576f657660804fec0b","value":"4200000000000000000000","valueExactBase36":"omdmprieouisqo","blockNumber":0,"timestamp":1438269973,"transactionHash":"GENESIS_001d14804b399c6ef80e64576f657660804fec0b","type":"genesis","primaryKey":2}
{"from":"0x001762430ea9c3a26e5749afdb70da5f78ddbb8c","to":"0x001d14804b399c6ef80e64576f657660804fec0b","value":"100000000000000000000","valueExactBase36":"167i830vk1gbnk","blockNumber":1,"timestamp":1438270073,"transactionHash":"001d14804b399c6ef80e64576f657660804fec0b","type":"fee","primaryKey":3}
```

This input should produce the following output:

```json
{"address": "GENESIS", balance: -200000000000000000000, "blockNumber: 0"}
{"address": "0x001762430ea9c3a26e5749afdb70da5f78ddbb8c", balance: 200000000000000000000, "blockNumber: 0"}
{"address": "GENESIS", balance: -4400000000000000000000, "blockNumber: 0"}
{"address": "0x001d14804b399c6ef80e64576f657660804fec0b", balance: 4200000000000000000000, "blockNumber: 0"}
{"address": "0x001762430ea9c3a26e5749afdb70da5f78ddbb8c", balance: 100000000000000000000, "blockNumber: 1"}
{"address": "0x001d14804b399c6ef80e64576f657660804fec0b", balance: 4300000000000000000000, "blockNumber: 1"}
```

To solve the above problem please produce a Flink streaming
job. Although we provide as input a file, instead of handling the
whole file as a bulk, handle each transfer as a separate event.

Hint: Assign to each transfer even time, use watermarks to align time
windows and do computation per-block in those time windows. You can
read more about Flink's concept of even time here:
https://ci.apache.org/projects/flink/flink-docs-release-1.12/concepts/timely-stream-processing.html


You can find the needed input the file
`eth_transfers_input.txt.bz2`. It is a compressed text file. We have
also provided a `docker-compose.yaml` file which you can use to run a
local Flink cluster. You can find the necessary Apache Flink
documentation here: https://flink.apache.org/

Good luck with the task!
