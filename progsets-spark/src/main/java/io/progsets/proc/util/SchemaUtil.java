package io.progsets.proc.util;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;

/**
 * 
 * Provides Spark DataFrame Schema Utils
 * 
 * 
 * @author mjs
 *
 */
public class SchemaUtil {

	/**
	 * Return an appropriate Spark DataType based on the java instance type of the given value
	 * 
	 * @param value
	 * @return
	 */
	public static DataType getDatatypeByValue(Object value) {
		if (value instanceof Date) return DataTypes.DateType;
		if (value instanceof Timestamp) return DataTypes.TimestampType;
		if (value instanceof Boolean) return DataTypes.BooleanType;
		if (value instanceof Byte) return DataTypes.ByteType;
		if (value instanceof Integer) return DataTypes.IntegerType;
		if (value instanceof Short) return DataTypes.ShortType;
		if (value instanceof Long) return DataTypes.LongType;
		if (value instanceof Float) return DataTypes.FloatType;
		if (value instanceof Double) return DataTypes.DoubleType;
		if (value instanceof String) return DataTypes.StringType;
		return DataTypes.BinaryType;
	}
	
	/**
	 * Return an appropriate Spark DataType for the given value object
	 * 
	 * @param value
	 * @return
	 */
	public static DataType getDatatypeByName(String datatype) {
		if (StringUtils.equalsIgnoreCase(datatype, "date")) return DataTypes.DateType;
		if (StringUtils.equalsIgnoreCase(datatype, "datetime")) return DataTypes.DateType;
		if (StringUtils.equalsIgnoreCase(datatype, "timestmap")) return DataTypes.DateType;
		if (StringUtils.equalsIgnoreCase(datatype, "boolean")) return DataTypes.BooleanType;
		if (StringUtils.equalsIgnoreCase(datatype, "byte")) return DataTypes.ByteType;
		if (StringUtils.equalsIgnoreCase(datatype, "int")) return DataTypes.IntegerType;
		if (StringUtils.equalsIgnoreCase(datatype, "short")) return DataTypes.ShortType;
		if (StringUtils.equalsIgnoreCase(datatype, "long")) return DataTypes.LongType;
		if (StringUtils.equalsIgnoreCase(datatype, "float")) return DataTypes.FloatType;
		if (StringUtils.equalsIgnoreCase(datatype, "double")) return DataTypes.DoubleType;
		if (StringUtils.equalsIgnoreCase(datatype, "string")) return DataTypes.StringType;
		return DataTypes.BinaryType;
	}
}
