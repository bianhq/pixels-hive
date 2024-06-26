/*
 * Copyright 2019 PixelsDB.
 *
 * This file is part of Pixels.
 *
 * Pixels is free software: you can redistribute it and/or modify
 * it under the terms of the Affero GNU General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Pixels is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Affero GNU General Public License for more details.
 *
 * You should have received a copy of the Affero GNU General Public
 * License along with Pixels.  If not, see
 * <https://www.gnu.org/licenses/>.
 */
package io.pixelsdb.pixels.hive.common;

import io.pixelsdb.pixels.core.encoding.EncodingLevel;
import org.apache.hadoop.conf.Configuration;

import java.util.Properties;

/**
 * Define the configuration properties that Pixels understands.
 * refers to {@link org.apache.orc.OrcConf}
 */
public enum PixelsConf
{
    ROW_INDEX_STRIDE("pixels.row.index.stride",
            "hive.exec.pixels.default.row.index.stride", 10000,
            "Define the default Pixels index stride in number of rows. (Stride is the\n" +
                    " number of rows n index entry represents.)"),
    STRIPE_SIZE("pixels.stripe.size", "hive.exec.pixels.default.stripe.size",
            64L * 1024 * 1024,
            "Define the default Pixels stripe size, in bytes."),
    BLOCK_SIZE("pixels.block.size", "hive.exec.pixels.default.block.size",
            256L * 1024 * 1024,
            "Define the default file system block size for Pixels files."),
    BLOCK_REPLICATION("pixels.block.replication", "hive.exec.pixels.default.block.size",
            3,
            "Define the default file system block replication for Pixels files."),
    BLOCK_PADDING("pixels.block.padding", "hive.exec.pixels.default.block.padding",
            true,
            "Define whether stripes should be padded to the HDFS block boundaries."),
    ENCODING_LEVEL("pixels.encoding.level", "hive.exec.pixels.encoding.level",
            EncodingLevel.EL2,
            "Define the encoding level to use while writing data. Changing this\n" +
                    "will only affect the light weight encoding for columns. This\n" +
                    "flag will not change the compression level of higher level\n" +
                    "compression codec (like ZLIB)."),
    COMPRESSION_STRATEGY("pixels.compression.strategy",
            "hive.exec.pixels.compression.strategy", 1,
            "Define the compression strategy to use while writing data.\n" +
                    "This changes the compression level of higher level compression\n" +
                    "codec (like ZLIB)."),

    MAPRED_SHUFFLE_KEY_SCHEMA("pixels.mapred.map.output.key.schema", null, null,
            "The schema of the MapReduce shuffle key. The values are\n" +
                    "interpreted using TypeDescription.fromString."),

    MAPRED_SHUFFLE_VALUE_SCHEMA("pixels.mapred.map.output.value.schema", null, null,
            "The schema of the MapReduce shuffle value. The values are\n" +
                    "interpreted using TypeDescription.fromString."),
    MAPRED_OUTPUT_SCHEMA("pixels.mapred.output.schema", null, null,
            "The schema that the user desires to write. The values are\n" +
                    "interpreted using TypeDescription.fromString."),;

    private final String attribute;
    private final String hiveConfName;
    private final Object defaultValue;
    private final String description;

    PixelsConf(String attribute,
               String hiveConfName,
               Object defaultValue,
               String description)
    {
        this.attribute = attribute;
        this.hiveConfName = hiveConfName;
        this.defaultValue = defaultValue;
        this.description = description;
    }

    public String getAttribute()
    {
        return attribute;
    }

    public String getHiveConfName()
    {
        return hiveConfName;
    }

    public Object getDefaultValue()
    {
        return defaultValue;
    }

    public String getDescription()
    {
        return description;
    }

    private String lookupValue(Properties tbl, Configuration conf)
    {
        String result = null;
        if (tbl != null)
        {
            result = tbl.getProperty(attribute);
        }
        if (result == null && conf != null)
        {
            result = conf.get(attribute);
            if (result == null && hiveConfName != null)
            {
                result = conf.get(hiveConfName);
            }
        }
        return result;
    }

    public long getLong(Properties tbl, Configuration conf)
    {
        String value = lookupValue(tbl, conf);
        if (value != null)
        {
            return Long.parseLong(value);
        }
        return ((Number) defaultValue).longValue();
    }

    public long getLong(Configuration conf)
    {
        return getLong(null, conf);
    }

    public void setLong(Configuration conf, long value)
    {
        conf.setLong(attribute, value);
    }

    public String getString(Properties tbl, Configuration conf)
    {
        String value = lookupValue(tbl, conf);
        return value == null ? (String) defaultValue : value;
    }

    public String getString(Configuration conf)
    {
        return getString(null, conf);
    }

    public void setString(Configuration conf, String value)
    {
        conf.set(attribute, value);
    }

    public boolean getBoolean(Properties tbl, Configuration conf)
    {
        String value = lookupValue(tbl, conf);
        if (value != null)
        {
            return Boolean.parseBoolean(value);
        }
        return (Boolean) defaultValue;
    }

    public boolean getBoolean(Configuration conf)
    {
        return getBoolean(null, conf);
    }

    public EncodingLevel getEncodingLevel(Properties tbl, Configuration conf)
    {
        String value = lookupValue(tbl, conf);
        if (value != null)
        {
            return EncodingLevel.from(value);
        }
        return (EncodingLevel) defaultValue;
    }

    public EncodingLevel getEncodingLevel(Configuration conf)
    {
        return getEncodingLevel(null, conf);
    }

    public void setBoolean(Configuration conf, boolean value)
    {
        conf.setBoolean(attribute, value);
    }

    public double getDouble(Properties tbl, Configuration conf)
    {
        String value = lookupValue(tbl, conf);
        if (value != null)
        {
            return Double.parseDouble(value);
        }
        return ((Number) defaultValue).doubleValue();
    }

    public double getDouble(Configuration conf)
    {
        return getDouble(null, conf);
    }

    public void setDouble(Configuration conf, double value)
    {
        conf.setDouble(attribute, value);
    }
}
