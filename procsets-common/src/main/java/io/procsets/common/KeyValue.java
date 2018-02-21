package io.procsets.common;

public class KeyValue {

  private String key = null;
  private Object value = null;
  private String remark = null;

  public KeyValue(String key, Object value) {
    this.key = key;
    this.value = value;
  }

  public KeyValue(String key, Object value, String remark) {
    this.key = key;
    this.value = value;
    this.remark = remark;
  }

  public boolean hasRemark() {
    return remark != null;
  }

  public String getRemark() {
    return this.remark;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }
  
}