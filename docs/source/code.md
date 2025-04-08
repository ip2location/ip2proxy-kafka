# IP2Proxy Kafka API

## Schema

|Name|Schema|Description|
|---|---|---|
|ip2proxy_is_proxy|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|Determine whether if an IP address was a proxy or not. Returns 0 is not proxy, 1 if proxy, and 2 if it's data center IP.|
|ip2proxy_proxy_type|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|Type of proxy.|
|ip2proxy_country_code|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|Two-character country code based on ISO 3166.|
|ip2proxy_country_name|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|Country name based on ISO 3166.|
|ip2proxy_region|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|Region or state name.|
|ip2proxy_city|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|City name.|
|ip2proxy_isp|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|Internet Service Provider or company\'s name.|
|ip2proxy_domain|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|Internet domain name associated with IP address range.|
|ip2proxy_usage_type|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|Usage type classification of ISP or company.|
|ip2proxy_asn|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|Autonomous system number (ASN).|
|ip2proxy_as|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|Autonomous system (AS) name.|
|ip2proxy_last_seesn|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|Proxy last seen in days.|
|ip2proxy_threat|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|Security threat reported.|
|ip2proxy_provider|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|Name of VPN provider if available.|
|ip2proxy_fraud_score|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|Potential risk score (0 - 99) associated with IP address.|
|ip2proxy_error|[String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)|IP2Proxy BIN lookup error message.|
