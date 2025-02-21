# IP2Proxy Kafka Transform

This Apache Kafka Transform can be used to find the IP addresses which are used as VPN servers, open proxies, web proxies, Tor exit nodes, search engine robots, data center ranges, residential proxies, consumer privacy networks, and enterprise private networks.

This tranform allows user to query an IP address if it was being used as VPN anonymizer, open proxies, web proxies, Tor exits, data center, web hosting (DCH) range, search engine robots (SES), residential proxies (RES), consumer privacy networks (CPN), and enterprise private networks (EPN). It lookup the proxy IP address from **IP2Proxy BIN Data** file. This data file can be downloaded at

* Free IP2Proxy BIN Data: https://lite.ip2location.com
* Commercial IP2Proxy BIN Data: https://www.ip2location.com/database/ip2proxy

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

## Schema

|Name|Schema|
|---|---|
|ip2proxy_is_proxy|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|
|ip2proxy_proxy_type|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|
|ip2proxy_country_code|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|
|ip2proxy_country_name|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|
|ip2proxy_region|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|
|ip2proxy_city|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|
|ip2proxy_isp|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|
|ip2proxy_domain|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|
|ip2proxy_usage_type|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|
|ip2proxy_asn|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|
|ip2proxy_as|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|
|ip2proxy_last_seesn|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|
|ip2proxy_threat|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|
|ip2proxy_provider|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|
|ip2proxy_fraud_score|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|
|ip2proxy_error|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|
