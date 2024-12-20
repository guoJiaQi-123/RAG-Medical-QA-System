CREATE TABLE `file_info` (
                             `id` int(11) NOT NULL AUTO_INCREMENT,
                             `file_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '文件名字',
                             `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小',
                             `char_count` int(11) DEFAULT NULL COMMENT '文件字符',
                             `segments` int(11) DEFAULT NULL COMMENT '文件分段',
                             `status` varchar(222) COLLATE utf8mb4_bin DEFAULT '待处理' COMMENT '文件状态',
                             `upload_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
                             `file_path` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '文件路径',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;