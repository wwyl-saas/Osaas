
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_announce
-- ----------------------------
DROP TABLE IF EXISTS `t_announce`;
CREATE TABLE `t_announce`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '公告类型',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '公告名称',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '公告内容html格式',
  `tag` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标签',
  `picture_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '背景图',
  `enable` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否开启',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_mid`(`merchant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_announce_user
-- ----------------------------
DROP TABLE IF EXISTS `t_announce_user`;
CREATE TABLE `t_announce_user`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `announce_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '公告ID',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_mid`(`merchant_id`, `merchant_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公告查看表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_another_pay
-- ----------------------------
DROP TABLE IF EXISTS `t_another_pay`;
CREATE TABLE `t_another_pay`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `application_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '应用ID',
  `pay_customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '代付用户ID',
  `use_customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '被付用户ID',
  `consume_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '消费记录ID',
  `pay_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '代付金额',
  `pay_code` bigint(20) NOT NULL DEFAULT 0 COMMENT '代付码',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_p_key`(`merchant_id`, `pay_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员代付表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_banner
-- ----------------------------
DROP TABLE IF EXISTS `t_banner`;
CREATE TABLE `t_banner`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `banner_position_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '位置ID',
  `media_type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '媒体类型',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '链接',
  `picture_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '图片url',
  `enable` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否启用',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_b_id`(`merchant_id`, `banner_position_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '首页banner表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_card
-- ----------------------------
DROP TABLE IF EXISTS `t_card`;
CREATE TABLE `t_card`  (
  `id` bigint(20) NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '次卡类型',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '次卡名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '次卡详情',
  `tag` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '次卡标注',
  `discount` int(11) NOT NULL DEFAULT 0 COMMENT '折扣金额',
  `residue_num` int(11) NOT NULL DEFAULT 0 COMMENT '剩余下发量',
  `issue_num` int(11) NOT NULL DEFAULT 0 COMMENT '下发总量',
  `effect_num` int(11) NOT NULL DEFAULT 0 COMMENT '次卡生效次数',
  `start_time` date NOT NULL COMMENT '开始时间',
  `end_time` date NOT NULL COMMENT '结束时间',
  `enable` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '生效状态，1生效0关闭',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_mid`(`merchant_id`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '服务卡项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类名称',
  `sort_order` tinyint(3) UNSIGNED NOT NULL DEFAULT 50 COMMENT '排序',
  `icon` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'icon url',
  `icon_on` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enable` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否启用',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_id`(`merchant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `order_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单ID',
  `goods_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品ID',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户技师ID',
  `merchant_user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '服务技师名称',
  `merchant_user_title` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '计师头像',
  `customer_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '头像',
  `grade` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '评分',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '反馈信息',
  `picture_urls` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '反馈图片ID,限制3',
  `is_anonymous` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否匿名 1是0不是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_se_id`(`merchant_id`, `goods_id`) USING BTREE,
  INDEX `m_m_u_id`(`merchant_id`, `merchant_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '服务评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_company_saler
-- ----------------------------
DROP TABLE IF EXISTS `t_company_saler`;
CREATE TABLE `t_company_saler`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名称',
  `city_id` int(11) NOT NULL DEFAULT 0 COMMENT '所属城市',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `city_id`(`city_id`) USING BTREE,
  INDEX `mobile`(`mobile`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公司销售信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_coupon
-- ----------------------------
DROP TABLE IF EXISTS `t_coupon`;
CREATE TABLE `t_coupon`  (
  `id` bigint(20) NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '卡券类型',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '优惠券名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '优惠券详情',
  `tag` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '优惠券标注',
  `discount` int(11) NOT NULL DEFAULT 0 COMMENT '折扣金额分为单位,折扣比例0-99表示0-9.9折',
  `residue_num` int(11) NOT NULL DEFAULT 0 COMMENT '剩余下发量',
  `issue_num` int(11) NOT NULL DEFAULT 0 COMMENT '发放总数量',
  `start_time` date NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` date NULL DEFAULT NULL COMMENT '结束时间',
  `enable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否启用 1启用',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_mid`(`merchant_id`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '卡券表（尚未绑定用户）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_coupon_effect_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_coupon_effect_rule`;
CREATE TABLE `t_coupon_effect_rule`  (
  `id` bigint(20) NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `coupon_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '优惠券ID',
  `rule_type` tinyint(3) NOT NULL DEFAULT 0 COMMENT '规则类型',
  `relation` tinyint(3) NOT NULL DEFAULT 0 COMMENT '条件关系',
  `rule_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '参考值，in和notin不能超过12个元素',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_mid`(`merchant_id`, `coupon_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '卡券生效规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_coupon_issue_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_coupon_issue_rule`;
CREATE TABLE `t_coupon_issue_rule`  (
  `id` bigint(20) NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `coupon_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '优惠券ID',
  `rule_type` tinyint(3) NOT NULL DEFAULT 0 COMMENT '规则类型',
  `relation` tinyint(3) NOT NULL DEFAULT 0 COMMENT '条件关系',
  `rule_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '参考值，in和notin不能超过12个元素',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_mid`(`merchant_id`, `coupon_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '卡券发放规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_customer
-- ----------------------------
DROP TABLE IF EXISTS `t_customer`;
CREATE TABLE `t_customer`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `wx_unoinid` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信unionId',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名称',
  `nickname` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '别名',
  `city_id` int(11) NOT NULL DEFAULT 0 COMMENT '所属城市',
  `gender` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '性别',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '头像',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `last_login_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '最后登录时间',
  `source` tinyint(3) NOT NULL DEFAULT 0 COMMENT '来源',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_wu_id`(`merchant_id`, `wx_unoinid`) USING BTREE,
  INDEX `m_mobile`(`merchant_id`, `mobile`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'C端用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_customer_account
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_account`;
CREATE TABLE `t_customer_account`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `customer_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户ID',
  `member_id` bigint(20) NOT NULL COMMENT '商户会员等级Id',
  `member_level` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '会员级别',
  `consume_score` int(11) NOT NULL DEFAULT 0 COMMENT '消费积分',
  `consume_num` int(11) NOT NULL DEFAULT 0 COMMENT '消费次数',
  `charge_num` int(11) NOT NULL DEFAULT 0 COMMENT '充值次数',
  `balance` decimal(19, 2) NOT NULL DEFAULT 0.00 COMMENT '账户余额，实际充值+赠送',
  `cumulative_recharge` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '累计实际充值金额',
  `cumulative_consume` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '累计消费金额',
  `wx_card_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信会员卡code',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `m_c_id`(`merchant_id`, `customer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'C端用户账户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_customer_application
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_application`;
CREATE TABLE `t_customer_application`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `customer_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户ID',
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '来源应用ID',
  `merchant_app_type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '来源应用类型',
  `wx_openid` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信openId',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `m_c_a_id`(`merchant_id`, `customer_id`, `merchant_app_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'C端用户应用来源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_customer_appointment
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_appointment`;
CREATE TABLE `t_customer_appointment`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '来源应用ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `shop_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店名称',
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'C端用户ID',
  `arrive_date` date NULL DEFAULT NULL COMMENT '到达日期',
  `arrive_time` time(0) NULL DEFAULT NULL COMMENT '到达时间',
  `appoint_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '预约人名称',
  `appoint_mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '预约人手机号',
  `appoint_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `goods_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '服务项目ID',
  `goods_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `status` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_id`(`merchant_id`, `customer_id`) USING BTREE,
  INDEX `m_s_id`(`merchant_id`, `shop_id`, `merchant_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'C端用户预约表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_customer_card
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_card`;
CREATE TABLE `t_customer_card`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'C端用户ID',
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '来源应用ID',
  `card_id` bigint(20) NOT NULL,
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '次卡类型',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '次卡名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '次卡详情',
  `tag` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '此卡标注',
  `discount` int(11) NOT NULL DEFAULT 0 COMMENT '折扣金额',
  `residue_num` int(11) NOT NULL DEFAULT 0 COMMENT '剩余次数',
  `effect_num` int(11) NOT NULL DEFAULT 0 COMMENT '次卡有效次数',
  `start_time` date NOT NULL COMMENT '开始时间',
  `end_time` date NOT NULL COMMENT '结束时间',
  `status` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_mid`(`merchant_id`, `customer_id`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'C端用户卡项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_customer_charge
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_charge`;
CREATE TABLE `t_customer_charge`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '应用ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `merchant_user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'C端用户ID',
  `charge_type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '充值类型',
  `amount` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '实际充值金额',
  `gift_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '返赠金额 直接返赠，活动返赠，优惠券返赠',
  `charge_status` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '充值状态',
  `operator_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人ID，客户端充值时为0',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_id`(`merchant_id`, `customer_id`) USING BTREE,
  INDEX `m_s_u_id`(`merchant_id`, `shop_id`, `merchant_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员充值表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_customer_consume_0
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_consume_0`;
CREATE TABLE `t_customer_consume_0`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '应用ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'C端用户ID',
  `order_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单ID',
  `description` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '日志描述',
  `consume_type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '记录类型',
  `amount` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '金额',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_id`(`merchant_id`, `customer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员充值消费记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_customer_consume_1
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_consume_1`;
CREATE TABLE `t_customer_consume_1`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '应用ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'C端用户ID',
  `order_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单ID',
  `description` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '日志描述',
  `consume_type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '记录类型',
  `amount` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '金额',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_id`(`merchant_id`, `customer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员充值消费记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_customer_consume_2
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_consume_2`;
CREATE TABLE `t_customer_consume_2`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '应用ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'C端用户ID',
  `order_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单ID',
  `description` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '日志描述',
  `consume_type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '记录类型',
  `amount` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '金额',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_id`(`merchant_id`, `customer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员充值消费记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_customer_consume_3
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_consume_3`;
CREATE TABLE `t_customer_consume_3`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '应用ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'C端用户ID',
  `order_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单ID',
  `description` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '日志描述',
  `consume_type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '记录类型',
  `amount` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '金额',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_id`(`merchant_id`, `customer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员充值消费记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_customer_consume_4
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_consume_4`;
CREATE TABLE `t_customer_consume_4`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '应用ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'C端用户ID',
  `order_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单ID',
  `description` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '日志描述',
  `consume_type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '记录类型',
  `amount` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '金额',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_id`(`merchant_id`, `customer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员充值消费记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_customer_consume_5
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_consume_5`;
CREATE TABLE `t_customer_consume_5`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '应用ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'C端用户ID',
  `order_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单ID',
  `description` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '日志描述',
  `consume_type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '记录类型',
  `amount` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '金额',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_id`(`merchant_id`, `customer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员充值消费记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_customer_coupon
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_coupon`;
CREATE TABLE `t_customer_coupon`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'C端用户ID',
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '来源应用ID',
  `coupon_id` bigint(20) NOT NULL COMMENT '商户优惠券ID',
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '卡券类型',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '优惠券名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '优惠券详情',
  `tag` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '优惠券标注',
  `discount` int(11) NOT NULL DEFAULT 0 COMMENT '折扣金额分为单位,折扣比例0-99表示0-9.9折',
  `start_time` date NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` date NULL DEFAULT NULL COMMENT '结束时间',
  `status` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_mid`(`merchant_id`, `customer_id`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'C端用户卡券表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_feedback
-- ----------------------------
DROP TABLE IF EXISTS `t_feedback`;
CREATE TABLE `t_feedback`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '应用ID',
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'C端用户ID',
  `customer_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `customer_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '反馈类型',
  `anonymous` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否匿名',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '反馈信息',
  `picture_urls` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '反馈图片,限制3个',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_id`(`merchant_id`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员反馈表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_feedback_user
-- ----------------------------
DROP TABLE IF EXISTS `t_feedback_user`;
CREATE TABLE `t_feedback_user`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `feekback_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '反馈ID',
  `is_read` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已读',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_mid`(`merchant_id`, `merchant_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户用户反馈表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_goods
-- ----------------------------
DROP TABLE IF EXISTS `t_goods`;
CREATE TABLE `t_goods`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `category_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分类ID',
  `name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `brief_pic_url` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品简图url',
  `primary_pic_urls` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '主图url列表',
  `list_pic_urls` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '详情图url列表',
  `goods_brief` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品简介',
  `goods_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品描述',
  `on_sale` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否上架',
  `counter_price` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '专柜价格',
  `market_price` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '市场价格',
  `hot` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否热卖',
  `new` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否新品',
  `recommend` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否推荐',
  `sell_volume` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '销售量',
  `sort_order` int(11) UNSIGNED NOT NULL DEFAULT 100,
  `keywords` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关键字',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_id`(`merchant_id`, `category_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '服务信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_goods_attribute
-- ----------------------------
DROP TABLE IF EXISTS `t_goods_attribute`;
CREATE TABLE `t_goods_attribute`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `goods_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '属性名称',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '属性值',
  `sort_order` int(11) UNSIGNED NOT NULL DEFAULT 100,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_s_id`(`merchant_id`, `goods_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '服务规格表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_goods_issue
-- ----------------------------
DROP TABLE IF EXISTS `t_goods_issue`;
CREATE TABLE `t_goods_issue`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `goods_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品ID',
  `question` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '问题',
  `answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回答',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `merchant_id`(`merchant_id`, `goods_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '服务问题表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_goods_merchant_user
-- ----------------------------
DROP TABLE IF EXISTS `t_goods_merchant_user`;
CREATE TABLE `t_goods_merchant_user`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `goods_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品ID',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺用户ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `merchant_g_id`(`merchant_id`, `goods_id`) USING BTREE,
  INDEX `merchant_u_id`(`merchant_id`, `merchant_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品和技师表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_goods_shop
-- ----------------------------
DROP TABLE IF EXISTS `t_goods_shop`;
CREATE TABLE `t_goods_shop`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `goods_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `merchant_g_id`(`merchant_id`, `goods_id`) USING BTREE,
  INDEX `merchant_s_id`(`merchant_id`, `shop_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '服务门店表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_member
-- ----------------------------
DROP TABLE IF EXISTS `t_member`;
CREATE TABLE `t_member`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `level` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '级别',
  `picture_url` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '会员卡图片',
  `wx_picture_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信会员卡素材URL',
  `welfare_all_types` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '所有福利',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_id`(`merchant_id`, `level`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户会员福利表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_member_threshold
-- ----------------------------
DROP TABLE IF EXISTS `t_member_threshold`;
CREATE TABLE `t_member_threshold`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `member_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '会员福利ID',
  `threshold_type` tinyint(3) NOT NULL DEFAULT 0 COMMENT '门槛键值',
  `threshold_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '门槛值val',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_m_id`(`merchant_id`, `member_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员门槛值属性信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_member_welfare
-- ----------------------------
DROP TABLE IF EXISTS `t_member_welfare`;
CREATE TABLE `t_member_welfare`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `member_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '会员福利ID',
  `welfare_key` int(11) NOT NULL DEFAULT 0 COMMENT '属性键值',
  `welfare_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '属性value',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_m_id`(`merchant_id`, `member_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员卡固有属性信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_merchant
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant`;
CREATE TABLE `t_merchant`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商户名称',
  `city_id` int(11) NOT NULL DEFAULT 0 COMMENT '所属城市',
  `telephone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '联系电话',
  `saler_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '销售ID',
  `source` int(11) NOT NULL DEFAULT 0 COMMENT '渠道来源',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `telephone`(`telephone`) USING BTREE,
  INDEX `city_id`(`city_id`) USING BTREE,
  INDEX `saler_id`(`saler_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_merchant_application
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_application`;
CREATE TABLE `t_merchant_application`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '产品名称',
  `type` tinyint(3) NOT NULL DEFAULT 0 COMMENT '产品类型',
  `url` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '入口URL',
  `app_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '三方APPID',
  `app_secret` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '三方APP密钥',
  `msg_token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '消息服务token',
  `msg_aes_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '消息服务AESKey',
  `msg_format_type` tinyint(3) NOT NULL DEFAULT 1 COMMENT 'Msg格式类型',
  `wx_mch_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信支付商户ID',
  `wx_partner_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信支付密钥',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '启用时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `merchant_id`(`merchant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户app表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_merchant_info
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_info`;
CREATE TABLE `t_merchant_info`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `brief_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商户简称',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商户简介',
  `province_id` int(11) NOT NULL DEFAULT 0 COMMENT '省',
  `city_id` int(11) NOT NULL DEFAULT 0 COMMENT '城市',
  `district_id` int(11) NOT NULL DEFAULT 0 COMMENT '区县',
  `address` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '具体地址',
  `licence` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '社会信用编号',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_id`(`merchant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_merchant_post_title
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_post_title`;
CREATE TABLE `t_merchant_post_title`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '岗位职称名称',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户岗位职称表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_merchant_role_navigator
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_role_navigator`;
CREATE TABLE `t_merchant_role_navigator`  (
  `id` int(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `role_type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '角色类型',
  `navigator_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '导航类型',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `m_r_n_id`(`merchant_id`, `role_type`, `navigator_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户角色导航表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_merchant_shop
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_shop`;
CREATE TABLE `t_merchant_shop`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '门店名称',
  `telephone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '联系电话',
  `address` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '门店地址',
  `picture` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '门店照片',
  `longitude` double(10, 6) NOT NULL DEFAULT 0.000000 COMMENT '经度坐标',
  `latitude` double(10, 6) NOT NULL DEFAULT 0.000000 COMMENT '纬度坐标',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `merchant_id`(`merchant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户门店表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_merchant_user
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_user`;
CREATE TABLE `t_merchant_user`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户电话',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户姓名',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户头像',
  `profile_url` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户照片',
  `wx_ap_openid` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信小程序openID',
  `wx_mp_openid` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信公众号openID',
  `wx_unionid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信unionID',
  `password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户密码',
  `post_title_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '岗位职称ID',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效',
  `if_appointment` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否接受预约',
  `user_brief` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户简介',
  `grade` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '综合评分',
  `last_login_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '最后登陆时间',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '艺名',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `ping_yin` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'a' COMMENT '姓名拼音简写',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `m_mobile`(`mobile`) USING BTREE,
  INDEX `post_title_id`(`merchant_id`, `post_title_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_merchant_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_user_role`;
CREATE TABLE `t_merchant_user_role`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '门店ID',
  `user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
  `role_type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '角色ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_m_s_u_r`(`merchant_id`, `shop_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_merchant_wx_card
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_wx_card`;
CREATE TABLE `t_merchant_wx_card`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `card_type` tinyint(3) NOT NULL DEFAULT 0 COMMENT '卡券类型',
  `card_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '卡券名称',
  `card_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '卡券Id',
  `issue_num` int(11) NOT NULL DEFAULT 0 COMMENT '下发总量',
  `residue_num` int(11) NOT NULL DEFAULT 0 COMMENT '剩余下发量',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_id`(`merchant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户微信卡券表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_navigator
-- ----------------------------
DROP TABLE IF EXISTS `t_navigator`;
CREATE TABLE `t_navigator`  (
  `id` int(20) UNSIGNED NOT NULL,
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '图标',
  `color` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '颜色',
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '类型',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '链接',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户首页导航表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_notice
-- ----------------------------
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '通知类型',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '通知者',
  `avatar` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '通知者头像',
  `subject` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '消息主题',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '消息内容html格式',
  `is_read` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已读',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_mid`(`merchant_id`, `merchant_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息通知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_0
-- ----------------------------
DROP TABLE IF EXISTS `t_order_0`;
CREATE TABLE `t_order_0`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '应用ID',
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'C端用户ID',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '订单号',
  `status` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单状态',
  `coupon_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '优惠券ID',
  `coupon_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券减免金额',
  `total_num` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商品总数',
  `order_sum_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '订单总价',
  `discount_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '折扣金额',
  `pay_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '实际支付金额',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `goods_names` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '购买商品名称，逗号分隔',
  `checker` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '收银用户ID',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_id`(`merchant_id`, `customer_id`) USING BTREE,
  INDEX `m_s_m_id_d`(`merchant_id`, `shop_id`, `merchant_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_1
-- ----------------------------
DROP TABLE IF EXISTS `t_order_1`;
CREATE TABLE `t_order_1`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '应用ID',
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'C端用户ID',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '订单号',
  `status` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单状态',
  `coupon_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '优惠券ID',
  `coupon_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券减免金额',
  `total_num` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商品总数',
  `order_sum_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '订单总价',
  `discount_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '折扣金额',
  `pay_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '实际支付金额',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `goods_names` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '购买商品名称，逗号分隔',
  `checker` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '收银用户ID',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_id`(`merchant_id`, `customer_id`) USING BTREE,
  INDEX `m_s_m_id_d`(`merchant_id`, `shop_id`, `merchant_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_2
-- ----------------------------
DROP TABLE IF EXISTS `t_order_2`;
CREATE TABLE `t_order_2`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '应用ID',
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'C端用户ID',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '订单号',
  `status` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单状态',
  `coupon_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '优惠券ID',
  `coupon_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券减免金额',
  `total_num` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商品总数',
  `order_sum_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '订单总价',
  `discount_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '折扣金额',
  `pay_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '实际支付金额',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `goods_names` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '购买商品名称，逗号分隔',
  `checker` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '收银用户ID',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_id`(`merchant_id`, `customer_id`) USING BTREE,
  INDEX `m_s_m_id_d`(`merchant_id`, `shop_id`, `merchant_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_3
-- ----------------------------
DROP TABLE IF EXISTS `t_order_3`;
CREATE TABLE `t_order_3`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '应用ID',
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'C端用户ID',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '订单号',
  `status` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单状态',
  `coupon_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '优惠券ID',
  `coupon_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券减免金额',
  `total_num` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商品总数',
  `order_sum_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '订单总价',
  `discount_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '折扣金额',
  `pay_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '实际支付金额',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `goods_names` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '购买商品名称，逗号分隔',
  `checker` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '收银用户ID',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_id`(`merchant_id`, `customer_id`) USING BTREE,
  INDEX `m_s_m_id_d`(`merchant_id`, `shop_id`, `merchant_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_4
-- ----------------------------
DROP TABLE IF EXISTS `t_order_4`;
CREATE TABLE `t_order_4`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '应用ID',
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'C端用户ID',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '订单号',
  `status` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单状态',
  `coupon_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '优惠券ID',
  `coupon_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券减免金额',
  `total_num` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商品总数',
  `order_sum_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '订单总价',
  `discount_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '折扣金额',
  `pay_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '实际支付金额',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `goods_names` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '购买商品名称，逗号分隔',
  `checker` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '收银用户ID',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_id`(`merchant_id`, `customer_id`) USING BTREE,
  INDEX `m_s_m_id_d`(`merchant_id`, `shop_id`, `merchant_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_5
-- ----------------------------
DROP TABLE IF EXISTS `t_order_5`;
CREATE TABLE `t_order_5`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '应用ID',
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'C端用户ID',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '订单号',
  `status` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单状态',
  `coupon_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '优惠券ID',
  `coupon_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券减免金额',
  `total_num` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商品总数',
  `order_sum_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '订单总价',
  `discount_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '折扣金额',
  `pay_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '实际支付金额',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `goods_names` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '购买商品名称，逗号分隔',
  `checker` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '收银用户ID',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_c_id`(`merchant_id`, `customer_id`) USING BTREE,
  INDEX `m_s_m_id_d`(`merchant_id`, `shop_id`, `merchant_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_desc_0
-- ----------------------------
DROP TABLE IF EXISTS `t_order_desc_0`;
CREATE TABLE `t_order_desc_0`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `order_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单ID',
  `goods_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品ID',
  `brief_pic_url` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '简图URL',
  `name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `goods_brief` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品简介',
  `counter_price` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '专柜价格',
  `market_price` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '市场价格',
  `goods_num` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品数量',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_s_o_id`(`merchant_id`, `order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_desc_1
-- ----------------------------
DROP TABLE IF EXISTS `t_order_desc_1`;
CREATE TABLE `t_order_desc_1`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `order_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单ID',
  `goods_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品ID',
  `brief_pic_url` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '简图URL',
  `name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `goods_brief` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品简介',
  `counter_price` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '专柜价格',
  `market_price` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '市场价格',
  `goods_num` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品数量',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_s_o_id`(`merchant_id`, `order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_desc_2
-- ----------------------------
DROP TABLE IF EXISTS `t_order_desc_2`;
CREATE TABLE `t_order_desc_2`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `order_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单ID',
  `goods_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品ID',
  `brief_pic_url` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '简图URL',
  `name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `goods_brief` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品简介',
  `counter_price` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '专柜价格',
  `market_price` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '市场价格',
  `goods_num` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品数量',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_s_o_id`(`merchant_id`, `order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_desc_3
-- ----------------------------
DROP TABLE IF EXISTS `t_order_desc_3`;
CREATE TABLE `t_order_desc_3`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `order_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单ID',
  `goods_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品ID',
  `brief_pic_url` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '简图URL',
  `name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `goods_brief` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品简介',
  `counter_price` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '专柜价格',
  `market_price` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '市场价格',
  `goods_num` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品数量',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_s_o_id`(`merchant_id`, `order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_desc_4
-- ----------------------------
DROP TABLE IF EXISTS `t_order_desc_4`;
CREATE TABLE `t_order_desc_4`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `order_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单ID',
  `goods_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品ID',
  `brief_pic_url` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '简图URL',
  `name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `goods_brief` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品简介',
  `counter_price` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '专柜价格',
  `market_price` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '市场价格',
  `goods_num` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品数量',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_s_o_id`(`merchant_id`, `order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_desc_5
-- ----------------------------
DROP TABLE IF EXISTS `t_order_desc_5`;
CREATE TABLE `t_order_desc_5`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `order_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '订单ID',
  `goods_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品ID',
  `brief_pic_url` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '简图URL',
  `name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `goods_brief` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品简介',
  `counter_price` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '专柜价格',
  `market_price` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '市场价格',
  `goods_num` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品数量',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `m_s_o_id`(`merchant_id`, `order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_push_message
-- ----------------------------
DROP TABLE IF EXISTS `t_push_message`;
CREATE TABLE `t_push_message`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `merchant_app_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '应用ID',
  `customer_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
  `form_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '小程序表单或者payID',
  `business_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '业务ID',
  `business_type` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '业务类型',
  `push_number` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '推送次数',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `merchant_id`(`merchant_id`, `business_type`, `business_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '延迟推送消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_region
-- ----------------------------
DROP TABLE IF EXISTS `t_region`;
CREATE TABLE `t_region`  (
  `id` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT,
  `parent_id` smallint(5) UNSIGNED NOT NULL DEFAULT 0,
  `name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `type` tinyint(1) NOT NULL DEFAULT 2,
  `agency_id` smallint(5) UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `parent_id`(`parent_id`) USING BTREE,
  INDEX `region_type`(`type`) USING BTREE,
  INDEX `agency_id`(`agency_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_statistic_0
-- ----------------------------
DROP TABLE IF EXISTS `t_statistic_0`;
CREATE TABLE `t_statistic_0`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '统计类型',
  `data_value` decimal(19, 2) NOT NULL DEFAULT 0.00 COMMENT '统计值',
  `data_date` date NOT NULL COMMENT '数据日期',
  `year` int(11) NOT NULL DEFAULT 0 COMMENT '年度',
  `quarter` tinyint(3) NOT NULL COMMENT '自然季度1,2,3,4',
  `month` tinyint(3) NOT NULL DEFAULT 0 COMMENT '自然月1-12',
  `week` tinyint(3) NOT NULL DEFAULT 0 COMMENT '自然周1-54',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `d_m_s_key`(`merchant_id`, `shop_id`, `merchant_user_id`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户首页指标表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_statistic_1
-- ----------------------------
DROP TABLE IF EXISTS `t_statistic_1`;
CREATE TABLE `t_statistic_1`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '统计类型',
  `data_value` decimal(19, 2) NOT NULL DEFAULT 0.00 COMMENT '统计值',
  `data_date` date NOT NULL COMMENT '数据日期',
  `year` int(11) NOT NULL DEFAULT 0 COMMENT '年度',
  `quarter` tinyint(3) NOT NULL COMMENT '自然季度1,2,3,4',
  `month` tinyint(3) NOT NULL DEFAULT 0 COMMENT '自然月1-12',
  `week` tinyint(3) NOT NULL DEFAULT 0 COMMENT '自然周1-54',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `d_m_s_key`(`merchant_id`, `shop_id`, `merchant_user_id`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户首页指标表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_statistic_2
-- ----------------------------
DROP TABLE IF EXISTS `t_statistic_2`;
CREATE TABLE `t_statistic_2`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '统计类型',
  `data_value` decimal(19, 2) NOT NULL DEFAULT 0.00 COMMENT '统计值',
  `data_date` date NOT NULL COMMENT '数据日期',
  `year` int(11) NOT NULL DEFAULT 0 COMMENT '年度',
  `quarter` tinyint(3) NOT NULL COMMENT '自然季度1,2,3,4',
  `month` tinyint(3) NOT NULL DEFAULT 0 COMMENT '自然月1-12',
  `week` tinyint(3) NOT NULL DEFAULT 0 COMMENT '自然周1-54',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `d_m_s_key`(`merchant_id`, `shop_id`, `merchant_user_id`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户首页指标表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_statistic_3
-- ----------------------------
DROP TABLE IF EXISTS `t_statistic_3`;
CREATE TABLE `t_statistic_3`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '统计类型',
  `data_value` decimal(19, 2) NOT NULL DEFAULT 0.00 COMMENT '统计值',
  `data_date` date NOT NULL COMMENT '数据日期',
  `year` int(11) NOT NULL DEFAULT 0 COMMENT '年度',
  `quarter` tinyint(3) NOT NULL COMMENT '自然季度1,2,3,4',
  `month` tinyint(3) NOT NULL DEFAULT 0 COMMENT '自然月1-12',
  `week` tinyint(3) NOT NULL DEFAULT 0 COMMENT '自然周1-54',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `d_m_s_key`(`merchant_id`, `shop_id`, `merchant_user_id`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户首页指标表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_statistic_4
-- ----------------------------
DROP TABLE IF EXISTS `t_statistic_4`;
CREATE TABLE `t_statistic_4`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '统计类型',
  `data_value` decimal(19, 2) NOT NULL DEFAULT 0.00 COMMENT '统计值',
  `data_date` date NOT NULL COMMENT '数据日期',
  `year` int(11) NOT NULL DEFAULT 0 COMMENT '年度',
  `quarter` tinyint(3) NOT NULL COMMENT '自然季度1,2,3,4',
  `month` tinyint(3) NOT NULL DEFAULT 0 COMMENT '自然月1-12',
  `week` tinyint(3) NOT NULL DEFAULT 0 COMMENT '自然周1-54',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `d_m_s_key`(`merchant_id`, `shop_id`, `merchant_user_id`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户首页指标表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_statistic_5
-- ----------------------------
DROP TABLE IF EXISTS `t_statistic_5`;
CREATE TABLE `t_statistic_5`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `merchant_user_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '统计类型',
  `data_value` decimal(19, 2) NOT NULL DEFAULT 0.00 COMMENT '统计值',
  `data_date` date NOT NULL COMMENT '数据日期',
  `year` int(11) NOT NULL DEFAULT 0 COMMENT '年度',
  `quarter` tinyint(3) NOT NULL COMMENT '自然季度1,2,3,4',
  `month` tinyint(3) NOT NULL DEFAULT 0 COMMENT '自然月1-12',
  `week` tinyint(3) NOT NULL DEFAULT 0 COMMENT '自然周1-54',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `d_m_s_key`(`merchant_id`, `shop_id`, `merchant_user_id`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户首页指标表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_statistic_month_sum
-- ----------------------------
DROP TABLE IF EXISTS `t_statistic_month_sum`;
CREATE TABLE `t_statistic_month_sum`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `merchant_user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '统计类型',
  `data_value` decimal(19, 2) NOT NULL DEFAULT 0.00 COMMENT '统计值',
  `year` int(11) NOT NULL DEFAULT 0 COMMENT '年度',
  `month` tinyint(3) NOT NULL DEFAULT 0 COMMENT '自然月1-12',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `d_m_s_key`(`merchant_id`, `shop_id`, `merchant_user_id`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户首页指标表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_statistic_quarter_sum
-- ----------------------------
DROP TABLE IF EXISTS `t_statistic_quarter_sum`;
CREATE TABLE `t_statistic_quarter_sum`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `merchant_user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '统计类型',
  `data_value` decimal(19, 2) NOT NULL DEFAULT 0.00 COMMENT '统计值',
  `year` int(11) NOT NULL DEFAULT 0 COMMENT '年度',
  `quarter` tinyint(3) NOT NULL COMMENT '自然季度1,2,3,4',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `d_m_s_key`(`merchant_id`, `shop_id`, `merchant_user_id`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户首页指标表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_statistic_week_sum
-- ----------------------------
DROP TABLE IF EXISTS `t_statistic_week_sum`;
CREATE TABLE `t_statistic_week_sum`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `merchant_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商户ID',
  `shop_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商铺ID',
  `merchant_user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '商户用户ID',
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '统计类型',
  `data_value` decimal(19, 2) NOT NULL DEFAULT 0.00 COMMENT '统计值',
  `year` int(11) NOT NULL DEFAULT 0 COMMENT '年度',
  `week` tinyint(3) NOT NULL DEFAULT 0 COMMENT '自然周1-54',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `d_m_s_key`(`merchant_id`, `shop_id`, `merchant_user_id`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户首页指标表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_subscriber
-- ----------------------------
DROP TABLE IF EXISTS `t_subscriber`;
CREATE TABLE `t_subscriber`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `wx_unoinid` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信unionId',
  `wx_openid` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信openId',
  `nickname` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '别名',
  `city_id` int(11) NOT NULL DEFAULT 0 COMMENT '所属城市',
  `gender` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '性别',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '头像',
  `subscribe` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否订阅',
  `source` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '来源',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `wu_id`(`wx_unoinid`) USING BTREE,
  INDEX `wx_openid`(`wx_openid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '企业公众号订阅者表（需商户用户订阅）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for test_user
-- ----------------------------
DROP TABLE IF EXISTS `test_user`;
CREATE TABLE `test_user`  (
  `user_id` int(11) NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
