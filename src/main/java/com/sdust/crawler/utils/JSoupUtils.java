package com.sdust.crawler.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Created by jiangyongchao on 2017/9/12.
 */
public class JSoupUtils {
  public static final String CLASS = "class";
  public static final String ID = "ID";
  public static final String SELECT = "select";
  public static final String Attr = "attr";

  /**
   * 从elements获取某个element，如果越界，则返回null
   * @param elements
   * @param index
   * @return
   */
  public static Element findElementFromElements(Elements elements, int index) {
    try {
      if(elements == null) {
        return null;
      }
      if(elements.size() > index) {
        return elements.get(index);
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 根据属性获取element
   * @param element
   * @param key
   * @param value
   * @param exactMatch
   * @return
   */
  public static Element findElementByAttribute(Element element, String key, String value, boolean exactMatch) {
    return findElementByAttribute(element, key, value, exactMatch, 0);
  }

  /**
   * 根据属性获取element
   * @param element
   * @param key
   * @param value
   * @param exactMatch
   * @param index
   * @return
   */
  public static Element findElementByAttribute(Element element, String key, String value, boolean exactMatch, int index) {
    try {
      if(StringUtils.isBlank(value)) {
        return findElementFromElements(element.getElementsByAttribute(key), index);
      }
      if(exactMatch) {
        return findElementFromElements(element.getElementsByAttributeValue(key, value), index);
      }
      return findElementFromElements(element.getElementsByAttributeValueContaining(key, value), index);
    } catch(Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 获取element的text
   * @param element
   * @return
   */
  public static String findTextByElement(Element element) {
    try {
      if(element != null) {
        return element.text();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * 获取某个element下的attribute的text
   * @param element
   * @param key
   * @return
   */
  public static String findAttributeValueByElement(Element element, String key) {
    try {
      if(element == null) {
        return "";
      }
      return element.attr(key);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String findTextByElement(Elements elements) {
    try {
      return findTextByElement(findElementFromElements(elements, 0));
    } catch(Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String findTextByElements(Elements elements, int index) {
    try {
      return findTextByElement(findElementFromElements(elements, index));
    } catch(Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static Element findElementByClass(Element element, String classType) {
    return findElementByAttribute(element, "class", classType, true);
  }

  public static Element findElementByClass(Element element, String classType, int index) {
    return findElementByAttribute(element, "class", classType, true, index);
  }

  public static Elements findElementsByClass(Element element, String classType) {
    try {
      if(element != null) {
        return element.getElementsByAttributeValue("class", classType);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new Elements();
  }

}
