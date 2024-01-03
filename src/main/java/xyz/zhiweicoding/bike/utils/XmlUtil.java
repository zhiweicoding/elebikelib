package xyz.zhiweicoding.bike.utils;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Map;
import java.util.Set;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
public class XmlUtil {

    public static String mapToXmlStr(Map<String, String> mapBean) {
        Document document = DocumentHelper.createDocument();
        Element rootEle = document.addElement("xml");
        Set<String> keySets = mapBean.keySet();
        for (String keySet : keySets) {
            Element keySetEle = rootEle.addElement(keySet);
            keySetEle.setText(String.valueOf(mapBean.get(keySet)));
        }
        return document.asXML();
    }

}
