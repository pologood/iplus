set names utf8;
use `kpi`;

CREATE TABLE `kpi` (
  `id` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `xmId` INT(10) NOT NULL COMMENT '项目Id',  
  `kpiId` INT(10) NOT NULL COMMENT 'kpiId',
  `kpi` DECIMAL(12,2) NOT NULL COMMENT 'kpi',
  `createDate` DATE NOT NULL DEFAULT 0 COMMENT 'kpi时间',
  UNIQUE KEY `xmid_kpiid_idx` (`xmId`, `kpiId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
