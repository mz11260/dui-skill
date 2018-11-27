package com.zm.protocol.request.nodes;

import com.zm.protocol.request.SkillRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>当前的设备信息，用户信息和应用状态</p>
 * <p>上级节点{@link SkillRequest}</p>
 */
@Data
@EqualsAndHashCode
public class Context {

    /** 当前技能信息 */
    private SkillInfo skill;

    /** 当前设备信息 */
    private Device device;

    /** 与当前设备绑定的用户信息 */
    private UserInfo user;

    /** 产品信息 */
    private Product product;

    /**
     * 设备信息
     */
    @Data
    public static class Device {

        private String deviceName;
        private DeviceInfo deviceInfo;

        /**
         * 设备基础信息,主要包含设备制造信息、时间信息、国家文字信息
         */
        @Data
        public static class DeviceInfo {


        }

    }

    /**
     * 应用信息
     */
    @Data
    public static class SkillInfo {

        /** CloudApp 在 dui 开放平台 上的唯一ID */
        private String skillId;


    }

    /**
     * 用户信息
     */
    @Data
    public static class UserInfo {
        /** 用户ID */
        private String userId;

    }

    /**
     * 产品信息
     */
    @Data
    public static class Product {
        /** 产品id */
        private String productId;

        /** 产品版本 */
        private String productVersion = "1.0";

        /** 内部使用字段，产品形象Id */
        private String profileId;
    }
}


