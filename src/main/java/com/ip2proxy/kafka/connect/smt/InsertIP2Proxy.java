/*
 * Copyright © 2022 IP2Location.com (support@ip2location.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ip2proxy.kafka.connect.smt;

import org.apache.kafka.common.cache.Cache;
import org.apache.kafka.common.cache.LRUCache;
import org.apache.kafka.common.cache.SynchronizedCache;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;

import org.apache.kafka.connect.transforms.Transformation;
import org.apache.kafka.connect.transforms.util.SchemaUtil;
import org.apache.kafka.connect.transforms.util.SimpleConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ip2proxy.*;

import static org.apache.kafka.connect.transforms.util.Requirements.requireMap;
import static org.apache.kafka.connect.transforms.util.Requirements.requireStruct;

public abstract class InsertIP2Proxy<R extends ConnectRecord<R>> implements Transformation<R> {

    public static final String OVERVIEW_DOC =
            "Insert IP2Proxy data using a field value in the record as the IP address to query.";

    private interface ConfigName {
        String IP2PROXY_BIN_PATH = "ip2proxy.bin.path";
        String IP2PROXY_INPUT = "ip2proxy.input";
    }

    public static final ConfigDef CONFIG_DEF = new ConfigDef()
            .define(ConfigName.IP2PROXY_BIN_PATH, ConfigDef.Type.STRING, "", ConfigDef.Importance.HIGH,
                    "Path to the IP2Proxy BIN database")
            .define(ConfigName.IP2PROXY_INPUT, ConfigDef.Type.STRING, "", ConfigDef.Importance.HIGH,
                    "IP address field to query");

    private static final String PURPOSE = "inserting IP2Proxy data into record";

    private String binFile;
    private String ipAddressField;

    private Cache<Schema, Schema> schemaUpdateCache;

    @Override
    public void configure(Map<String, ?> props) {
        final SimpleConfig config = new SimpleConfig(CONFIG_DEF, props);
        binFile = config.getString(ConfigName.IP2PROXY_BIN_PATH);
        ipAddressField = config.getString(ConfigName.IP2PROXY_INPUT);

        schemaUpdateCache = new SynchronizedCache<>(new LRUCache<Schema, Schema>(16));
    }

    @Override
    public R apply(R record) {
        if (operatingSchema(record) == null) {
            return applySchemaless(record);
        } else {
            return applyWithSchema(record);
        }
    }

    private R applySchemaless(R record) {
        final Map<String, Object> value = requireMap(operatingValue(record), PURPOSE);

        final Map<String, Object> updatedValue = new HashMap<>(value);

        // get IP2Proxy here and add the result fields below
        String prefix = "ip2proxy_";
        try {
            IP2Proxy proxy = new IP2Proxy();
            ProxyResult all;
            if (proxy.Open(binFile, IP2Proxy.IOModes.IP2PROXY_FILE_IO) == 0) {
                all = proxy.GetAll(updatedValue.get(ipAddressField).toString());
                updatedValue.put(prefix + "is_proxy", String.valueOf(all.Is_Proxy));
                updatedValue.put(prefix + "proxy_type", all.Proxy_Type);
                updatedValue.put(prefix + "country_code", all.Country_Short);
                updatedValue.put(prefix + "country_name", all.Country_Long);
                updatedValue.put(prefix + "region", all.Region);
                updatedValue.put(prefix + "city", all.City);
                updatedValue.put(prefix + "isp", all.ISP);
                updatedValue.put(prefix + "domain", all.Domain);
                updatedValue.put(prefix + "usage_type", all.Usage_Type);
                updatedValue.put(prefix + "asn", all.ASN);
                updatedValue.put(prefix + "as", all.AS);
                updatedValue.put(prefix + "last_seen", all.Last_Seen);
                updatedValue.put(prefix + "threat", all.Threat);
                updatedValue.put(prefix + "provider", all.Provider);
            } else {
                updatedValue.put(prefix + "error", "Error reading BIN file.");
            }
            proxy.Close();
        } catch (IOException ex) {
            updatedValue.put(prefix + "error", ex.getMessage());
        }
        return newRecord(record, null, updatedValue);
    }

    private R applyWithSchema(R record) {
        final Struct value = requireStruct(operatingValue(record), PURPOSE);

        Schema updatedSchema = schemaUpdateCache.get(value.schema());
        if (updatedSchema == null) {
            updatedSchema = makeUpdatedSchema(value.schema());
            schemaUpdateCache.put(value.schema(), updatedSchema);
        }

        final Struct updatedValue = new Struct(updatedSchema);

        for (Field field : value.schema().fields()) {
            updatedValue.put(field.name(), value.get(field));
        }

        // get IP2Proxy here and add the result fields below
        String prefix = "ip2proxy_";
        try {
            IP2Proxy proxy = new IP2Proxy();
            ProxyResult all;
            if (proxy.Open(binFile, IP2Proxy.IOModes.IP2PROXY_FILE_IO) == 0) {
                all = proxy.GetAll(updatedValue.get(ipAddressField).toString());
                updatedValue.put(prefix + "is_proxy", String.valueOf(all.Is_Proxy));
                updatedValue.put(prefix + "proxy_type", all.Proxy_Type);
                updatedValue.put(prefix + "country_code", all.Country_Short);
                updatedValue.put(prefix + "country_name", all.Country_Long);
                updatedValue.put(prefix + "region", all.Region);
                updatedValue.put(prefix + "city", all.City);
                updatedValue.put(prefix + "isp", all.ISP);
                updatedValue.put(prefix + "domain", all.Domain);
                updatedValue.put(prefix + "usage_type", all.Usage_Type);
                updatedValue.put(prefix + "asn", all.ASN);
                updatedValue.put(prefix + "as", all.AS);
                updatedValue.put(prefix + "last_seen", all.Last_Seen);
                updatedValue.put(prefix + "threat", all.Threat);
                updatedValue.put(prefix + "provider", all.Provider);
            } else {
                updatedValue.put(prefix + "error", "Error reading BIN file.");
            }
            proxy.Close();
        } catch (IOException ex) {
            updatedValue.put(prefix + "error", ex.getMessage());
        }

        return newRecord(record, updatedSchema, updatedValue);
    }

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }

    @Override
    public void close() {
        schemaUpdateCache = null;
    }

    private Schema makeUpdatedSchema(Schema schema) {
        final SchemaBuilder builder = SchemaUtil.copySchemaBasics(schema, SchemaBuilder.struct());

        for (Field field : schema.fields()) {
            builder.field(field.name(), field.schema());
        }

        String prefix = "ip2proxy_";
        builder.field(prefix + "is_proxy", Schema.STRING_SCHEMA);
        builder.field(prefix + "proxy_type", Schema.STRING_SCHEMA);
        builder.field(prefix + "country_code", Schema.STRING_SCHEMA);
        builder.field(prefix + "country_name", Schema.STRING_SCHEMA);
        builder.field(prefix + "region", Schema.STRING_SCHEMA);
        builder.field(prefix + "city", Schema.STRING_SCHEMA);
        builder.field(prefix + "isp", Schema.STRING_SCHEMA);
        builder.field(prefix + "domain", Schema.STRING_SCHEMA);
        builder.field(prefix + "usage_type", Schema.STRING_SCHEMA);
        builder.field(prefix + "asn", Schema.STRING_SCHEMA);
        builder.field(prefix + "as", Schema.STRING_SCHEMA);
        builder.field(prefix + "last_seen", Schema.STRING_SCHEMA);
        builder.field(prefix + "threat", Schema.STRING_SCHEMA);
        builder.field(prefix + "provider", Schema.STRING_SCHEMA);
        builder.field(prefix + "error", Schema.STRING_SCHEMA);

        return builder.build();
    }

    protected abstract Schema operatingSchema(R record);

    protected abstract Object operatingValue(R record);

    protected abstract R newRecord(R record, Schema updatedSchema, Object updatedValue);

    public static class Key<R extends ConnectRecord<R>> extends InsertIP2Proxy<R> {

        @Override
        protected Schema operatingSchema(R record) {
            return record.keySchema();
        }

        @Override
        protected Object operatingValue(R record) {
            return record.key();
        }

        @Override
        protected R newRecord(R record, Schema updatedSchema, Object updatedValue) {
            return record.newRecord(record.topic(), record.kafkaPartition(), updatedSchema, updatedValue, record.valueSchema(), record.value(), record.timestamp());
        }

    }

    public static class Value<R extends ConnectRecord<R>> extends InsertIP2Proxy<R> {

        @Override
        protected Schema operatingSchema(R record) {
            return record.valueSchema();
        }

        @Override
        protected Object operatingValue(R record) {
            return record.value();
        }

        @Override
        protected R newRecord(R record, Schema updatedSchema, Object updatedValue) {
            return record.newRecord(record.topic(), record.kafkaPartition(), record.keySchema(), record.key(), updatedSchema, updatedValue, record.timestamp());
        }

    }
}


