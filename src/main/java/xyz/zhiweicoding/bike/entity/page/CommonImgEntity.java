package xyz.zhiweicoding.bike.entity.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * {
 * "goodTitle": "title",
 * "goodBrief": "desc",
 * "symbolId": "8dcf19be5f7048d6911dcceeb2a090d4",
 * "retailPrice": 11,
 * "isNew": "1",
 * "isChosen": "1",
 * "scenePicUrl": [
 * {
 * "uid": "rc-upload-1705506841511-4",
 * "lastModified": 1670288096000,
 * "lastModifiedDate": "2022-12-06T00:54:56.000Z",
 * "name": "1.jpg",
 * "size": 1344979,
 * "type": "image/jpeg",
 * "percent": 100,
 * "originFileObj": {
 * "uid": "rc-upload-1705506841511-4"
 * },
 * "status": "done",
 * "response": "https://bike-1256485110.cos.ap-beijing.myqcloud.com/38cc615a1fd54f9092f453a7263560fe..jpg",
 * "xhr": {}
 * }
 * ],
 * "listPicUrl": [
 * {
 * "uid": "rc-upload-1705506841511-6",
 * "lastModified": 1670288096000,
 * "lastModifiedDate": "2022-12-06T00:54:56.000Z",
 * "name": "1.jpg",
 * "size": 1344979,
 * "type": "image/jpeg",
 * "percent": 100,
 * "originFileObj": {
 * "uid": "rc-upload-1705506841511-6"
 * },
 * "status": "done",
 * "response": "https://bike-1256485110.cos.ap-beijing.myqcloud.com/4a8724501a6d4dd1b3402ae942c2a585..jpg",
 * "xhr": {}
 * }
 * ],
 * "photoUrl": [
 * {
 * "uid": "rc-upload-1705506841511-8",
 * "lastModified": 1670288096000,
 * "lastModifiedDate": "2022-12-06T00:54:56.000Z",
 * "name": "1.jpg",
 * "size": 1344979,
 * "type": "image/jpeg",
 * "percent": 100,
 * "originFileObj": {
 * "uid": "rc-upload-1705506841511-8"
 * },
 * "status": "done",
 * "response": "https://bike-1256485110.cos.ap-beijing.myqcloud.com/d5c0b43e770f4870baec768ebc9f4e0d..jpg",
 * "xhr": {}
 * }
 * ]
 * }
 *
 * @author zhiweicoding.xyz
 * @date 1/17/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonImgEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 2044062377995963488L;
    private String uid;
    private long lastModified;
    private String lastModifiedDate;
    private String name;
    private int size;
    private String type;
    private int percent;
    private OriginFileObjBean originFileObj;
    private String status;
    private String response;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OriginFileObjBean implements Serializable {

        @Serial
        private static final long serialVersionUID = -1394462821233744803L;
        private String uid;
    }
}
