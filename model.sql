set names utf8;
use `kpi`;

CREATE TABLE `kpi` (
  `id` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `xmId` INT(10) NOT NULL COMMENT '项目Id',  
  `kpiId` INT(10) NOT NULL COMMENT 'kpiId',
  `kpi` DECIMAL(12,2) NOT NULL COMMENT 'kpi',
  `kpiDate` DATE NOT NULL DEFAULT 0 COMMENT 'kpi时间',
  `createDate` DATE NOT NULL DEFAULT 0 COMMENT '创建时间',
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY `kpidate_xmid_kpiid_idx` (`kpiDate`, `xmId`, `kpiId`),
  UNIQUE KEY `createdate_xmid_kpiid_idx` (`createDate`, `xmId`, `kpiId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
