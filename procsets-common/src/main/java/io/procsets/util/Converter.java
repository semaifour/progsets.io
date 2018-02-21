package io.procsets.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.procsets.common.KeyValue;

/**
 * Convert String argument to the right type.
 * 
 * @author gigdata
 *
 */
public class Converter {

  public static Map<String, IConverter<?>> converters = new HashMap<String, IConverter<?>>();
  public static Locale locale = Locale.ENGLISH;

  static {
    converters.put("byte", new ByteConverter());
    converters.put("boolean", new BooleanConverter());
    converters.put("short", new ShortConverter());
    converters.put("int", new IntegerConverter());
    converters.put("long", new LongConverter());
    converters.put("float", new FloatConverter());
    converters.put("double", new DoubleConverter());
    converters.put("date", new DateConverter());
    converters.put("datetime", new DateConverter());
    converters.put("string", new StringConverter());

  }

  private static IConverter getConverter(String key) throws Exception {
    IConverter converter = converters.get(key);
    if (converter == null) {
      throw new Exception("Invalid data type :" + key);
    }
    return converter;
  }

  public static KeyValue convert(String key, Object value) {
    String[] spec = key.split(";|:",3);
    try {
      if (spec.length == 1) {
        return new KeyValue(spec[0], value);
      } else if (spec.length == 2) {
        return new KeyValue(spec[0], getConverter(spec[1]).convert(String.valueOf(value)));
      } else if (spec.length == 3) {
        return new KeyValue(spec[0], getConverter(spec[1]).convert(String.valueOf(value), spec[2]));
      } else {
        return new KeyValue(spec[0], value, "Unsupported spec :" + key);
      }
    } catch (Exception e) {
      return new KeyValue(spec[0], value, e.toString());
    }
  }
}


//
// Converters
//
abstract class IConverter<T> {

  public T convert(String value, String informat) throws Exception {
    return null;
  }

  public abstract T convert(String value) throws Exception;
}


class ByteConverter extends IConverter<Byte> {
  @Override
  public Byte convert(String value) throws Exception {
    return Byte.parseByte(value);
  }
}


class BooleanConverter extends IConverter<Boolean> {
  @Override
  public Boolean convert(String value) throws Exception {
    return Boolean.parseBoolean(value);
  }
}


class ShortConverter extends IConverter<Short> {
  @Override
  public Short convert(String value) throws Exception {
    return Short.parseShort(value);
  }
}


class IntegerConverter extends IConverter<Integer> {
  @Override
  public Integer convert(String value) throws Exception {
    return Integer.parseInt(value);
  }
}


class LongConverter extends IConverter<Long> {
  @Override
  public Long convert(String value) throws Exception {
    return Long.parseLong(value);
  }
}


class FloatConverter extends IConverter<Float> {
  @Override
  public Float convert(String value) throws Exception {
    return Float.parseFloat(value);
  }
}


class DoubleConverter extends IConverter<Double> {
  @Override
  public Double convert(String value) throws Exception {
    return Double.parseDouble(value);
  }
}


class StringConverter extends IConverter<String> {
  @Override
  public String convert(String value) throws Exception {
    return value;
  }
}


class DateConverter extends IConverter<Date> {
  @Override
  public Date convert(String value) throws Exception {
    return DateFormat.getDateTimeInstance(DateFormat.SHORT,
                                          DateFormat.SHORT,
                                          Converter.locale).parse(value);
  }

  @Override
  public Date convert(String value, String informat) throws Exception {
    SimpleDateFormat formatter = new SimpleDateFormat(informat, Converter.locale);
    return formatter.parse(value);
  }

}
