# Quickstart

## Dependencies

This library requires IP2Proxy BIN database to function. You may download the BIN database at

-   IP2Proxy LITE BIN Data (Free): <https://lite.ip2location.com>
-   IP2Proxy Commercial BIN Data (Comprehensive):
    <https://www.ip2location.com>

## Installation

Download the latest IP2Proxy Java jar file ip2proxy-java-x.y.z.jar from https://search.maven.org/artifact/com.ip2proxy/ip2proxy-java

Download the latest IP2Proxy Kafka jar file ip2proxy-kafka-x.y.z.jar from https://search.maven.org/artifact/com.ip2proxy/ip2proxy-kafka

Copy both jar files into whichever plugin folder your Kafka installation is using.

Download the IP2Proxy BIN database file into the folder of your choice.

## Configure your connector properties

This transformation is used to lookup data from a IP2Proxy BIN database file and appending the data into an existing struct.

```properties
transforms=insertip2proxy
transforms.insertip2proxy.type=com.ip2proxy.kafka.connect.smt.InsertIP2Proxy$Value

# Set these required values
transforms.insertip2proxy.ip2proxy.bin.path=
transforms.insertip2proxy.ip2proxy.input=
```

|Name|Description|
|---|---|
|ip2proxy.bin.path|The full path to the IP2Proxy BIN database file.|
|ip2proxy.input|The field name in the record containing the IP address to query for proxy data.|