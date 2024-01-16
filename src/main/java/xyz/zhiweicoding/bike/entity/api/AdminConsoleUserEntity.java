package xyz.zhiweicoding.bike.entity.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * {
 * name: 'Serati Ma',
 * avatar: 'https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png',
 * userid: '00000001',
 * email: 'antdesign@alipay.com',
 * signature: '海纳百川，有容乃大',
 * title: '交互专家',
 * group: '蚂蚁金服－某某某事业群－某某平台部－某某技术部－UED',
 * tags: [
 * { key: '0', label: '很有想法的' },
 * { key: '1', label: '专注设计' },
 * { key: '2', label: '辣~' },
 * { key: '3', label: '大长腿' },
 * { key: '4', label: '川妹子' },
 * { key: '5', label: '海纳百川' },
 * ],
 * notifyCount: 12,
 * unreadCount: 11,
 * country: 'China',
 * "adminRole": 1,
 * geographic: {
 * province: { label: '浙江省', key: '330000' },
 * city: { label: '杭州市', key: '330100' },
 * },
 * address: '西湖区工专路 77 号',
 * phone: '0752-268888888',
 * }
 *
 * @author zhiweicoding.xyz
 * @date 1/1/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminConsoleUserEntity implements Serializable {

    private String name;
    private String avatar;
    private String userid;
    private String email;
    private String signature;
    private String title;
    private String group;
    private int adminRole;
    private int notifyCount = 0;
    private int unreadCount = 0;
    private String country;
    /**
     * province : {"label":"浙江省","key":"330000"}
     * city : {"label":"杭州市","key":"330100"}
     */
    private GeographicBean geographic;
    private String address;
    private String phone;
    /**
     * key : 0
     * label : 很有想法的
     */
    private List<TagsBean> tags;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeographicBean implements Serializable {
        @Serial
        private static final long serialVersionUID = -1364974918294724319L;
        /**
         * label : 浙江省
         * key : 330000
         */

        private ProvinceBean province;
        /**
         * label : 杭州市
         * key : 330100
         */

        private CityBean city;


        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ProvinceBean implements Serializable {
            @Serial
            private static final long serialVersionUID = -4281563758192328806L;
            private String label;
            private String key;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class CityBean implements Serializable {
            @Serial
            private static final long serialVersionUID = -8261630675093892563L;
            private String label;
            private String key;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TagsBean implements Serializable {
        @Serial
        private static final long serialVersionUID = -2430384057433561216L;
        private String key;
        private String label;
    }
}
