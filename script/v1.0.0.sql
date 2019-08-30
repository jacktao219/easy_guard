--
-- Table structure for table `guard_datasource_define`
--

CREATE TABLE `guard_datasource_define` (
  `system` varchar(48) NOT NULL COMMENT '系统',
  `username` varchar(48) NOT NULL COMMENT '用户名',
  `pwd` varchar(64) DEFAULT NULL COMMENT '加密后的密码 ',
  `driver_class` varchar(64) DEFAULT 'com.mysql.cj.jdbc.Driver' COMMENT '驱动',
  `url` varchar(1024) NOT NULL COMMENT '数据库链接',
  PRIMARY KEY (`system`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库定义';

--
-- Table structure for table `guard_define`
--

CREATE TABLE `guard_define` (
  `guard_define_id` int(11) NOT NULL AUTO_INCREMENT,
  `system` varchar(48) DEFAULT NULL COMMENT '所属系统',
  `guard_name` varchar(48) DEFAULT NULL COMMENT '告警名称',
  `guard_type` varchar(32) DEFAULT NULL COMMENT '告警类型 sql:数据库配置 http:拨测接口',
  `guard_evaluate` varchar(1024) DEFAULT NULL COMMENT '告警评估，如：sql语句的配置或http接口',
  `evaluate_expect` varchar(100) DEFAULT NULL COMMENT '评估期望结果，如果不等于此结果就告警',
  `frequency` varchar(16) DEFAULT NULL COMMENT '频率',
  `repeat_gap` int(11) DEFAULT '30' COMMENT '多少分钟内不重复告警，默认30分钟',
  `warn_notice_msg` varchar(1024) DEFAULT NULL COMMENT '告警内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`guard_define_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='告警定义表';

--
-- Table structure for table `guard_notice_define`
--

CREATE TABLE `guard_notice_define` (
  `notice_define_id` int(11) NOT NULL AUTO_INCREMENT,
  `guard_define_id` int(11) NOT NULL,
  `guard_name` varchar(64) DEFAULT NULL COMMENT '告警名称',
  `warn_channel` varchar(32) DEFAULT NULL COMMENT 'message:短信 email:邮件',
  `warn_notice_target` varchar(32) DEFAULT NULL COMMENT '告警对象',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`notice_define_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='告警定义表';

--
-- Table structure for table `guard_notice_group`
--

CREATE TABLE `guard_notice_group` (
  `notice_group_id` int(11) NOT NULL AUTO_INCREMENT,
  `notice_group_name` varchar(32) DEFAULT NULL COMMENT '告警组名称',
  `notice_group_code` varchar(32) DEFAULT NULL COMMENT '告警组编码',
  `target_type` varchar(16) DEFAULT NULL COMMENT '类型 message:短信 email:邮件',
  `target_value` varchar(64) DEFAULT NULL COMMENT '手机号或者邮箱',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`notice_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='告警组';

--
-- Table structure for table `guard_notice_record`
--

CREATE TABLE `guard_notice_record` (
  `notice_record_id` int(11) NOT NULL AUTO_INCREMENT,
  `notice_define_id` int(11) NOT NULL,
  `guard_record_id` int(11) NOT NULL,
  `guard_name` varchar(64) DEFAULT NULL COMMENT '告警名称',
  `notice_status` varchar(16) DEFAULT NULL COMMENT '通知到达',
  `notice_remark` varchar(256) DEFAULT NULL COMMENT '通知描述',
  `warn_channel` varchar(32) DEFAULT NULL COMMENT 'message:短信 email:邮件',
  `warn_notice_target` varchar(32) DEFAULT NULL COMMENT '告警对象',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`notice_record_id`),
  KEY `guard_notice_record_create_time_IDX` (`create_time`,`notice_define_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='告警对象记录表';

--
-- Table structure for table `guard_record`
--

CREATE TABLE `guard_record` (
  `guard_record_id` int(11) NOT NULL AUTO_INCREMENT,
  `guard_define_id` int(11) NOT NULL,
  `system` varchar(48) DEFAULT NULL COMMENT '所属系统',
  `guard_name` varchar(48) DEFAULT NULL COMMENT '告警名称',
  `guard_type` varchar(32) DEFAULT NULL COMMENT '告警类型 sql:数据库sql配置',
  `guard_evaluate` varchar(1024) DEFAULT NULL COMMENT '告警评估，如：sql语句的配置',
  `evaluate_expect` varchar(100) DEFAULT NULL COMMENT '评估期望结果，如果不等于此结果就告警',
  `evaluate_result` varchar(100) DEFAULT NULL COMMENT '评估实际结果',
  `exec_result` varchar(100) DEFAULT NULL COMMENT '执行结果',
  `exec_remark` varchar(100) DEFAULT NULL COMMENT '执行备注',
  `frequency` varchar(48) DEFAULT NULL COMMENT '频率',
  `warn_notice_msg` varchar(1024) DEFAULT NULL COMMENT '告警内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`guard_record_id`),
  KEY `guard_record_create_time_IDX` (`create_time`,`guard_define_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='告警记录表';

