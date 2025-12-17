/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : erbaojiaoxue

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 17/12/2025 18:04:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `teacher_id` int NOT NULL,
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `student_count` int NULL DEFAULT 0,
  `college` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `grade` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `course_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` enum('进行中','已结课') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '进行中',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`code` ASC) USING BTREE,
  INDEX `fk_class_teacher`(`teacher_id` ASC) USING BTREE,
  CONSTRAINT `fk_class_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of class
-- ----------------------------
INSERT INTO `class` VALUES (1, '20250402', 34, '21级西班牙语', 0, '西语学院', '大三', '西班牙语', '进行中', '2025-04-02 09:32:44');

-- ----------------------------
-- Table structure for objection
-- ----------------------------
DROP TABLE IF EXISTS `objection`;
CREATE TABLE `objection`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL COMMENT '异议用户id',
  `question_id` int NULL DEFAULT NULL COMMENT '题目id',
  `patient_analyse_id` int NULL DEFAULT NULL COMMENT '病例id',
  `question_set_id` int NULL DEFAULT NULL COMMENT '套题id',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '题目',
  `answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '答案',
  `objection_answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '异议答案',
  `objection_argument` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '异议理由',
  `type` enum('CASE','BANK') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '异议类型 病例/题库',
  `objection_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '异议用户',
  `processing_state` int NULL DEFAULT 0 COMMENT '处理状态 0表示未处理 1表示已处理',
  `processing_result` int NULL DEFAULT NULL COMMENT '处理结果 1表示采纳 0表示驳回',
  `processing_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'admin' COMMENT '处理用户',
  `processing_reply` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理回复',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 83 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '异议表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of objection
-- ----------------------------
INSERT INTO `objection` VALUES (59, 36, NULL, 160, NULL, '/unzip/2-1.jpg', '2', '切面1', '这是对李四第一张图的异议', 'CASE', '2024', 1, 1, 'admin', 'ss', '2024-06-04 15:23:24', '2024-06-04 15:23:24', 1);
INSERT INTO `objection` VALUES (60, 36, NULL, 161, NULL, '/unzip/2-2.jpg', '4', '切面5', '这是对李四第二张图的异议', 'CASE', '2024', 1, 1, 'admin', 'ssss', '2024-06-04 15:23:31', '2024-06-04 15:23:31', 1);
INSERT INTO `objection` VALUES (61, 35, NULL, 173, NULL, '/img/2-1.jpg', '切面7', '切面6', '不对', 'CASE', '学员1', 1, 1, 'admin', 'sss', '2024-06-04 15:40:43', '2024-06-04 15:40:43', 0);
INSERT INTO `objection` VALUES (62, 35, NULL, 174, NULL, '/img/2-2.jpg', '非标准切面', '切面6', '不对的', 'CASE', '学员1', 1, 1, 'admin', 'sss', '2024-06-04 15:40:52', '2024-06-04 15:40:52', 0);
INSERT INTO `objection` VALUES (69, 34, NULL, 160, NULL, '/img/2-3.jpg', '2', '切面5', 'ss', 'CASE', 'admin', 0, NULL, 'admin', NULL, '2024-06-05 11:24:15', '2024-06-05 11:24:15', 1);
INSERT INTO `objection` VALUES (70, 34, NULL, 161, NULL, '/img/2-4.jpg', '4', '切面2', 'ss', 'CASE', 'admin', 0, NULL, 'admin', NULL, '2024-06-05 11:31:25', '2024-06-05 11:31:25', 1);
INSERT INTO `objection` VALUES (73, 36, NULL, 188, NULL, '/img/2-5.jpg', '切面1', '切面1', 'qwerty', 'CASE', '2024', 1, 1, 'admin', '222', '2024-06-05 15:30:54', '2024-06-05 15:30:54', 0);
INSERT INTO `objection` VALUES (74, 36, NULL, 196, NULL, '/img/2-6.jpg', '非标准切面', '切面2', '1234', 'CASE', '2024', 1, 0, 'admin', '111', '2024-06-05 16:32:48', '2024-06-05 16:32:48', 1);
INSERT INTO `objection` VALUES (75, 36, NULL, 197, NULL, '/img/2-7.jpg', '切面1', '切面6', '123', 'CASE', '2024', 1, 1, 'admin', '同意你的意见', '2024-06-05 16:32:52', '2024-06-05 16:32:52', 0);
INSERT INTO `objection` VALUES (76, 72, 242, NULL, NULL, '/unzip/2-1.jpg', '切面4', '切面2', '就该是切面2', 'BANK', '2023', 1, 1, 'admin', '对的', '2024-06-06 15:42:11', '2024-06-06 15:42:11', 0);
INSERT INTO `objection` VALUES (77, 36, 233, NULL, NULL, '/unzip/2-2.jpg', '切面7', '切面4', '就是切面4', 'BANK', '2024', 1, 1, 'admin', 'duidui', '2024-06-06 15:43:56', '2024-06-06 15:43:56', 0);
INSERT INTO `objection` VALUES (78, 36, 232, NULL, NULL, '/unzip/2-3.jpg', '非标准切面', '切面2', '2222', 'BANK', '2024', 0, NULL, 'admin', NULL, '2024-06-18 14:41:44', '2024-06-18 14:41:44', 0);
INSERT INTO `objection` VALUES (79, 36, 291, NULL, 1, '/unzip/2-3.jpg', '切面4', '切面3', '123', 'BANK', '2024', 0, NULL, 'admin', NULL, '2025-04-16 14:23:43', '2025-04-16 14:23:43', 0);
INSERT INTO `objection` VALUES (80, 36, 325, 198, NULL, '/unzip/2-9.jpg', '切面6', '切面1', '222', 'CASE', '2024', 0, NULL, 'admin', NULL, '2025-04-22 21:10:42', '2025-04-22 21:10:42', 0);
INSERT INTO `objection` VALUES (81, 36, NULL, 158, NULL, '/unzip/2-9.jpg', '切面1', '切面6', '222', 'CASE', '2024', 0, NULL, 'admin', NULL, '2025-04-22 21:21:50', '2025-04-22 21:21:50', 0);
INSERT INTO `objection` VALUES (82, 36, 354, NULL, 1, '\\img\\59a0fde2.jpg', '切面1', '切面1', 'wwwww', 'BANK', '2024', 1, 0, 'admin', '不采纳\n', '2025-05-15 15:13:21', '2025-05-15 15:13:21', 0);

-- ----------------------------
-- Table structure for patient
-- ----------------------------
DROP TABLE IF EXISTS `patient`;
CREATE TABLE `patient`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '病人id',
  `user_id` int NULL DEFAULT NULL COMMENT '上传病例用户id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '病人姓名',
  `sex` enum('MALE','FEMALE') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别 男/女',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `ultrasonic_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '超声号',
  `instrument_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '仪器类型',
  `aspect_correct` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '切面准确率',
  `outpatient_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '门诊号',
  `ultrasonic_video` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '超声视频',
  `check_item` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '检查项目',
  `type` enum('VIDEO','IMG') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型',
  `objection_num` int NULL DEFAULT NULL COMMENT '异议数',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 198 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '病人表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of patient
-- ----------------------------
INSERT INTO `patient` VALUES (131, 19, '张三', 'MALE', 22, '101001.0', '1001.0', '100.0%', '11', '', '淋巴结', 'IMG', 0, '2024-05-28 13:50:06', '2024-05-28 13:50:06', 1);
INSERT INTO `patient` VALUES (132, 19, '李四', 'FEMALE', 23, '101002.0', '1002.0', '50.0%', '2.0', '', '肝脏', 'IMG', NULL, '2024-05-28 13:50:06', '2024-05-28 13:50:06', 1);
INSERT INTO `patient` VALUES (133, 19, '张三', 'MALE', 22, '101001.0', '1001.0', '100.0%', '1.0', '', '淋巴结', 'IMG', NULL, '2024-05-28 13:54:40', '2024-05-28 13:54:40', 1);
INSERT INTO `patient` VALUES (134, 19, '李四', 'FEMALE', 23, '101002.0', '1002.0', '50.0%', '2.0', '', '肝脏', 'IMG', NULL, '2024-05-28 13:54:40', '2024-05-28 13:54:40', 1);
INSERT INTO `patient` VALUES (135, 36, '张三', 'FEMALE', 22, '101001.0', '1001.0', '100.0%', '1.0', '', '淋巴结', 'IMG', 1, '2024-05-28 14:19:46', '2024-05-28 14:19:46', 0);
INSERT INTO `patient` VALUES (136, 36, '李四', 'MALE', 23, '101002.0', '1002.0', '50.0%', '2.0', '', '肝脏', 'IMG', 0, '2024-05-28 14:19:46', '2024-05-28 14:19:46', 0);
INSERT INTO `patient` VALUES (143, 35, '张三', 'FEMALE', 33, 'undefined', 'null', '50.0%', 'null', NULL, 'null', 'IMG', 2, '2024-05-28 15:27:37', '2024-05-28 15:27:37', 0);
INSERT INTO `patient` VALUES (149, 36, '2024', 'MALE', 22, 'undefined', 'null', '0.0%', 'null', 'video/WeChat_20240523140100.mp4', 'null', 'VIDEO', NULL, '2024-06-03 11:56:27', '2024-06-03 11:56:27', 1);
INSERT INTO `patient` VALUES (151, 53, '病例熊', 'MALE', 30, 'undefined', 'null', '100.0%', 'null', NULL, 'null', 'IMG', 1, '2024-06-04 16:47:32', '2024-06-04 16:47:32', 1);
INSERT INTO `patient` VALUES (152, 53, '病例坤', 'MALE', 29, 'undefined', 'null', '100.0%', 'null', NULL, 'null', 'IMG', 1, '2024-06-04 16:48:41', '2024-06-04 16:48:41', 1);
INSERT INTO `patient` VALUES (153, 54, '病例龙', 'MALE', 25, 'undefined', 'null', '100.0%', 'null', NULL, 'null', 'IMG', 1, '2024-06-04 16:50:25', '2024-06-04 16:50:25', 1);
INSERT INTO `patient` VALUES (154, 36, '王五', 'MALE', 22, '22222', '2222222', '67.0%', '1111', NULL, '44444', 'IMG', 3, '2024-06-05 14:28:24', '2024-06-05 14:28:24', 0);
INSERT INTO `patient` VALUES (155, 36, '金线林', 'MALE', 11, 'undefined', 'null', '0.0%', 'null', NULL, 'null', 'IMG', NULL, '2024-06-07 15:10:50', '2024-06-07 15:10:50', 1);
INSERT INTO `patient` VALUES (156, 72, '王七七', 'MALE', 25, 'undefined', 'null', '100.0%', 'null', NULL, 'null', 'IMG', NULL, '2024-06-07 16:40:21', '2024-06-07 16:40:21', 1);
INSERT INTO `patient` VALUES (157, 73, '李金金', 'MALE', 65, 'undefined', 'null', '100.0%', 'null', NULL, 'null', 'IMG', NULL, '2024-06-07 16:58:39', '2024-06-07 16:58:39', 1);
INSERT INTO `patient` VALUES (158, 72, '樊冰林', 'MALE', 56, 'undefined', 'null', '100.0%', 'null', NULL, 'null', 'IMG', NULL, '2024-06-07 17:05:52', '2024-06-07 17:05:52', 1);
INSERT INTO `patient` VALUES (159, 72, '王思', 'MALE', 56, 'undefined', 'null', '100.0%', 'null', NULL, 'null', 'IMG', NULL, '2024-06-07 17:12:23', '2024-06-07 17:12:23', 1);
INSERT INTO `patient` VALUES (160, 73, '第一名', 'MALE', 24, 'fdvfd', 'svdsvd', '100.0%', 'vsvd', '', 'svdv', 'IMG', NULL, '2024-06-07 17:12:51', '2024-06-07 17:12:51', 1);
INSERT INTO `patient` VALUES (161, 73, '第二名', 'FEMALE', 15, 'vsfv', 'dscsd222', '100.0%', 'vsd', '', 'svdv', 'IMG', NULL, '2024-06-07 17:12:51', '2024-06-07 17:12:51', 1);
INSERT INTO `patient` VALUES (162, 72, '第一名', 'MALE', 24, 'fdvfd', 'svdsvd', '100.0%', 'vsvd', '', 'svdv', 'IMG', NULL, '2024-06-07 17:17:54', '2024-06-07 17:17:54', 1);
INSERT INTO `patient` VALUES (163, 72, '第二名', 'FEMALE', 15, 'vsfv', 'dscsd222', '100.0%', 'vsd', '', 'svdv', 'IMG', NULL, '2024-06-07 17:17:54', '2024-06-07 17:17:54', 1);
INSERT INTO `patient` VALUES (164, 36, '病例-1', 'FEMALE', 1, 'undefined', 'null', '0.0%', 'null', 'video/11.mp4', 'null', 'VIDEO', NULL, '2024-06-12 14:17:38', '2024-06-12 14:17:38', 1);
INSERT INTO `patient` VALUES (165, 36, '病例-2', 'FEMALE', 2, 'undefined', 'null', '100.0%', 'null', NULL, 'null', 'IMG', NULL, '2024-06-12 14:17:53', '2024-06-12 14:17:53', 1);
INSERT INTO `patient` VALUES (168, 36, '病例-3', 'MALE', 22, 'undefined', 'null', '0.0%', 'null', NULL, 'null', 'IMG', NULL, '2024-06-20 14:00:33', '2024-06-20 14:00:33', 0);
INSERT INTO `patient` VALUES (169, 36, '病例-4', 'MALE', 33, 'undefined', 'null', '0.0%', 'null', NULL, 'null', 'IMG', NULL, '2024-06-20 14:00:46', '2024-06-20 14:00:46', 0);
INSERT INTO `patient` VALUES (170, 36, '病例-5', 'FEMALE', 3, 'undefined', 'null', '0.0%', 'null', NULL, 'null', 'IMG', NULL, '2024-06-20 14:00:56', '2024-06-20 14:00:56', 0);
INSERT INTO `patient` VALUES (171, 36, '病例-6', 'MALE', 1, 'undefined', 'null', '0.0%', 'null', NULL, 'null', 'IMG', NULL, '2024-06-20 14:01:06', '2024-06-20 14:01:06', 0);
INSERT INTO `patient` VALUES (172, 36, '病例-7', 'MALE', 5, 'undefined', 'null', '0.0%', 'null', NULL, 'null', 'IMG', NULL, '2024-06-20 14:01:17', '2024-06-20 14:01:17', 0);
INSERT INTO `patient` VALUES (173, 36, '6', 'MALE', 66, 'undefined', 'null', '0.0%', 'null', NULL, 'null', 'IMG', NULL, '2024-06-20 14:01:28', '2024-06-20 14:01:28', 1);
INSERT INTO `patient` VALUES (174, 72, 'test', 'MALE', 20, NULL, NULL, '100.0%', NULL, NULL, NULL, 'IMG', NULL, '2024-06-26 13:44:20', '2024-06-26 13:44:20', 0);
INSERT INTO `patient` VALUES (175, 72, 'test', 'MALE', 21, NULL, NULL, '100.0%', NULL, NULL, NULL, 'IMG', NULL, '2024-06-26 13:48:37', '2024-06-26 13:48:37', 0);
INSERT INTO `patient` VALUES (176, 72, 'test', 'MALE', 21, NULL, NULL, '100.0%', NULL, NULL, NULL, 'IMG', NULL, '2024-06-26 13:49:12', '2024-06-26 13:49:12', 0);
INSERT INTO `patient` VALUES (182, 36, '金建勋', 'MALE', 22, 'undefined', 'null', '100.0%', 'null', NULL, 'null', 'IMG', NULL, '2025-04-22 14:33:02', '2025-04-22 14:33:02', 1);
INSERT INTO `patient` VALUES (183, 36, '是我', 'MALE', 33, 'undefined', 'null', '100.0%', 'null', NULL, 'null', 'IMG', NULL, '2025-04-22 14:41:37', '2025-04-22 14:41:37', 0);
INSERT INTO `patient` VALUES (184, 36, '宋烁', 'FEMALE', 22, 'undefined', 'null', '100.0%', 'null', NULL, 'null', 'IMG', NULL, '2025-04-22 14:44:52', '2025-04-22 14:44:52', 1);
INSERT INTO `patient` VALUES (185, 36, '希望', 'MALE', 22, 'undefined', 'null', '0.0%', 'null', 'video/WeChat_20250422150403.mp4', 'null', 'VIDEO', NULL, '2025-04-22 15:05:06', '2025-04-22 15:05:06', 0);
INSERT INTO `patient` VALUES (186, 36, '张三', 'MALE', 22, '101001.0', '1001.0', '0.0%', '1.0', '', '淋巴结', 'IMG', NULL, '2025-04-22 15:49:00', '2025-04-22 15:49:00', 0);
INSERT INTO `patient` VALUES (187, 36, '李四', 'FEMALE', 23, '101002.0', '1002.0', '0.0%', '2.0', '', '肝脏', 'IMG', NULL, '2025-04-22 15:49:00', '2025-04-22 15:49:00', 0);
INSERT INTO `patient` VALUES (188, 36, '22222', 'MALE', 11, 'undefined', 'null', '100.0%', 'null', NULL, 'null', 'IMG', NULL, '2025-05-15 15:15:52', '2025-05-15 15:15:52', 0);
INSERT INTO `patient` VALUES (189, 36, '222', 'MALE', 11, 'undefined', 'null', '100.0%', 'null', NULL, 'null', 'IMG', NULL, '2025-05-15 15:16:22', '2025-05-15 15:16:22', 1);
INSERT INTO `patient` VALUES (190, 95, '王津津', 'MALE', 22, 'X108372', '2107839', '0.0%', '', 'video/WeChat_20240523140100.mp4', '', 'VIDEO', NULL, '2025-05-21 10:16:10', '2025-05-21 10:16:10', 0);
INSERT INTO `patient` VALUES (191, 95, '123', 'MALE', 22, 'undefined', 'null', '100.0%', 'null', NULL, 'null', 'IMG', NULL, '2025-05-21 10:17:33', '2025-05-21 10:17:33', 0);
INSERT INTO `patient` VALUES (192, 95, '张三', 'MALE', 22, '101001.0', '1001.0', '0.0%', '1.0', '', '淋巴结', 'IMG', NULL, '2025-05-21 16:46:02', '2025-05-21 16:46:02', 0);
INSERT INTO `patient` VALUES (193, 95, '李四', 'FEMALE', 23, '101002.0', '1002.0', '0.0%', '2.0', '', '肝脏', 'IMG', NULL, '2025-05-21 16:46:02', '2025-05-21 16:46:02', 0);
INSERT INTO `patient` VALUES (194, 95, 'Wang Xi', 'MALE', 23, 'undefined', 'null', '100.0%', 'null', NULL, 'null', 'IMG', NULL, '2025-05-21 20:53:58', '2025-05-21 20:53:58', 0);
INSERT INTO `patient` VALUES (195, 36, '1234', 'MALE', 12, 'undefined', 'null', '0.0%', 'null', 'video/WeChat_20240523140100.mp4', 'null', 'VIDEO', NULL, '2025-05-22 09:40:07', '2025-05-22 09:40:07', 0);
INSERT INTO `patient` VALUES (196, 36, '张三', 'MALE', 22, '101001.0', '1001.0', '0.0%', '1.0', '', '淋巴结', 'IMG', NULL, '2025-05-22 09:41:12', '2025-05-22 09:41:12', 0);
INSERT INTO `patient` VALUES (197, 36, '李四', 'FEMALE', 23, '101002.0', '1002.0', '0.0%', '2.0', '', '肝脏', 'IMG', NULL, '2025-05-22 09:41:12', '2025-05-22 09:41:12', 0);

