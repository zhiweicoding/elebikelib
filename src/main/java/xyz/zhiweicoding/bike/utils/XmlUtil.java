package xyz.zhiweicoding.bike.utils;

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
        Iterator var4 = keySets.iterator();

        while(var4.hasNext()) {
            String keySet = (String)var4.next();
            Element keySetEle = rootEle.addElement(keySet);
            keySetEle.setText(String.valueOf(mapBean.get(keySet)));
        }

        return document.asXML();
    }

}