-- ----------------------------
-- Table structure for patient_analyse
-- ----------------------------
DROP TABLE IF EXISTS `patient_analyse`;
CREATE TABLE `patient_analyse`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `patient_id` int NULL DEFAULT NULL COMMENT '病人id',
  `original_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原图/原帧',
  `analyse_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分析图/分析帧',
  `analyse_answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分析答案',
  `is_standard` tinyint(1) NULL DEFAULT NULL COMMENT '是否标准切面',
  `aspect_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '切面名称',
  `is_objection` tinyint(1) NULL DEFAULT NULL COMMENT '是否异议',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除',
  `possibility` double(10, 1) NULL DEFAULT NULL COMMENT '概率',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 241 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '病例分析页面' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of patient_analyse
-- ----------------------------
INSERT INTO `patient_analyse` VALUES (145, 128, '/img/2-1.jpg', NULL, '切面7', 1, NULL, NULL, '2024-05-28 13:28:36', '2024-05-28 13:28:36', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (146, 129, '/img/2-2.jpg', NULL, '切面5', 1, NULL, NULL, '2024-05-28 13:31:24', '2024-05-28 13:31:24', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (147, 129, '/img/2-3.jpg', NULL, '切面7', 1, NULL, NULL, '2024-05-28 13:31:25', '2024-05-28 13:31:25', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (148, 129, '/img/2-4.jpg', NULL, '切面7', 1, NULL, NULL, '2024-05-28 13:31:25', '2024-05-28 13:31:25', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (150, 131, '/unzip/2-1.jpg', NULL, '切面1', 1, NULL, NULL, '2024-05-28 13:50:06', '2024-05-28 13:50:06', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (151, 131, '/unzip/2-2.jpg', NULL, '切面6', 1, NULL, NULL, '2024-05-28 13:50:06', '2024-05-28 13:50:06', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (152, 132, '/unzip/2-3.jpg', NULL, '非标准切面', 0, NULL, NULL, '2024-05-28 13:50:06', '2024-05-28 13:50:06', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (153, 132, '/unzip/2-4.jpg', NULL, '切面7', 1, NULL, NULL, '2024-05-28 13:50:06', '2024-05-28 13:50:06', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (154, 133, '/unzip/2-5.jpg', NULL, '切面1', 1, NULL, NULL, '2024-05-28 13:54:40', '2024-05-28 13:54:40', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (155, 133, '/unzip/2-6.jpg', NULL, '切面6', 1, NULL, NULL, '2024-05-28 13:54:40', '2024-05-28 13:54:40', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (156, 134, '/unzip/2-7.jpg', NULL, '非标准切面', 0, NULL, NULL, '2024-05-28 13:54:40', '2024-05-28 13:54:40', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (157, 134, '/unzip/2-8.jpg', NULL, '切面7', 1, NULL, NULL, '2024-05-28 13:54:40', '2024-05-28 13:54:40', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (158, 135, '/unzip/2-9.jpg', NULL, '切面1', 1, NULL, NULL, '2024-05-28 14:19:46', '2024-05-28 14:19:46', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (159, 135, '/unzip/2-10.jpg', NULL, '切面6', 1, NULL, NULL, '2024-05-28 14:19:46', '2024-05-28 14:19:46', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (160, 136, '/unzip/2-11.jpg', NULL, '切面1', 0, NULL, NULL, '2024-05-28 14:19:46', '2024-05-28 14:19:46', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (161, 136, '/unzip/2-12.jpg', NULL, '切面5', 1, NULL, NULL, '2024-05-28 14:19:46', '2024-05-28 14:19:46', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (173, 143, '/img/2-1.jpg', NULL, '切面6', 1, NULL, NULL, '2024-05-28 15:27:38', '2024-05-28 15:27:38', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (174, 143, '/img/2-2.jpg', NULL, '切面6', 0, NULL, NULL, '2024-05-28 15:27:38', '2024-05-28 15:27:38', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (185, 151, '/img/2-3.jpg', NULL, '切面3', 1, NULL, NULL, '2024-06-04 16:47:33', '2024-06-04 16:47:33', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (186, 152, '/img/2-4.jpg', NULL, '切面3', 1, NULL, NULL, '2024-06-04 16:48:41', '2024-06-04 16:48:41', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (187, 153, '/img/2-5.jpg', NULL, '切面3', 1, NULL, NULL, '2024-06-04 16:50:25', '2024-06-04 16:50:25', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (188, 154, '/img/2-6.jpg', NULL, '切面1', 1, NULL, NULL, '2024-06-05 14:28:25', '2024-06-05 14:28:25', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (189, 154, '/img/2-7.jpg', NULL, '切面6', 1, NULL, NULL, '2024-06-05 14:28:26', '2024-06-05 14:28:26', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (190, 154, '/img/2-8.jpg', NULL, '非标准切面', 0, NULL, NULL, '2024-06-05 14:28:26', '2024-06-05 14:28:26', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (191, 154, '/img/2-9.jpg', NULL, '切面1', 1, NULL, NULL, '2024-06-05 14:28:27', '2024-06-05 14:28:27', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (192, 154, '/img/2-10.jpg', NULL, '切面6', 1, NULL, NULL, '2024-06-05 14:28:28', '2024-06-05 14:28:28', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (193, 154, '/img/2-11.jpg', NULL, '非标准切面', 0, NULL, NULL, '2024-06-05 14:28:28', '2024-06-05 14:28:28', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (194, 154, '/img/2-12.jpg', NULL, '切面1', 1, NULL, NULL, '2024-06-05 14:28:29', '2024-06-05 14:28:29', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (195, 154, '/img/2-13.jpg', NULL, '切面6', 1, NULL, NULL, '2024-06-05 14:28:29', '2024-06-05 14:28:29', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (196, 154, '/img/2-14.jpg', NULL, '非标准切面', 0, NULL, NULL, '2024-06-05 14:28:30', '2024-06-05 14:28:30', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (197, 154, '/img/2-15.jpg', NULL, '切面6', 1, NULL, NULL, '2024-06-05 14:28:30', '2024-06-05 14:28:30', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (198, 154, '/img/2-16.jpg', NULL, '切面6', 1, NULL, NULL, '2024-06-05 14:28:31', '2024-06-05 14:28:31', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (199, 154, '/img/2-17.jpg', NULL, '非标准切面', 0, NULL, NULL, '2024-06-05 14:28:31', '2024-06-05 14:28:31', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (200, 155, '/img/2-18.jpg', NULL, '非标准切面', 0, NULL, NULL, '2024-06-07 15:10:51', '2024-06-07 15:10:51', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (201, 156, '/img/2-19.jpg', NULL, '切面3', 1, NULL, NULL, '2024-06-07 16:40:22', '2024-06-07 16:40:22', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (202, 157, '/img/2-20.jpg', NULL, '切面3', 1, NULL, NULL, '2024-06-07 16:58:40', '2024-06-07 16:58:40', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (203, 158, '/img/2-21.jpg', NULL, '切面3', 1, NULL, NULL, '2024-06-07 17:05:53', '2024-06-07 17:05:53', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (204, 159, '/img/2-22.jpg', NULL, '切面3', 1, NULL, NULL, '2024-06-07 17:12:24', '2024-06-07 17:12:24', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (205, 160, '/unzip/2-1.jpg', NULL, '切面3', 1, NULL, NULL, '2024-06-07 17:12:51', '2024-06-07 17:12:51', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (206, 160, '/unzip/2-2.jpg', NULL, '切面6', 1, NULL, NULL, '2024-06-07 17:12:51', '2024-06-07 17:12:51', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (207, 161, '/unzip/2-3.jpg', NULL, '切面6', 1, NULL, NULL, '2024-06-07 17:12:51', '2024-06-07 17:12:51', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (208, 161, '/unzip/2-4.jpg', NULL, '切面6', 1, NULL, NULL, '2024-06-07 17:12:51', '2024-06-07 17:12:51', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (209, 162, '/unzip/2-5.jpg', NULL, '切面3', 1, NULL, NULL, '2024-06-07 17:17:54', '2024-06-07 17:17:54', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (210, 162, '/unzip/2-6.jpg', NULL, '切面6', 1, NULL, NULL, '2024-06-07 17:17:54', '2024-06-07 17:17:54', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (211, 163, '/unzip/2-7.jpg', NULL, '切面6', 1, NULL, NULL, '2024-06-07 17:17:54', '2024-06-07 17:17:54', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (212, 163, '/unzip/2-8.jpg', NULL, '切面6', 1, NULL, NULL, '2024-06-07 17:17:54', '2024-06-07 17:17:54', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (213, 165, '/img/2-1.jpg', NULL, '切面1', 1, NULL, NULL, '2024-06-12 14:17:54', '2024-06-12 14:17:54', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (214, 174, '/img/2-2.jpg', NULL, '切面3', 1, NULL, NULL, '2024-06-26 13:46:54', '2024-06-26 13:46:54', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (215, 175, '/img/2-3.jpg', NULL, '切面3', 1, NULL, NULL, '2024-06-26 13:48:41', '2024-06-26 13:48:41', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (216, 176, '/img/2-4.jpg', NULL, '切面3', 1, NULL, NULL, '2024-06-26 13:49:16', '2024-06-26 13:49:16', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (217, 182, '\\img\\fe0899dd.jpg', NULL, '标准切面', 1, NULL, NULL, '2025-04-22 14:33:02', '2025-04-22 14:33:02', 1, 0.8);
INSERT INTO `patient_analyse` VALUES (218, 183, '\\img\\4e88b0e0.jpg', NULL, '标准切面', 1, NULL, NULL, '2025-04-22 14:41:37', '2025-04-22 14:41:37', 0, 0.8);
INSERT INTO `patient_analyse` VALUES (219, 184, '\\img\\5c7babf6.jpg', NULL, '标准切面', 1, NULL, NULL, '2025-04-22 14:44:52', '2025-04-22 14:44:52', 1, 0.8);
INSERT INTO `patient_analyse` VALUES (220, 184, '\\img\\250d3b7a.jpg', NULL, '标准切面', 1, NULL, NULL, '2025-04-22 14:44:52', '2025-04-22 14:44:52', 1, 0.8);
INSERT INTO `patient_analyse` VALUES (221, 186, '\\unzip\\a5ec7cd82-1.jpg', NULL, '非标准切面', 1, NULL, NULL, '2025-04-22 15:49:00', '2025-04-22 15:49:00', 0, 0.0);
INSERT INTO `patient_analyse` VALUES (222, 186, '\\unzip\\942751fe2-2.jpg', NULL, '非标准切面', 1, NULL, NULL, '2025-04-22 15:49:00', '2025-04-22 15:49:00', 0, 0.0);
INSERT INTO `patient_analyse` VALUES (223, 187, '\\unzip\\4a1c5d812-3.jpg', NULL, '非标准切面', 1, NULL, NULL, '2025-04-22 15:49:00', '2025-04-22 15:49:00', 0, 0.0);
INSERT INTO `patient_analyse` VALUES (224, 187, '\\unzip\\420fb0232-4.jpg', NULL, '非标准切面', 1, NULL, NULL, '2025-04-22 15:49:00', '2025-04-22 15:49:00', 0, 0.0);
INSERT INTO `patient_analyse` VALUES (225, 188, '\\img\\806d6487.jpg', NULL, '标准切面', 1, NULL, NULL, '2025-05-15 15:15:52', '2025-05-15 15:15:52', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (226, 189, '\\img\\5a2d4da1.jpg', NULL, '标准切面', 1, NULL, NULL, '2025-05-15 15:16:22', '2025-05-15 15:16:22', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (227, 189, '\\img\\cea0a219.jpg', NULL, '标准切面', 1, NULL, NULL, '2025-05-15 15:16:22', '2025-05-15 15:16:22', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (228, 189, '\\img\\8d50d5ab.jpg', NULL, '标准切面', 1, NULL, NULL, '2025-05-15 15:16:22', '2025-05-15 15:16:22', 1, 1.0);
INSERT INTO `patient_analyse` VALUES (229, 191, '\\img\\1b67dd1c.jpg', NULL, '标准切面', 1, NULL, NULL, '2025-05-21 10:17:33', '2025-05-21 10:17:33', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (230, 191, '\\img\\f92facad.jpg', NULL, '标准切面', 1, NULL, NULL, '2025-05-21 10:17:33', '2025-05-21 10:17:33', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (231, 191, '\\img\\02d359e6.jpg', NULL, '标准切面', 1, NULL, NULL, '2025-05-21 10:17:33', '2025-05-21 10:17:33', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (232, 192, '\\unzip\\d9433e8a2-1.jpg', NULL, '非标准切面', 0, NULL, NULL, '2025-05-21 16:46:02', '2025-05-21 16:46:02', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (233, 192, '\\unzip\\cb3306f42-2.jpg', NULL, '非标准切面', 0, NULL, NULL, '2025-05-21 16:46:02', '2025-05-21 16:46:02', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (234, 193, '\\unzip\\aa432a7c2-3.jpg', NULL, '非标准切面', 0, NULL, NULL, '2025-05-21 16:46:02', '2025-05-21 16:46:02', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (235, 193, '\\unzip\\c4403d812-4.jpg', NULL, '非标准切面', 0, NULL, NULL, '2025-05-21 16:46:02', '2025-05-21 16:46:02', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (236, 194, '\\img\\bb679439.jpg', NULL, '标准切面', 1, NULL, NULL, '2025-05-21 20:53:58', '2025-05-21 20:53:58', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (237, 196, '\\unzip\\c8639d7a2-1.jpg', NULL, '非标准切面', 0, NULL, NULL, '2025-05-22 09:41:12', '2025-05-22 09:41:12', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (238, 196, '\\unzip\\13b8861f2-2.jpg', NULL, '非标准切面', 0, NULL, NULL, '2025-05-22 09:41:12', '2025-05-22 09:41:12', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (239, 197, '\\unzip\\6ab77ac52-3.jpg', NULL, '非标准切面', 0, NULL, NULL, '2025-05-22 09:41:12', '2025-05-22 09:41:12', 0, 1.0);
INSERT INTO `patient_analyse` VALUES (240, 197, '\\unzip\\81566d1a2-4.jpg', NULL, '非标准切面', 0, NULL, NULL, '2025-05-22 09:41:12', '2025-05-22 09:41:12', 0, 1.0);

-- ----------------------------
-- Table structure for practice_page
-- ----------------------------
DROP TABLE IF EXISTS `practice_page`;
CREATE TABLE `practice_page`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `question_id` int NULL DEFAULT NULL COMMENT '题目id',
  `question_set_id` int NULL DEFAULT NULL COMMENT '套题id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错题名称',
  `answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '题目正确答案',
  `user_answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户填写答案',
  `is_objection` tinyint(1) NULL DEFAULT NULL COMMENT '是否有异议',
  `is_true` tinyint(1) NULL DEFAULT NULL COMMENT '是否正确',
  `is_collect` tinyint(1) NULL DEFAULT NULL COMMENT '是否收藏',
  `topic_source` enum('BANK','CASE') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '题目来源 病例/题库',
  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'admin' COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 407 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '错题和收藏页面' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of practice_page
-- ----------------------------
INSERT INTO `practice_page` VALUES (262, 62, 232, NULL, '/unzip/2-1.jpg', '非标准切面', '非标准切面', 0, 1, 0, 'BANK', 'admin', '2024-06-05 17:22:53', '2024-06-05 17:22:53', 1);
INSERT INTO `practice_page` VALUES (263, 62, 233, NULL, '/unzip/2-2.jpg', '切面7', '切面5', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:23:26', '2024-06-05 17:23:26', 1);
INSERT INTO `practice_page` VALUES (264, 62, 234, NULL, '/unzip/2-3.jpg', '切面5', '切面6', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:23:28', '2024-06-05 17:23:28', 1);
INSERT INTO `practice_page` VALUES (265, 62, 235, NULL, '/unzip/2-4.jpg', '切面6', '切面6', 0, 1, 0, 'BANK', 'admin', '2024-06-05 17:23:30', '2024-06-05 17:23:30', 1);
INSERT INTO `practice_page` VALUES (266, 62, 236, NULL, '/unzip/2-5.jpg', '切面3', '切面7', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:23:33', '2024-06-05 17:23:33', 1);
INSERT INTO `practice_page` VALUES (267, 62, 237, NULL, '/unzip/2-6.jpg', '切面6', '非标准切面', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:23:35', '2024-06-05 17:23:35', 1);
INSERT INTO `practice_page` VALUES (268, 62, 238, NULL, '/unzip/2-7.jpg', '切面6', '非标准切面', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:23:38', '2024-06-05 17:23:38', 1);
INSERT INTO `practice_page` VALUES (269, 62, 239, NULL, '/unzip/2-8.jpg', '非标准切面', '非标准切面', 0, 1, 0, 'BANK', 'admin', '2024-06-05 17:23:40', '2024-06-05 17:23:40', 1);
INSERT INTO `practice_page` VALUES (270, 63, 232, NULL, '/unzip/2-9.jpg', '非标准切面', '非标准切面', 0, 1, 0, 'BANK', 'admin', '2024-06-05 17:24:09', '2024-06-05 17:24:09', 1);
INSERT INTO `practice_page` VALUES (271, 63, 233, NULL, '/unzip/2-10.jpg', '切面7', '切面2', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:24:11', '2024-06-05 17:24:11', 1);
INSERT INTO `practice_page` VALUES (272, 63, 234, NULL, '/unzip/2-11.jpg', '切面5', '切面5', 0, 1, 0, 'BANK', 'admin', '2024-06-05 17:24:13', '2024-06-05 17:24:13', 1);
INSERT INTO `practice_page` VALUES (273, 63, 235, NULL, '/unzip/2-12.jpg', '切面6', '切面6', 0, 1, 0, 'BANK', 'admin', '2024-06-05 17:24:15', '2024-06-05 17:24:15', 1);
INSERT INTO `practice_page` VALUES (274, 63, 236, NULL, '/unzip/2-13.jpg', '切面3', '切面6', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:24:17', '2024-06-05 17:24:17', 1);
INSERT INTO `practice_page` VALUES (275, 63, 237, NULL, '/unzip/2-15.jpg', '切面6', '切面7', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:24:19', '2024-06-05 17:24:19', 1);
INSERT INTO `practice_page` VALUES (276, 63, 239, NULL, '/unzip/2-14.jpg', '非标准切面', '切面7', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:24:27', '2024-06-05 17:24:27', 1);
INSERT INTO `practice_page` VALUES (277, 64, 232, NULL, '/unzip/2-16.jpg', '非标准切面', '切面3', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:25:24', '2024-06-05 17:25:24', 1);
INSERT INTO `practice_page` VALUES (278, 64, 233, NULL, '/unzip/2-17.jpg', '切面7', '切面7', 0, 1, 0, 'BANK', 'admin', '2024-06-05 17:25:26', '2024-06-05 17:25:26', 1);
INSERT INTO `practice_page` VALUES (279, 64, 234, NULL, '/unzip/2-18.jpg', '切面5', '切面5', 0, 1, 0, 'BANK', 'admin', '2024-06-05 17:25:28', '2024-06-05 17:25:28', 1);
INSERT INTO `practice_page` VALUES (280, 64, 235, NULL, '/unzip/2-19.jpg', '切面6', '切面1', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:25:31', '2024-06-05 17:25:31', 1);
INSERT INTO `practice_page` VALUES (281, 64, 236, NULL, '/unzip/2-20.jpg', '切面3', '非标准切面', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:25:33', '2024-06-05 17:25:33', 1);
INSERT INTO `practice_page` VALUES (282, 64, 237, NULL, '/unzip/2-21.jpg', '切面6', '切面2', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:25:35', '2024-06-05 17:25:35', 1);
INSERT INTO `practice_page` VALUES (283, 65, 232, NULL, '/unzip/2-22.jpg', '非标准切面', '非标准切面', 0, 1, 0, 'BANK', 'admin', '2024-06-05 17:25:41', '2024-06-05 17:25:41', 1);
INSERT INTO `practice_page` VALUES (284, 65, 233, NULL, '/unzip/2-1.jpg', '切面7', '切面4', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:25:43', '2024-06-05 17:25:43', 1);
INSERT INTO `practice_page` VALUES (285, 65, 241, NULL, '/unzip/2-2.jpg', '切面4', '非标准切面', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:25:46', '2024-06-05 17:25:46', 1);
INSERT INTO `practice_page` VALUES (286, 65, 240, NULL, '/unzip/2-3.jpg', '切面5', '切面4', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:25:48', '2024-06-05 17:25:48', 1);
INSERT INTO `practice_page` VALUES (287, 65, 234, NULL, '/unzip/2-4.jpg', '切面5', '切面4', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:25:53', '2024-06-05 17:25:53', 1);
INSERT INTO `practice_page` VALUES (288, 66, 232, NULL, '/unzip/2-5.jpg', '非标准切面', '切面3', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:26:01', '2024-06-05 17:26:01', 1);
INSERT INTO `practice_page` VALUES (289, 66, 233, NULL, '/unzip/2-6.jpg', '切面7', '切面7', 0, 1, 0, 'BANK', 'admin', '2024-06-05 17:26:03', '2024-06-05 17:26:03', 1);
INSERT INTO `practice_page` VALUES (290, 66, 234, NULL, '/unzip/2-7.jpg', '切面5', '切面6', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:26:05', '2024-06-05 17:26:05', 1);
INSERT INTO `practice_page` VALUES (291, 66, 235, NULL, '/unzip/2-8.jpg', '切面6', '切面4', 0, 0, 0, 'BANK', 'admin', '2024-06-05 17:26:07', '2024-06-05 17:26:07', 1);
INSERT INTO `practice_page` VALUES (292, 72, 232, NULL, '/unzip/2-9.jpg', '非标准切面', '切面2', 0, 0, 1, 'BANK', 'admin', '2024-06-06 13:47:17', '2024-06-06 13:47:17', 0);
INSERT INTO `practice_page` VALUES (293, 72, 233, NULL, '/unzip/2-10.jpg', '切面7', '切面2', 0, 0, 1, 'BANK', 'admin', '2024-06-06 13:47:21', '2024-06-06 13:47:21', 0);
INSERT INTO `practice_page` VALUES (294, 72, 234, NULL, '/unzip/2-11.jpg', '切面5', '切面6', 0, 0, 0, 'BANK', 'admin', '2024-06-06 13:47:24', '2024-06-06 13:47:24', 0);
INSERT INTO `practice_page` VALUES (295, 72, 235, NULL, '/unzip/2-12.jpg', '切面6', '切面6', 0, 1, 0, 'BANK', 'admin', '2024-06-06 13:47:26', '2024-06-06 13:47:26', 0);
INSERT INTO `practice_page` VALUES (296, 72, 236, NULL, '/unzip/2-13.jpg', '切面3', '切面5', 0, 0, 0, 'BANK', 'admin', '2024-06-06 13:47:32', '2024-06-06 13:47:32', 0);
INSERT INTO `practice_page` VALUES (297, 72, 237, NULL, '/unzip/2-15.jpg', '切面6', '切面6', 0, 1, 0, 'BANK', 'admin', '2024-06-06 13:47:35', '2024-06-06 13:47:35', 0);
INSERT INTO `practice_page` VALUES (298, 72, 238, NULL, '/unzip/2-14.jpg', '切面6', '切面6', 0, 1, 0, 'BANK', 'admin', '2024-06-06 13:47:39', '2024-06-06 13:47:39', 0);
INSERT INTO `practice_page` VALUES (299, 35, 232, NULL, '/unzip/2-16.jpg', '非标准切面', '切面4', 0, 0, 0, 'BANK', 'admin', '2024-06-06 13:50:34', '2024-06-06 13:50:34', 0);
INSERT INTO `practice_page` VALUES (300, 35, 233, NULL, '/unzip/2-17.jpg', '切面7', '切面7', 0, 1, 0, 'BANK', 'admin', '2024-06-06 13:50:36', '2024-06-06 13:50:36', 0);
INSERT INTO `practice_page` VALUES (301, 35, 234, NULL, '/unzip/2-18.jpg', '切面5', '切面3', 0, 0, 0, 'BANK', 'admin', '2024-06-06 13:50:40', '2024-06-06 13:50:40', 0);
INSERT INTO `practice_page` VALUES (302, 72, 239, NULL, '/unzip/2-19.jpg', '非标准切面', '切面6', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:41:46', '2024-06-06 15:41:46', 0);
INSERT INTO `practice_page` VALUES (303, 72, 240, NULL, '/unzip/2-20.jpg', '切面5', '切面5', 0, 1, 0, 'BANK', 'admin', '2024-06-06 15:41:49', '2024-06-06 15:41:49', 0);
INSERT INTO `practice_page` VALUES (304, 72, 241, NULL, '/unzip/2-21.jpg', '切面4', '切面4', 0, 1, 0, 'BANK', 'admin', '2024-06-06 15:41:57', '2024-06-06 15:41:57', 0);
INSERT INTO `practice_page` VALUES (305, 72, 242, NULL, '/unzip/2-22.jpg', '切面4', '切面5', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:41:59', '2024-06-06 15:41:59', 0);
INSERT INTO `practice_page` VALUES (306, 36, 232, NULL, '/unzip/2-1.jpg', '非标准切面', '切面4', 0, 0, 1, 'BANK', 'admin', '2024-06-06 15:43:45', '2024-06-06 15:43:45', 0);
INSERT INTO `practice_page` VALUES (307, 36, 233, NULL, '/unzip/2-2.jpg', '切面7', '切面4', 0, 0, 1, 'BANK', 'admin', '2024-06-06 15:43:48', '2024-06-06 15:43:48', 0);
INSERT INTO `practice_page` VALUES (308, 36, 234, NULL, '/unzip/2-3.jpg', '切面5', '切面4', 0, 0, 1, 'BANK', 'admin', '2024-06-06 15:44:53', '2024-06-06 15:44:53', 0);
INSERT INTO `practice_page` VALUES (309, 36, 235, NULL, '/unzip/2-4.jpg', '切面6', '切面6', 0, 1, 1, 'BANK', 'admin', '2024-06-06 15:44:55', '2024-06-06 15:44:55', 0);
INSERT INTO `practice_page` VALUES (310, 72, 243, NULL, '/unzip/2-5.jpg', '切面3', '切面4', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:52:55', '2024-06-06 15:52:55', 0);
INSERT INTO `practice_page` VALUES (311, 72, 244, NULL, '/unzip/2-6.jpg', '非标准切面', '切面5', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:52:57', '2024-06-06 15:52:57', 0);
INSERT INTO `practice_page` VALUES (312, 72, 245, NULL, '/unzip/2-7.jpg', '切面2', '切面4', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:52:58', '2024-06-06 15:52:58', 0);
INSERT INTO `practice_page` VALUES (313, 72, 246, NULL, '/unzip/2-8.jpg', '切面1', '切面4', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:01', '2024-06-06 15:53:01', 0);
INSERT INTO `practice_page` VALUES (314, 72, 247, NULL, '/unzip/2-9.jpg', '非标准切面', '切面2', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:04', '2024-06-06 15:53:04', 0);
INSERT INTO `practice_page` VALUES (315, 72, 248, NULL, '/unzip/2-10.jpg', '切面6', '切面3', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:07', '2024-06-06 15:53:07', 0);
INSERT INTO `practice_page` VALUES (316, 72, 249, NULL, '/unzip/2-11.jpg', '切面4', '切面4', 0, 1, 0, 'BANK', 'admin', '2024-06-06 15:53:09', '2024-06-06 15:53:09', 0);
INSERT INTO `practice_page` VALUES (317, 72, 250, NULL, '/unzip/2-12.jpg', '切面3', '切面2', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:10', '2024-06-06 15:53:10', 0);
INSERT INTO `practice_page` VALUES (318, 72, 251, NULL, '/unzip/2-13.jpg', '切面7', '切面2', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:13', '2024-06-06 15:53:13', 0);
INSERT INTO `practice_page` VALUES (319, 72, 252, NULL, '/unzip/2-15.jpg', '切面1', '切面2', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:17', '2024-06-06 15:53:17', 0);
INSERT INTO `practice_page` VALUES (320, 72, 253, NULL, '/unzip/2-14.jpg', '切面7', '切面4', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:18', '2024-06-06 15:53:18', 0);
INSERT INTO `practice_page` VALUES (321, 72, 254, NULL, '/unzip/2-16.jpg', '切面3', '切面2', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:20', '2024-06-06 15:53:20', 0);
INSERT INTO `practice_page` VALUES (322, 72, 255, NULL, '/unzip/2-17.jpg', '切面5', '切面2', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:22', '2024-06-06 15:53:22', 0);
INSERT INTO `practice_page` VALUES (323, 72, 256, NULL, '/unzip/2-18.jpg', '切面7', '切面2', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:24', '2024-06-06 15:53:24', 0);
INSERT INTO `practice_page` VALUES (324, 72, 257, NULL, '/unzip/2-19.jpg', '切面2', '切面1', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:27', '2024-06-06 15:53:27', 0);
INSERT INTO `practice_page` VALUES (325, 72, 258, NULL, '/unzip/2-20.jpg', '非标准切面', '切面2', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:30', '2024-06-06 15:53:30', 0);
INSERT INTO `practice_page` VALUES (326, 72, 259, NULL, '/unzip/2-21.jpg', '切面5', '切面4', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:32', '2024-06-06 15:53:32', 0);
INSERT INTO `practice_page` VALUES (327, 72, 260, NULL, '/unzip/2-22.jpg', '切面1', '切面3', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:34', '2024-06-06 15:53:34', 0);
INSERT INTO `practice_page` VALUES (328, 72, 261, NULL, '/unzip/2-1.jpg', '切面4', '切面7', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:36', '2024-06-06 15:53:36', 0);
INSERT INTO `practice_page` VALUES (329, 72, 262, NULL, '/unzip/2-2.jpg', '切面2', '切面4', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:38', '2024-06-06 15:53:38', 0);
INSERT INTO `practice_page` VALUES (330, 72, 263, NULL, '/unzip/2-3.jpg', '切面5', '切面4', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:42', '2024-06-06 15:53:42', 0);
INSERT INTO `practice_page` VALUES (331, 72, 264, NULL, '/unzip/2-4.jpg', '切面1', '切面2', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:44', '2024-06-06 15:53:44', 0);
INSERT INTO `practice_page` VALUES (332, 72, 265, NULL, '/unzip/2-5.jpg', '切面5', '切面3', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:46', '2024-06-06 15:53:46', 0);
INSERT INTO `practice_page` VALUES (333, 72, 266, NULL, '/unzip/2-6.jpg', '非标准切面', '切面4', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:48', '2024-06-06 15:53:48', 0);
INSERT INTO `practice_page` VALUES (334, 72, 267, NULL, '/unzip/2-7.jpg', '切面3', '切面2', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:51', '2024-06-06 15:53:51', 0);
INSERT INTO `practice_page` VALUES (335, 72, 268, NULL, '/unzip/2-8.jpg', '切面3', '切面5', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:55', '2024-06-06 15:53:55', 0);
INSERT INTO `practice_page` VALUES (336, 72, 269, NULL, '/unzip/2-9.jpg', '切面4', '切面3', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:53:57', '2024-06-06 15:53:57', 0);
INSERT INTO `practice_page` VALUES (337, 72, 270, NULL, '/unzip/2-10.jpg', '切面2', '切面2', 0, 1, 0, 'BANK', 'admin', '2024-06-06 15:53:59', '2024-06-06 15:53:59', 0);
INSERT INTO `practice_page` VALUES (338, 72, 271, NULL, '/unzip/2-11.jpg', '切面2', '切面4', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:54:01', '2024-06-06 15:54:01', 0);
INSERT INTO `practice_page` VALUES (339, 72, 272, NULL, '/unzip/2-12.jpg', '切面5', '切面5', 0, 1, 0, 'BANK', 'admin', '2024-06-06 15:54:03', '2024-06-06 15:54:03', 0);
INSERT INTO `practice_page` VALUES (340, 72, 273, NULL, '/unzip/2-13.jpg', '切面1', '切面2', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:54:07', '2024-06-06 15:54:07', 0);
INSERT INTO `practice_page` VALUES (341, 72, 274, NULL, '/unzip/2-15.jpg', '切面6', '非标准切面', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:54:09', '2024-06-06 15:54:09', 0);
INSERT INTO `practice_page` VALUES (342, 72, 275, NULL, '/unzip/2-14.jpg', '切面7', '切面3', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:54:11', '2024-06-06 15:54:11', 0);
INSERT INTO `practice_page` VALUES (343, 72, 276, NULL, '/unzip/2-16.jpg', '非标准切面', '切面3', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:54:13', '2024-06-06 15:54:13', 0);
INSERT INTO `practice_page` VALUES (344, 72, 277, NULL, '/unzip/2-17.jpg', '切面7', '切面5', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:54:14', '2024-06-06 15:54:14', 0);
INSERT INTO `practice_page` VALUES (345, 72, 278, NULL, '/unzip/2-18.jpg', '切面3', '切面7', 0, 0, 0, 'BANK', 'admin', '2024-06-06 15:54:16', '2024-06-06 15:54:16', 0);
INSERT INTO `practice_page` VALUES (346, 72, 280, NULL, '/unzip/2-19.jpg', '切面1', '切面2', 0, 0, 0, 'CASE', 'admin', '2024-06-06 15:54:20', '2024-06-06 15:54:20', 0);
INSERT INTO `practice_page` VALUES (347, 36, 237, NULL, '/unzip/2-20.jpg', '切面6', '切面2', 0, 0, 1, 'BANK', 'admin', '2024-06-12 14:12:44', '2024-06-12 14:12:44', 0);
INSERT INTO `practice_page` VALUES (348, 36, 238, NULL, '/unzip/2-21.jpg', '切面6', '切面2', 0, 0, 1, 'BANK', 'admin', '2024-06-12 14:12:47', '2024-06-12 14:12:47', 0);
INSERT INTO `practice_page` VALUES (349, 36, 239, NULL, '/unzip/2-22.jpg', '非标准切面', '非标准切面', 0, 1, 1, 'BANK', 'admin', '2024-06-12 14:26:23', '2024-06-12 14:26:23', 0);
INSERT INTO `practice_page` VALUES (351, 36, 236, NULL, '/unzip/2-1.jpg', '切面3', '切面6', 0, 0, 1, 'BANK', 'admin', '2024-06-17 15:31:26', '2024-06-17 15:31:26', 0);
INSERT INTO `practice_page` VALUES (352, 36, 240, NULL, '/unzip/2-2.jpg', '切面5', '切面4', 0, 0, 1, 'BANK', 'admin', '2024-06-17 15:35:36', '2024-06-17 15:35:36', 0);
INSERT INTO `practice_page` VALUES (353, 36, 241, NULL, '/unzip/2-3.jpg', '切面4', '切面4', 0, 1, 1, 'BANK', 'admin', '2024-06-17 15:38:02', '2024-06-17 15:38:02', 0);
INSERT INTO `practice_page` VALUES (354, 36, 242, NULL, '/unzip/2-4.jpg', '切面2', '切面4', 0, 0, 1, 'BANK', 'admin', '2024-06-17 15:38:06', '2024-06-17 15:38:06', 0);
INSERT INTO `practice_page` VALUES (355, 36, 336, 4, '/unzip/2-20.jpg', '切面3', '切面3', 0, 1, 0, 'BANK', 'admin', '2024-10-17 11:33:55', '2024-10-17 11:33:55', 0);
INSERT INTO `practice_page` VALUES (356, 36, 345, 4, '/unzip/2-7.jpg', '切面1', '切面7', 0, 0, 0, 'BANK', 'admin', '2024-10-17 11:33:56', '2024-10-17 11:33:56', 0);
INSERT INTO `practice_page` VALUES (357, 36, 297, 2, '/unzip/2-6.jpg', '切面5', '切面4', 0, 0, 0, 'BANK', 'admin', '2024-10-17 13:49:51', '2024-10-17 13:49:51', 0);
INSERT INTO `practice_page` VALUES (358, 36, 289, 1, '/unzip/2-1.jpg', '切面2', '切面2', 0, 1, 0, 'BANK', 'admin', '2025-04-05 08:47:05', '2025-04-05 08:47:05', 0);
INSERT INTO `practice_page` VALUES (359, 36, 290, 1, '/unzip/2-2.jpg', '切面3', '切面3', 0, 1, 0, 'BANK', 'admin', '2025-04-16 12:57:59', '2025-04-16 12:57:59', 0);
INSERT INTO `practice_page` VALUES (360, 36, 291, 1, '/unzip/2-3.jpg', '切面4', '切面2', 1, 0, 1, 'BANK', 'admin', '2025-04-16 12:58:16', '2025-04-16 12:58:16', 1);
INSERT INTO `practice_page` VALUES (361, 36, 292, 1, '/unzip/2-4.jpg', '切面5', '切面4', 0, 0, 1, 'BANK', 'admin', '2025-04-16 12:58:56', '2025-04-16 12:58:56', 1);
INSERT INTO `practice_page` VALUES (362, 36, 293, 1, '/unzip/2-5.jpg', '切面6', '切面6', 0, 1, 1, 'BANK', 'admin', '2025-04-16 13:41:07', '2025-04-16 13:41:07', 0);
INSERT INTO `practice_page` VALUES (363, 36, 304, 1, '/unzip/2-13.jpg', '切面7', '切面2', 0, 0, 1, 'CASE', 'admin', '2025-04-16 14:23:59', '2025-04-16 14:23:59', 1);
INSERT INTO `practice_page` VALUES (364, 36, 305, 1, '/unzip/2-15.jpg', '切面5', '切面3', 0, 0, 0, 'CASE', 'admin', '2025-04-16 14:24:01', '2025-04-16 14:24:01', 1);
INSERT INTO `practice_page` VALUES (365, 36, 306, 1, '/unzip/2-14.jpg', '切面7', '切面4', 0, 0, 1, 'CASE', 'admin', '2025-04-16 14:24:02', '2025-04-16 14:24:02', 1);
INSERT INTO `practice_page` VALUES (366, 36, 322, 1, '/unzip/2-6.jpg', '切面1', '切面3', 0, 0, 0, 'CASE', 'admin', '2025-04-16 14:24:04', '2025-04-16 14:24:04', 1);
INSERT INTO `practice_page` VALUES (367, 36, 324, 1, '/unzip/2-8.jpg', '切面5', '切面2', 0, 0, 1, 'BANK', 'admin', '2025-04-16 14:24:06', '2025-04-16 14:24:06', 1);
INSERT INTO `practice_page` VALUES (368, 36, 325, 1, '/unzip/2-9.jpg', '切面6', '切面6', 1, 1, 0, 'CASE', 'admin', '2025-04-16 14:24:07', '2025-04-16 14:24:07', 0);
INSERT INTO `practice_page` VALUES (369, 36, 331, 1, '/unzip/2-14.jpg', '切面6', '切面3', 0, 0, 0, 'CASE', 'admin', '2025-04-16 14:24:09', '2025-04-16 14:24:09', 0);
INSERT INTO `practice_page` VALUES (370, 36, 338, 1, '/unzip/2-22.jpg', '切面4', '切面2', 0, 0, 0, 'BANK', 'admin', '2025-04-16 14:24:11', '2025-04-16 14:24:11', 0);
INSERT INTO `practice_page` VALUES (371, 36, 344, 1, '/unzip/2-6.jpg', '切面1', '切面2', 0, 0, 0, 'CASE', 'admin', '2025-04-16 14:24:12', '2025-04-16 14:24:12', 0);
INSERT INTO `practice_page` VALUES (372, 36, 298, 2, '/unzip/2-7.jpg', '切面6', '', 0, NULL, 1, 'BANK', 'admin', '2025-04-16 14:26:24', '2025-04-16 14:26:24', 0);
INSERT INTO `practice_page` VALUES (373, 36, 307, 2, '/unzip/2-16.jpg', '切面2', '切面3', 0, 0, 0, 'BANK', 'admin', '2025-04-17 10:09:26', '2025-04-17 10:09:26', 0);
INSERT INTO `practice_page` VALUES (374, 36, 323, 5, '/unzip/2-7.jpg', '切面3', '切面6', 0, 0, 0, 'BANK', 'admin', '2025-04-17 10:09:42', '2025-04-17 10:09:42', 1);
INSERT INTO `practice_page` VALUES (375, 36, 332, 5, '/unzip/2-16.jpg', '切面1', '非标准切面', 0, 0, 0, 'CASE', 'admin', '2025-04-19 19:41:30', '2025-04-19 19:41:30', 0);
INSERT INTO `practice_page` VALUES (376, 36, 299, 3, '/unzip/2-8.jpg', '切面2', '切面2', 0, 1, 1, 'BANK', 'admin', '2025-04-19 19:41:48', '2025-04-19 19:41:48', 0);
INSERT INTO `practice_page` VALUES (377, 36, 300, 3, '/unzip/2-9.jpg', '切面3', '切面6', 0, 0, 1, 'BANK', 'admin', '2025-04-19 19:41:52', '2025-04-19 19:41:52', 1);
INSERT INTO `practice_page` VALUES (378, 36, 291, 1, '/unzip/2-3.jpg', '切面4', '切面1', 0, 0, 1, 'BANK', 'admin', '2025-04-24 12:09:47', '2025-04-24 12:09:47', 0);
INSERT INTO `practice_page` VALUES (379, 36, 292, 1, '/unzip/2-4.jpg', '切面5', '切面2', 0, 0, 1, 'BANK', 'admin', '2025-04-24 12:09:51', '2025-04-24 12:09:51', 0);
INSERT INTO `practice_page` VALUES (380, 36, 304, 1, '/unzip/2-13.jpg', '切面1', '切面6', 0, 0, 0, 'CASE', 'admin', '2025-04-24 12:09:54', '2025-04-24 12:09:54', 0);
INSERT INTO `practice_page` VALUES (381, 36, 305, 1, '/unzip/2-15.jpg', '切面5', '切面7', 0, 0, 0, 'CASE', 'admin', '2025-04-24 12:09:56', '2025-04-24 12:09:56', 0);
INSERT INTO `practice_page` VALUES (382, 36, 306, 1, '/unzip/2-14.jpg', '切面7', '切面6', 0, 0, 0, 'CASE', 'admin', '2025-04-24 12:09:59', '2025-04-24 12:09:59', 0);
INSERT INTO `practice_page` VALUES (383, 36, 322, 1, '/unzip/2-6.jpg', '切面1', '切面7', 0, 0, 0, 'CASE', 'admin', '2025-04-24 12:10:01', '2025-04-24 12:10:01', 0);
INSERT INTO `practice_page` VALUES (384, 36, 324, 1, '/unzip/2-8.jpg', '切面5', '切面6', 0, 0, 0, 'BANK', 'admin', '2025-04-24 12:10:03', '2025-04-24 12:10:03', 0);
INSERT INTO `practice_page` VALUES (385, 36, 352, 1, '\\img\\bef39e77.jpg', '非标准切面', '切面2', 0, 0, 0, 'BANK', 'admin', '2025-04-24 12:10:09', '2025-04-24 12:10:09', 0);
INSERT INTO `practice_page` VALUES (386, 36, 354, 1, '\\img\\59a0fde2.jpg', '切面1', '切面4', 1, 0, 0, 'BANK', 'admin', '2025-04-24 12:10:11', '2025-04-24 12:10:11', 0);
INSERT INTO `practice_page` VALUES (387, 36, 300, 3, '/unzip/2-9.jpg', '切面3', '切面5', 0, 0, 0, 'BANK', 'admin', '2025-05-15 14:32:04', '2025-05-15 14:32:04', 0);
INSERT INTO `practice_page` VALUES (388, 36, 301, 3, '/unzip/2-10.jpg', '非标准切面', '切面3', 0, 0, 0, 'BANK', 'admin', '2025-05-15 14:48:35', '2025-05-15 14:48:35', 0);
INSERT INTO `practice_page` VALUES (389, 36, 302, 3, '/unzip/2-11.jpg', '切面5', '切面7', 0, 0, 0, 'BANK', 'admin', '2025-05-15 14:48:59', '2025-05-15 14:48:59', 0);
INSERT INTO `practice_page` VALUES (390, 36, 303, 3, '/unzip/2-12.jpg', '切面6', '切面5', 0, 0, 0, 'BANK', 'admin', '2025-05-15 14:54:21', '2025-05-15 14:54:21', 0);
INSERT INTO `practice_page` VALUES (391, 36, 317, 3, '/unzip/2-1.jpg', '切面1', '切面4', 0, 0, 0, 'BANK', 'admin', '2025-05-15 14:55:57', '2025-05-15 14:55:57', 0);
INSERT INTO `practice_page` VALUES (392, 36, 318, 3, '/unzip/2-2.jpg', '切面2', '切面4', 0, 0, 0, 'BANK', 'admin', '2025-05-15 14:58:22', '2025-05-15 14:58:22', 0);
INSERT INTO `practice_page` VALUES (393, 36, 321, 3, '/unzip/2-5.jpg', '切面7', '切面3', 0, 0, 0, 'BANK', 'admin', '2025-05-15 14:59:37', '2025-05-15 14:59:37', 0);
INSERT INTO `practice_page` VALUES (394, 36, 329, 3, '/unzip/2-13.jpg', '非标准切面', '切面2', 0, 0, 0, 'BANK', 'admin', '2025-05-15 15:02:22', '2025-05-15 15:02:22', 0);
INSERT INTO `practice_page` VALUES (395, 36, 319, 3, '/unzip/2-3.jpg', '切面3', '切面2', 0, 0, 0, 'BANK', 'admin', '2025-05-15 15:05:45', '2025-05-15 15:05:45', 0);
INSERT INTO `practice_page` VALUES (396, 36, 320, 3, '/unzip/2-4.jpg', '非标准切面', '切面2', 0, 0, 0, 'BANK', 'admin', '2025-05-15 15:08:32', '2025-05-15 15:08:32', 0);
INSERT INTO `practice_page` VALUES (397, 36, 323, 5, '/unzip/2-7.jpg', '切面3', '切面3', 0, 1, 0, 'BANK', 'admin', '2025-05-17 12:29:17', '2025-05-17 12:29:17', 0);
INSERT INTO `practice_page` VALUES (398, 36, 327, 3, '/unzip/2-11.jpg', '切面2', '切面4', 0, 0, 0, 'BANK', 'admin', '2025-05-17 12:35:16', '2025-05-17 12:35:16', 0);
INSERT INTO `practice_page` VALUES (399, 95, 289, 1, '/unzip/2-1.jpg', '切面1', '切面3', 0, 0, 0, 'BANK', 'admin', '2025-05-21 10:14:26', '2025-05-21 10:14:26', 0);
INSERT INTO `practice_page` VALUES (400, 95, 292, 1, '/unzip/2-4.jpg', '切面5', '切面5', 0, 1, 1, 'BANK', 'admin', '2025-05-21 10:14:27', '2025-05-21 10:14:27', 0);
INSERT INTO `practice_page` VALUES (401, 95, 291, 1, '/unzip/2-3.jpg', '切面4', '', 0, NULL, 1, 'BANK', 'admin', '2025-05-21 10:14:31', '2025-05-21 10:14:31', 0);
INSERT INTO `practice_page` VALUES (402, 95, 303, 3, '/unzip/2-12.jpg', '切面6', '切面2', 0, 0, 1, 'BANK', 'admin', '2025-05-21 10:14:36', '2025-05-21 10:14:36', 0);
INSERT INTO `practice_page` VALUES (403, 95, 317, 3, '/unzip/2-1.jpg', '切面1', '切面7', 0, 0, 1, 'BANK', 'admin', '2025-05-21 10:14:38', '2025-05-21 10:14:38', 0);
INSERT INTO `practice_page` VALUES (404, 95, 301, 3, '/unzip/2-10.jpg', '非标准切面', '', 0, NULL, 0, 'BANK', 'admin', '2025-05-21 10:14:41', '2025-05-21 10:14:41', 0);
INSERT INTO `practice_page` VALUES (405, 95, 302, 3, '/unzip/2-11.jpg', '切面5', '', 0, NULL, 0, 'BANK', 'admin', '2025-05-21 10:14:43', '2025-05-21 10:14:43', 0);
INSERT INTO `practice_page` VALUES (406, 95, 368, 24, '\\unzip\\ba967a642-1.jpg', '切面1', '切面1', 0, 1, 0, 'BANK', 'admin', '2025-05-21 10:59:02', '2025-05-21 10:59:02', 0);

-- ----------------------------
-- Table structure for question_bank
-- ----------------------------
DROP TABLE IF EXISTS `question_bank`;
CREATE TABLE `question_bank`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `patient_analyse_id` int NULL DEFAULT NULL COMMENT '病例id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '题目名称',
  `answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '题目答案',
  `question_set_id` int NULL DEFAULT NULL COMMENT '题目对应套题id',
  `answered_user` int NULL DEFAULT NULL COMMENT '已答题人数',
  `correct_answered` int NULL DEFAULT NULL COMMENT '正确人数',
  `correct_rate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '正确率',
  `topic_source` enum('CASE','BANK') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '题目来源 bank文件和创建,case病例导入',
  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'admin' COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除',
  `score` int NOT NULL DEFAULT 1 COMMENT '题目分值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 381 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '题库表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question_bank
-- ----------------------------
INSERT INTO `question_bank` VALUES (289, NULL, '/unzip/2-1.jpg', '切面1', 1, 1, 0, '0.0%', 'BANK', 'admin', '2024-06-17 10:40:23', '2025-04-23 07:15:21', 0, 1);
INSERT INTO `question_bank` VALUES (290, NULL, '/unzip/2-2.jpg', '切面3', 1, 1, 1, '100.0%', 'BANK', 'admin', '2024-06-17 10:40:23', '2024-06-17 10:40:23', 0, 1);
INSERT INTO `question_bank` VALUES (291, NULL, '/unzip/2-3.jpg', '切面4', 1, 1, 0, '0.0%', 'BANK', 'admin', '2024-06-17 10:40:23', '2024-06-17 10:40:23', 0, 1);
INSERT INTO `question_bank` VALUES (292, NULL, '/unzip/2-4.jpg', '切面5', 1, 2, 1, '50.0%', 'BANK', 'admin', '2024-06-17 10:40:23', '2024-06-17 10:40:23', 0, 1);
INSERT INTO `question_bank` VALUES (293, NULL, '/unzip/2-5.jpg', '切面6', 1, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-17 10:40:23', '2024-06-17 10:40:23', 0, 1);
INSERT INTO `question_bank` VALUES (297, NULL, '/unzip/2-6.jpg', '切面5', 2, 1, 0, '0.0%', 'BANK', 'admin', '2024-06-17 10:40:49', '2024-06-17 10:40:49', 0, 1);
INSERT INTO `question_bank` VALUES (298, NULL, '/unzip/2-7.jpg', '切面6', 2, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-17 10:40:49', '2024-06-17 10:40:49', 0, 1);
INSERT INTO `question_bank` VALUES (299, NULL, '/unzip/2-8.jpg', '切面2', 3, 1, 1, '100.0%', 'BANK', 'admin', '2024-06-17 13:36:50', '2024-06-17 13:36:50', 0, 1);
INSERT INTO `question_bank` VALUES (300, NULL, '/unzip/2-9.jpg', '切面3', 3, 1, 0, '0.0%', 'BANK', 'admin', '2024-06-17 13:36:50', '2024-06-20 15:58:13', 0, 1);
INSERT INTO `question_bank` VALUES (301, NULL, '/unzip/2-10.jpg', '非标准切面', 3, 1, 0, '0.0%', 'BANK', 'admin', '2024-06-17 13:36:50', '2024-06-20 15:58:17', 0, 1);
INSERT INTO `question_bank` VALUES (302, NULL, '/unzip/2-11.jpg', '切面5', 3, 1, 0, '0.0%', 'BANK', 'admin', '2024-06-17 13:36:50', '2024-06-17 13:36:50', 0, 1);
INSERT INTO `question_bank` VALUES (303, NULL, '/unzip/2-12.jpg', '切面6', 3, 2, 0, '0.0%', 'BANK', 'admin', '2024-06-17 13:36:50', '2024-06-17 13:36:50', 0, 1);
INSERT INTO `question_bank` VALUES (304, 145, '/unzip/2-13.jpg', '切面1', 1, 1, 0, '0.0%', 'CASE', 'admin', '2024-06-19 09:47:26', '2025-04-23 07:15:13', 0, 1);
INSERT INTO `question_bank` VALUES (305, 146, '/unzip/2-15.jpg', '切面5', 1, 1, 0, '0.0%', 'CASE', 'admin', '2024-06-19 09:47:26', '2024-06-19 09:47:26', 0, 1);
INSERT INTO `question_bank` VALUES (306, 147, '/unzip/2-14.jpg', '切面7', 1, 1, 0, '0.0%', 'CASE', 'admin', '2024-06-19 09:47:26', '2024-06-19 09:47:26', 0, 1);
INSERT INTO `question_bank` VALUES (307, NULL, '/unzip/2-16.jpg', '切面2', 2, 1, 0, '0.0%', 'BANK', 'admin', '2024-06-19 09:49:41', '2024-06-19 09:49:41', 0, 1);
INSERT INTO `question_bank` VALUES (309, NULL, '/unzip/2-17.jpg', '切面4', 2, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-19 09:49:41', '2024-06-19 09:49:41', 0, 1);
INSERT INTO `question_bank` VALUES (312, NULL, '/unzip/2-18.jpg', '切面1', 2, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 14:51:57', '2024-06-20 14:51:57', 0, 1);
INSERT INTO `question_bank` VALUES (313, NULL, '/unzip/2-19.jpg', '切面2', 2, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 14:51:57', '2024-06-20 14:51:57', 0, 1);
INSERT INTO `question_bank` VALUES (314, NULL, '/unzip/2-20.jpg', '切面3', 2, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 14:51:57', '2024-06-20 14:51:57', 0, 1);
INSERT INTO `question_bank` VALUES (315, NULL, '/unzip/2-21.jpg', '非标准切面', 2, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 14:51:57', '2024-06-20 14:51:57', 0, 1);
INSERT INTO `question_bank` VALUES (316, NULL, '/unzip/2-22.jpg', '切面7', 2, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 14:51:57', '2024-06-20 14:51:57', 0, 1);
INSERT INTO `question_bank` VALUES (317, NULL, '/unzip/2-1.jpg', '切面1', 3, 2, 0, '0.0%', 'BANK', 'admin', '2024-06-20 14:53:47', '2024-06-20 14:53:47', 0, 1);
INSERT INTO `question_bank` VALUES (318, NULL, '/unzip/2-2.jpg', '切面2', 3, 1, 0, '0.0%', 'BANK', 'admin', '2024-06-20 14:53:47', '2024-06-20 14:53:47', 0, 1);
INSERT INTO `question_bank` VALUES (319, NULL, '/unzip/2-3.jpg', '切面3', 3, 1, 0, NULL, 'BANK', 'admin', '2024-06-20 14:53:47', '2024-06-20 14:53:47', 0, 1);
INSERT INTO `question_bank` VALUES (320, NULL, '/unzip/2-4.jpg', '非标准切面', 3, 1, 0, '0.0%', 'BANK', 'admin', '2024-06-20 14:53:47', '2024-06-20 14:53:47', 0, 1);
INSERT INTO `question_bank` VALUES (321, NULL, '/unzip/2-5.jpg', '切面7', 3, 1, 0, '0.0', 'BANK', 'admin', '2024-06-20 14:53:47', '2024-06-20 14:53:47', 0, 1);
INSERT INTO `question_bank` VALUES (322, 188, '/unzip/2-6.jpg', '切面1', 1, 1, 0, '0.0%', 'CASE', 'admin', '2024-06-20 14:55:31', '2024-06-20 14:55:31', 0, 1);
INSERT INTO `question_bank` VALUES (323, NULL, '/unzip/2-7.jpg', '切面3', 5, 1, 1, '100.0%', 'BANK', 'admin', '2024-06-20 15:09:59', '2024-06-20 15:09:59', 0, 1);
INSERT INTO `question_bank` VALUES (324, NULL, '/unzip/2-8.jpg', '切面5', 1, 1, 0, '0.0%', 'BANK', 'admin', '2024-06-20 16:42:36', '2024-06-20 16:42:36', 0, 1);
INSERT INTO `question_bank` VALUES (325, 198, '/unzip/2-9.jpg', '切面6', 1, 1, 1, '100.0%', 'CASE', 'admin', '2024-06-20 16:43:05', '2024-06-20 16:43:05', 0, 1);
INSERT INTO `question_bank` VALUES (326, NULL, '/unzip/2-10.jpg', '切面1', 3, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 16:45:18', '2024-06-20 16:45:18', 0, 1);
INSERT INTO `question_bank` VALUES (327, NULL, '/unzip/2-11.jpg', '切面2', 3, 1, 0, '0.0%', 'BANK', 'admin', '2024-06-20 16:45:18', '2024-06-20 16:45:18', 0, 1);
INSERT INTO `question_bank` VALUES (328, NULL, '/unzip/2-12.jpg', '切面3', 3, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 16:45:18', '2024-06-20 16:45:18', 0, 1);
INSERT INTO `question_bank` VALUES (329, NULL, '/unzip/2-13.jpg', '非标准切面', 3, 1, 0, NULL, 'BANK', 'admin', '2024-06-20 16:45:18', '2024-06-20 16:45:18', 0, 1);
INSERT INTO `question_bank` VALUES (330, NULL, '/unzip/2-15.jpg', '切面7', 3, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 16:45:18', '2024-06-20 16:45:18', 0, 1);
INSERT INTO `question_bank` VALUES (331, 173, '/unzip/2-14.jpg', '切面6', 1, 1, 0, '0.0%', 'CASE', 'admin', '2024-06-20 16:47:46', '2024-06-20 16:47:46', 0, 1);
INSERT INTO `question_bank` VALUES (332, 158, '/unzip/2-16.jpg', '切面1', 5, 1, 0, '0.0%', 'CASE', 'admin', '2024-06-20 16:49:25', '2024-06-20 16:49:25', 0, 1);
INSERT INTO `question_bank` VALUES (333, 159, '/unzip/2-17.jpg', '切面6', 2, NULL, NULL, NULL, 'CASE', 'admin', '2024-06-20 16:50:26', '2024-06-20 16:50:26', 0, 1);
INSERT INTO `question_bank` VALUES (334, NULL, '/unzip/2-18.jpg', '切面3', 3, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 16:55:37', '2024-06-20 16:55:37', 0, 1);
INSERT INTO `question_bank` VALUES (335, NULL, '/unzip/2-19.jpg', '切面5', 3, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 16:56:27', '2024-06-20 16:56:27', 0, 1);
INSERT INTO `question_bank` VALUES (336, NULL, '/unzip/2-20.jpg', '切面3', 4, 1, 1, '100.0%', 'BANK', 'admin', '2024-06-20 16:56:56', '2024-06-20 16:56:56', 0, 1);
INSERT INTO `question_bank` VALUES (337, NULL, '/unzip/2-21.jpg', '切面4', 3, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 16:59:13', '2024-06-20 16:59:13', 0, 1);
INSERT INTO `question_bank` VALUES (338, NULL, '/unzip/2-22.jpg', '切面4', 1, 1, 0, '0.0%', 'BANK', 'admin', '2024-06-20 17:00:28', '2024-06-20 17:00:28', 0, 1);
INSERT INTO `question_bank` VALUES (339, NULL, '/unzip/2-1.jpg', '切面1', 3, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 17:01:02', '2024-06-20 17:01:02', 0, 1);
INSERT INTO `question_bank` VALUES (340, NULL, '/unzip/2-2.jpg', '切面2', 3, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 17:01:02', '2024-06-20 17:01:02', 0, 1);
INSERT INTO `question_bank` VALUES (341, NULL, '/unzip/2-3.jpg', '切面3', 3, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 17:01:02', '2024-06-20 17:01:02', 0, 1);
INSERT INTO `question_bank` VALUES (342, NULL, '/unzip/2-4.jpg', '非标准切面', 3, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 17:01:02', '2024-06-20 17:01:02', 0, 1);
INSERT INTO `question_bank` VALUES (343, NULL, '/unzip/2-5.jpg', '切面7', 3, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 17:01:02', '2024-06-20 17:01:02', 0, 1);
INSERT INTO `question_bank` VALUES (344, 160, '/unzip/2-6.jpg', '切面1', 1, 1, 0, '0.0%', 'CASE', 'admin', '2024-06-20 17:01:27', '2024-06-20 17:01:27', 0, 1);
INSERT INTO `question_bank` VALUES (345, NULL, '/unzip/2-7.jpg', '切面1', 4, 1, 0, '0.0%', 'BANK', 'admin', '2024-06-20 17:15:20', '2024-06-20 17:15:20', 0, 1);
INSERT INTO `question_bank` VALUES (346, NULL, '/unzip/2-8.jpg', '切面2', 4, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 17:15:20', '2024-06-20 17:15:20', 0, 1);
INSERT INTO `question_bank` VALUES (347, NULL, '/unzip/2-9.jpg', '切面3', 4, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 17:15:20', '2024-06-20 17:15:20', 0, 1);
INSERT INTO `question_bank` VALUES (348, NULL, '/unzip/2-10.jpg', '非标准切面', 4, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 17:15:20', '2024-06-20 17:15:20', 0, 1);
INSERT INTO `question_bank` VALUES (349, NULL, '/unzip/2-11.jpg', '切面7', 4, NULL, NULL, NULL, 'BANK', 'admin', '2024-06-20 17:15:20', '2024-06-20 17:15:20', 0, 1);
INSERT INTO `question_bank` VALUES (350, 158, '/unzip/2-9.jpg', '切面1', 10, NULL, NULL, NULL, 'CASE', 'admin', '2025-04-05 09:01:35', '2025-04-05 09:01:35', 0, 1);
INSERT INTO `question_bank` VALUES (351, 158, '/unzip/2-9.jpg', '切面1', 10, NULL, NULL, NULL, 'CASE', 'admin', '2025-04-05 09:01:39', '2025-04-05 09:01:39', 0, 1);
INSERT INTO `question_bank` VALUES (352, NULL, '\\img\\bef39e77.jpg', '非标准切面', 1, 1, 0, '0.0%', 'BANK', 'admin', '2025-04-22 23:48:32', '2025-04-22 23:48:32', 0, 1);
INSERT INTO `question_bank` VALUES (353, NULL, '\\img\\ab48c67e.jpg', '切面1', 1, NULL, NULL, NULL, 'BANK', 'admin', '2025-04-22 23:48:40', '2025-04-22 23:48:40', 1, 1);
INSERT INTO `question_bank` VALUES (354, NULL, '\\img\\59a0fde2.jpg', '切面1', 1, 1, 0, '0.0%', 'BANK', 'admin', '2025-04-22 23:48:45', '2025-04-22 23:48:45', 0, 1);
INSERT INTO `question_bank` VALUES (355, NULL, '\\img\\792cbfea.jpg', '切面1', 2, NULL, NULL, NULL, 'BANK', 'admin', '2025-04-22 23:48:57', '2025-04-22 23:48:57', 0, 1);
INSERT INTO `question_bank` VALUES (356, NULL, '\\img\\813df7b7.jpg', '切面6', 7, NULL, NULL, NULL, 'BANK', 'admin', '2025-04-22 23:49:08', '2025-04-22 23:49:08', 0, 1);
INSERT INTO `question_bank` VALUES (357, 160, '/unzip/2-11.jpg', '切面1', 4, NULL, NULL, NULL, 'CASE', 'admin', '2025-04-24 11:22:54', '2025-04-24 11:22:54', 0, 1);
INSERT INTO `question_bank` VALUES (358, 160, '/unzip/2-11.jpg', '切面1', 4, NULL, NULL, NULL, 'CASE', 'admin', '2025-04-24 11:22:58', '2025-04-24 11:22:58', 0, 1);
INSERT INTO `question_bank` VALUES (359, 160, '/unzip/2-11.jpg', '切面1', 20, NULL, NULL, NULL, 'CASE', 'admin', '2025-04-24 11:23:07', '2025-04-24 11:23:07', 0, 1);
INSERT INTO `question_bank` VALUES (360, NULL, '\\img\\d4fbb3cf.jpg', '切面6', 20, NULL, NULL, NULL, 'BANK', 'admin', '2025-04-24 11:25:32', '2025-04-24 11:25:32', 0, 1);
INSERT INTO `question_bank` VALUES (361, NULL, '\\img\\9f17b881.jpg', '切面1', 1, NULL, NULL, NULL, 'BANK', 'admin', '2025-05-17 12:47:59', '2025-05-17 12:48:11', 0, 1);
INSERT INTO `question_bank` VALUES (362, 173, '/img/2-1.jpg', '切面6', 1, NULL, NULL, NULL, 'CASE', 'admin', '2025-05-17 12:48:25', '2025-05-17 12:48:25', 0, 1);
INSERT INTO `question_bank` VALUES (363, NULL, '\\unzip\\8919ec902-1.jpg', '切面1', 1, NULL, NULL, NULL, 'BANK', 'admin', '2025-05-17 12:48:54', '2025-05-17 12:48:54', 0, 1);
INSERT INTO `question_bank` VALUES (364, NULL, '\\unzip\\054ddaea2-2.jpg', '切面2', 1, NULL, NULL, NULL, 'BANK', 'admin', '2025-05-17 12:48:54', '2025-05-17 12:48:54', 0, 1);
INSERT INTO `question_bank` VALUES (365, NULL, '\\unzip\\882f2bdb2-3.jpg', '切面3', 1, NULL, NULL, NULL, 'BANK', 'admin', '2025-05-17 12:48:54', '2025-05-17 12:48:54', 0, 1);
INSERT INTO `question_bank` VALUES (366, NULL, '\\unzip\\d3fc8e652-4.jpg', '非标准切面', 1, NULL, NULL, NULL, 'BANK', 'admin', '2025-05-17 12:48:54', '2025-05-17 12:48:54', 0, 1);
INSERT INTO `question_bank` VALUES (367, NULL, '\\unzip\\b810530e2-5.jpg', '切面7', 1, NULL, NULL, NULL, 'BANK', 'admin', '2025-05-17 12:48:54', '2025-05-17 12:48:54', 0, 1);
INSERT INTO `question_bank` VALUES (368, NULL, '\\unzip\\ba967a642-1.jpg', '切面1', 24, 1, 1, '100.0%', 'BANK', 'admin', '2025-05-21 10:53:39', '2025-05-21 10:53:39', 0, 1);
INSERT INTO `question_bank` VALUES (369, NULL, '\\unzip\\568714652-2.jpg', '切面2', 24, NULL, NULL, NULL, 'BANK', 'admin', '2025-05-21 10:53:39', '2025-05-21 10:53:39', 0, 1);
INSERT INTO `question_bank` VALUES (370, NULL, '\\unzip\\78fadca92-3.jpg', '切面3', 24, NULL, NULL, NULL, 'BANK', 'admin', '2025-05-21 10:53:39', '2025-05-21 10:53:39', 0, 1);
INSERT INTO `question_bank` VALUES (371, NULL, '\\unzip\\d56691512-4.jpg', '非标准切面', 24, NULL, NULL, NULL, 'BANK', 'admin', '2025-05-21 10:53:39', '2025-05-21 10:53:39', 0, 1);
INSERT INTO `question_bank` VALUES (372, NULL, '\\unzip\\9002b5222-5.jpg', '切面7', 24, NULL, NULL, NULL, 'BANK', 'admin', '2025-05-21 10:53:39', '2025-05-21 10:53:39', 0, 1);
INSERT INTO `question_bank` VALUES (373, 173, '/img/2-1.jpg', '切面6', 23, NULL, NULL, NULL, 'CASE', 'admin', '2025-05-21 10:53:48', '2025-05-21 10:53:48', 0, 1);
INSERT INTO `question_bank` VALUES (374, 174, '/img/2-2.jpg', '切面6', 23, NULL, NULL, NULL, 'CASE', 'admin', '2025-05-21 10:53:48', '2025-05-21 10:53:48', 0, 1);
INSERT INTO `question_bank` VALUES (375, NULL, '\\img\\ef76200a.jpg', '切面4', 21, NULL, NULL, NULL, 'BANK', 'admin', '2025-05-21 10:54:00', '2025-05-21 10:54:00', 0, 1);
INSERT INTO `question_bank` VALUES (376, NULL, '\\unzip\\e00e39a02-1.jpg', '切面1', 4, NULL, NULL, NULL, 'BANK', 'admin', '2025-05-22 09:42:25', '2025-05-22 09:42:25', 0, 1);
INSERT INTO `question_bank` VALUES (377, NULL, '\\unzip\\b23360e62-2.jpg', '切面2', 4, NULL, NULL, NULL, 'BANK', 'admin', '2025-05-22 09:42:25', '2025-05-22 09:42:25', 0, 1);
INSERT INTO `question_bank` VALUES (378, NULL, '\\unzip\\812b67382-3.jpg', '切面3', 4, NULL, NULL, NULL, 'BANK', 'admin', '2025-05-22 09:42:25', '2025-05-22 09:42:25', 0, 1);
INSERT INTO `question_bank` VALUES (379, NULL, '\\unzip\\3b9168f02-4.jpg', '非标准切面', 4, NULL, NULL, NULL, 'BANK', 'admin', '2025-05-22 09:42:25', '2025-05-22 09:42:25', 0, 1);
INSERT INTO `question_bank` VALUES (380, NULL, '\\unzip\\511bc7732-5.jpg', '切面7', 4, NULL, NULL, NULL, 'BANK', 'admin', '2025-05-22 09:42:25', '2025-05-22 09:42:25', 0, 1);

-- ----------------------------
-- Table structure for question_set
-- ----------------------------
DROP TABLE IF EXISTS `question_set`;
CREATE TABLE `question_set`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '套题id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '套题名称',
  `correct_rate` decimal(10, 0) NULL DEFAULT NULL COMMENT '套题正确率',
  `correct_answer` int NULL DEFAULT NULL COMMENT '套题正确人数',
  `answered_user` int NULL DEFAULT NULL COMMENT '套题回答人数',
  `question_sum` int NULL DEFAULT NULL COMMENT '套题中题目总数',
  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '套题创建时间',
  `edit_time` datetime NULL DEFAULT NULL COMMENT '套题修改时间',
  `deleted` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除',
  `exam_duration` int NULL DEFAULT NULL COMMENT '测试时长',
  `exam_device` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '测试终端',
  `complete_rate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '完成比率',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question_set
-- ----------------------------
INSERT INTO `question_set` VALUES (1, '套题2', 11, 3, 16, 23, NULL, '2024-06-17 10:40:23', '2025-04-23 10:23:47', 0, 30, 'compssuter', '0.84%');
INSERT INTO `question_set` VALUES (2, '模拟练习-2', 0, 0, 0, 11, NULL, '2024-06-17 10:40:49', '2024-06-20 09:23:54', 0, 30, 'computer', NULL);
INSERT INTO `question_set` VALUES (3, '病例练习-1', 4, 1, 14, 23, NULL, '2024-06-17 13:36:49', '2024-06-20 09:23:41', 0, 60, 'computer', '0.74%');
INSERT INTO `question_set` VALUES (4, '套题3', 8, 1, 2, 13, NULL, '2024-06-19 09:49:41', '2024-06-20 09:23:25', 0, 60, 'computer', '0.09%');
INSERT INTO `question_set` VALUES (5, 'HSK模拟练习', 0, 0, 0, 2, NULL, '2024-06-20 09:20:22', '2024-06-20 09:23:16', 0, 30, 'computer', NULL);
INSERT INTO `question_set` VALUES (6, '21级六级备战模拟1', 0, NULL, NULL, 0, NULL, '2024-06-20 09:27:52', '2024-06-20 11:22:56', 0, 40, 'computer', NULL);
INSERT INTO `question_set` VALUES (7, '21级四级备战模拟1', 0, NULL, NULL, 0, NULL, '2024-06-20 09:28:01', '2024-06-20 15:09:47', 0, 20, 'computer', NULL);
INSERT INTO `question_set` VALUES (8, '21级六级备战模拟2', 0, NULL, NULL, 0, NULL, '2024-06-20 09:29:57', '2024-06-20 09:29:57', 0, 15, 'computer', NULL);
INSERT INTO `question_set` VALUES (9, '英语专八备战模拟1', NULL, NULL, NULL, 0, NULL, '2024-06-20 14:51:56', '2024-06-20 14:51:56', 0, 15, 'computer', NULL);
INSERT INTO `question_set` VALUES (10, '英语专四备战模拟1', NULL, NULL, NULL, 0, NULL, '2024-06-20 14:53:47', '2024-06-20 14:53:47', 0, 15, 'computer', NULL);
INSERT INTO `question_set` VALUES (11, '小测试', NULL, NULL, NULL, 0, NULL, '2024-06-20 15:01:40', '2024-06-20 15:01:40', 0, 15, 'computer', NULL);
INSERT INTO `question_set` VALUES (12, '小测试2', NULL, NULL, NULL, 0, NULL, '2024-06-20 15:04:01', '2024-06-20 15:04:01', 0, 15, 'computer', NULL);
INSERT INTO `question_set` VALUES (13, '123', NULL, NULL, NULL, 0, NULL, '2024-06-20 15:04:04', '2024-06-20 15:04:04', 0, 15, 'computer', NULL);
INSERT INTO `question_set` VALUES (18, '雅思', NULL, NULL, NULL, 0, NULL, '2025-04-22 23:30:04', '2025-04-22 23:30:04', 1, NULL, 'computer', NULL);
INSERT INTO `question_set` VALUES (19, '雅思', NULL, NULL, NULL, 0, NULL, '2025-04-22 23:30:07', '2025-04-23 10:26:15', 0, NULL, 'computer', NULL);
INSERT INTO `question_set` VALUES (20, '11111', NULL, NULL, NULL, 0, NULL, '2025-04-23 07:19:34', '2025-04-23 07:19:34', 0, NULL, 'computer', NULL);
INSERT INTO `question_set` VALUES (21, '222', 0, 0, 0, 1, NULL, '2025-05-15 14:17:03', '2025-05-15 14:17:03', 0, NULL, 'computer', '0.0%');
INSERT INTO `question_set` VALUES (22, '123123', NULL, NULL, NULL, 0, NULL, '2025-05-17 12:49:10', '2025-05-17 12:49:10', 1, NULL, 'computer', NULL);
INSERT INTO `question_set` VALUES (23, '0000', 0, 0, 0, 2, NULL, '2025-05-17 13:48:22', '2025-05-17 13:48:22', 0, 30, 'computer', '0.0%');
INSERT INTO `question_set` VALUES (24, '超声诊断', 20, 1, 1, 5, NULL, '2025-05-17 13:52:03', '2025-05-21 11:00:15', 0, 30, 'computer', '0.05%');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户性别',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户密码',
  `school` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属学校',
  `institute` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属学院',
  `grade` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '年级',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `num_upload_cases` int NULL DEFAULT NULL COMMENT '上传病例数',
  `correct_rate_case` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '病例切面正确率',
  `correct_rate_question_bank` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '题库正确率',
  `question_bank_complete_rate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '题库完成率',
  `last_login` datetime NULL DEFAULT NULL COMMENT '上次登陆时间',
  `edit_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '清空 0未删除 1删除',
  `role` enum('ADMIN','USER','SUPERADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色身份 admin管理员 user用户',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '真实姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (19, '学员2', 'MALE', '$2a$10$dJW06/20NqvhYWUlBKHj4uVvaNW7QBC/dSO4qqGNNct/CL.xzjhX6', '浙江大学', '信息与电子工程学院', '21级本', NULL, 4, '75.0%', '0.0%', '0.0%(0/78)', '2024-06-04 15:29:06', '2024-05-28 11:29:55', 1, 'USER', 1, '2024-05-15 15:25:06', '潘苏苏');
INSERT INTO `user` VALUES (34, 'admin', 'FEMALE', '$2a$10$ht/F2D1AN81Nb.wDWOHr7uVcElJGIVGEYtOdcwG91PCCiMeyOxUR.', '浙江大学', NULL, '', NULL, 0, '0', NULL, '0.0%(0/85)', '2025-05-21 09:54:47', '2024-05-28 10:48:21', 0, 'ADMIN', 0, '2024-05-28 10:48:21', '苏慧伦');
INSERT INTO `user` VALUES (35, '学员1', 'MALE', '$2a$10$8l6i57Vh/M6n0glYkRZgJeBmovRkvkrmSXkwGnDxsmhzgIN2fTA5m', '浙江大学', '信息与电子工程学院', '22级本', NULL, 1, '50.0%', '33.0%', '4.0%(3/85)', '2024-06-07 10:03:31', '2024-05-28 11:06:57', 0, 'USER', 0, '2024-05-28 11:06:57', '苏慧伦');
INSERT INTO `user` VALUES (36, '2024', 'MALE', '$2a$10$Fx7evUHtc0D9cFAMtWnXh./vnaaK76lNc/Yj97su4KwMODOnMmKUW', '浙江科技大学', '机电工程与自动化学院', '23级本', NULL, 16, '26.06%', '22.0%', '54.0%(46/85)', '2025-05-23 10:51:16', '2024-05-28 12:42:57', 0, 'USER', 0, '2024-05-28 12:42:57', '张炜旭');
INSERT INTO `user` VALUES (68, 'Wang Wei', 'MALE', '$2a$10$3jnC07VWPldoNDGEFlIjSOkPxn.FSjma3lFHLsjA02AIfJJNoxaKu', '浙江理工大学', NULL, NULL, NULL, NULL, NULL, NULL, '0.0%(0/85)', '2025-05-23 10:51:19', '2024-06-06 10:09:14', 0, 'ADMIN', 0, '2024-06-06 10:09:14', '高振翔');
INSERT INTO `user` VALUES (70, 'superadmin', 'MALE', '$2a$10$XAUNvMsTQzdFZDID8bpZG.gM.wG0YO/vXGGLnMUJX9Q4270YC4SW6', '浙江工商大学', NULL, NULL, NULL, NULL, NULL, NULL, '0.0%(0/85)', '2025-05-21 21:47:00', '2024-06-06 10:19:07', 0, 'SUPERADMIN', 0, '2024-06-06 10:19:07', '唐小惠');
INSERT INTO `user` VALUES (71, 'Rongshu', 'FEMALE', '$2a$10$E6uyUyrcOxx2B3NkJNt6ZOdkhXWe.VN1L0E8MmnLFM9tW7eb6Rkgu', '浙江工业大学', '理学院', '24级本', NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2024-06-06 10:53:02', 0, 'ADMIN', 0, '2024-06-06 10:53:02', '宋希');
INSERT INTO `user` VALUES (72, '2023', 'FEMALE', '$2a$10$LwCXAMI9uiDgeLDLMGJsHuKsrniwu9neOuMF0ChNWSFeCvcBPK5Za', '浙江外国语学院', '机电工程与自动化学院', '20级本', NULL, 3, '100.0%', '17.0%', '56.0%(48/85)', '2025-05-17 15:57:54', '2024-06-06 10:53:13', 0, 'USER', 0, '2024-06-06 10:53:13', '潘金金');
INSERT INTO `user` VALUES (73, '2022', 'FEMALE', '$2a$10$5EHzGfSlWt4lOT2fPu3UsuL9lqxyqjEUX86fgekIBYLrH2KT8vnjK', '浙江科技大学', '材料科学与工程学院', '19级研', NULL, 0, '0.0%', NULL, '0.0%(0/59)', '2024-06-07 17:13:04', '2024-06-07 14:42:37', 1, 'USER', 1, '2024-06-07 14:42:37', '叶晓晓');
INSERT INTO `user` VALUES (74, '2022', 'MALE', '$2a$10$.vD1mG6T.3DMtX/5pcJp2uMrpn0N.e7EFWrHXSKCvfR5bldd1CHfW', '浙江外国语学院', '经济管理学院', '22级研', NULL, NULL, NULL, NULL, NULL, NULL, '2024-06-17 10:50:21', 1, 'USER', 0, '2024-06-17 10:50:21', '杨力行');
INSERT INTO `user` VALUES (75, '2021', 'MALE', '$2a$10$wEu4TEN58kaj1q1WR7frJOBPsdR5r8j/8Y39/HdlY6FHyLQyjFsCW', '浙江大学', '材料科学与工程学院', '20级本', NULL, NULL, NULL, NULL, NULL, NULL, '2024-06-17 10:50:29', 1, 'USER', 0, '2024-06-17 10:50:29', '潘凯仑');
INSERT INTO `user` VALUES (76, '2022', 'MALE', '$2a$10$dthr4LYr/zCES7cRL3fhauT9B63NheBGed/t9F1eEZVzQnliHIPBW', '浙江师范大学', '建筑学院', '23级研', NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2024-06-17 11:02:27', 0, 'USER', 1, '2024-06-17 11:02:27', '陈维莱');
INSERT INTO `user` VALUES (77, '2021', 'MALE', '$2a$10$yGpub3K1SLPZ41Hcg8pGNODy/RMz8QlLhw9ErSH0b/osxnCQOO7N2', '浙江师范大学', '经济管理学院', '22级本', NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2024-06-19 09:33:53', 0, 'USER', 0, '2024-06-19 09:33:53', '甲亢哥');
INSERT INTO `user` VALUES (78, '2010', 'MALE', '$2a$10$AkQAdhuGvrMOhgzE0pIZ3eSLgPSgiMN2ChbhzU6w0nUi3lV4pI8Um', '杭州师范大学', '经济管理学院', '23级本', NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2024-06-19 09:34:04', 0, 'USER', 0, '2024-06-19 09:34:04', '陈苏联');
INSERT INTO `user` VALUES (79, '2020', 'MALE', '$2a$10$fwj34vutQtJ/OsdtTdGEj.NSb8p09g3QXB0Gas66n1IvIbuSsDtUi', '华中师范大学', '仪器科学与工程学院', '21级本', NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2024-06-19 09:34:13', 0, 'USER', 0, '2024-06-19 09:34:13', '张晶晶');
INSERT INTO `user` VALUES (80, '2019', 'MALE', '$2a$10$DkXOb//z5sPNd3O9FjAe3upqy9vazWyHS2UvtOJAEbCKyai.EFW2K', '上海大学', '物理学院', '24级本', NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2024-06-19 09:34:20', 0, 'USER', 0, '2024-06-19 09:34:20', '张三');
INSERT INTO `user` VALUES (81, '2018', 'MALE', '$2a$10$C4bDDVtPjIBof.CD5m8i0u2qBaJNOd/CT/Mt9nc5k83mzjAqEDJji', '上海交通大学', '生物与化学工程学院', '22级研', NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2024-06-19 09:34:34', 0, 'USER', 0, '2024-06-19 09:34:34', '李四');
INSERT INTO `user` VALUES (82, '2017', 'MALE', '$2a$10$0waXNl3th17brheeS4NvC.o6nbODxcboEpmyumB/x6bEjB9rl26la', '上海复旦大学', '艺术学院', '24级研', NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2024-06-19 09:34:39', 0, 'USER', 0, '2024-06-19 09:34:39', '王五');
INSERT INTO `user` VALUES (83, '2016', 'MALE', '$2a$10$q/2VLNX.1SI.xqxVFchgIesQtRN0QZY6FLwMS/jlJ0yQBlZat8An6', '上海大学', '文学院', '24级研', NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2024-06-19 09:34:44', 0, 'USER', 0, '2024-06-19 09:34:44', '李六');
INSERT INTO `user` VALUES (84, '2015', 'MALE', '$2a$10$BOQ6wZPrjTR4DOd8dmRUreS1qZFybZAXCxIbHk1aaEaTEheDGngEy', '浙江工业大学', '理学院', '21级研', NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2024-06-19 09:34:49', 0, 'USER', 1, '2024-06-19 09:34:49', '李嘉诚');
INSERT INTO `user` VALUES (85, '20122', 'MALE', '$2a$10$DsbTcEJEPZXShXqbtdfEJuNaUKryqiDfRTpiYLM74Us6pU94sdbkC', '浙江理工大学', '文学院', '22级本', NULL, NULL, NULL, NULL, NULL, NULL, '2024-06-20 15:59:40', 1, 'ADMIN', 1, '2024-06-20 15:59:40', '苏轼');
INSERT INTO `user` VALUES (86, '2025', 'FEMALE', '$2a$10$4GN4AQ41PwG4nYpFJQKqr.gW5JboVMYn6yppUpWSJNsa6aSktjPo.', '浙江科技大学', '环境学院', '22级本', NULL, NULL, NULL, NULL, '0.0%(0/85)', '2025-04-03 13:08:57', '2024-10-17 11:33:28', 0, 'USER', 0, '2024-10-17 11:33:28', '陈静怡');
INSERT INTO `user` VALUES (87, 'Jingwei X', 'FEMALE', '$2a$10$ykf77Ye9fqbYJpJKmqi2XOZN7LLTfO/USbBRMwl/Fpf/jS6zYk2Xi', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2025-04-23 06:11:25', 0, 'USER', 0, '2025-04-23 06:11:25', '');
INSERT INTO `user` VALUES (88, 'Song gaogoa', 'FEMALE', '$2a$10$Mgy0PRJaIJ5ghRBXedN9be.6XjAPYonH5bxJtDEGmSSDcTPtaod62', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2025-04-23 06:14:25', 0, 'USER', 0, '2025-04-23 06:14:25', '');
INSERT INTO `user` VALUES (89, 'Jin Xiankai', 'FEMALE', '$2a$10$6El6U9Fl3F/4p9Zw3rKp8.we9HqDrSgNp8dawfVpoxsxi.hjYsTrS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2025-04-23 06:23:43', 0, 'USER', 0, '2025-04-23 06:23:43', '');
INSERT INTO `user` VALUES (90, 'Jin Jiujiu', 'MALE', '$2a$10$ummZew26Oy1dodiXe6.lHuzmbzQhxHrlPNuIqKW6VLSsgO8HY0GV6', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2025-04-23 06:30:15', 0, 'USER', 0, '2025-04-23 06:30:15', '金九九');
INSERT INTO `user` VALUES (91, '123', 'FEMALE', '$2a$10$JrQsr5EgVDUk941n51n41ejkl8ENHbyQxhwoSXAqCqbo115yNngTq', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2025-04-27 20:51:35', 0, 'USER', 0, '2025-04-27 20:51:35', '');
INSERT INTO `user` VALUES (94, 'supadmin', 'MALE', '$2a$10$2cWgCXHaPAs5YnDUeb7wGeA2dAt6eY6HrrWIeUnFA0XCESPebJ.oK', '浙江工业大学', '', '22级硕', NULL, NULL, NULL, NULL, '0.0%(0/85)', '2025-04-27 21:19:49', '2025-04-27 21:01:01', 0, 'SUPERADMIN', 0, '2025-04-27 21:01:01', '管理员');
INSERT INTO `user` VALUES (95, '520', 'MALE', '$2a$10$GV0I38oqCDVP4porVtoJWOl.QdzMUnutgj5i/KXj7yaIMCBMpqYpm', '浙江理工大学', '', '', NULL, 5, '40.0%', '40.0%', '9.0%(8/85)', '2025-05-21 20:57:18', '2025-05-21 10:11:00', 0, 'USER', 0, '2025-05-21 10:11:00', '紧缠缠');
INSERT INTO `user` VALUES (96, '20000', 'FEMALE', '$2a$10$TT3HsPG9spQFSOhbDB9lKeYa38C0jl3fjEvJQhpgOmg/0fhFndzf2', '浙江师范大学', '', '22级硕', NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2025-05-21 10:33:56', 0, 'USER', 0, '2025-05-21 10:33:56', '亮晶晶');
INSERT INTO `user` VALUES (97, '20001', 'MALE', '$2a$10$oTSVakzI5130kh/Pu6QVpOvH5sPt4lhWRNdiCVjMuc3xV73NSEf0e', '中国计量大学', '', '22级硕', NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2025-05-21 10:36:36', 0, 'USER', 0, '2025-05-21 10:36:36', '黄金金');
INSERT INTO `user` VALUES (98, '123123', NULL, '$2a$10$QP5rba8t3oPmYr50P4O7vup0kus4lJszwVh39fbPinIDC14uOHtf.', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2025-05-21 10:42:07', 0, 'USER', 0, '2025-05-21 10:42:07', '');
INSERT INTO `user` VALUES (99, '111111', NULL, '$2a$10$INk4mbIgrPKVB9yH38L04O1S5cnYMHsCjt.WlgZOEjfXIjwRQS2Su', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.0%(0/85)', NULL, '2025-05-21 10:42:33', 0, 'ADMIN', 0, '2025-05-21 10:42:33', '');

SET FOREIGN_KEY_CHECKS = 1;
