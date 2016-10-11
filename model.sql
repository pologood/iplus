CREATE TABLE IF NOT EXISTS kpi (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  xmId          INT NOT NULL COMMENT 'immut',
  kpiId         INT NOT NULL COMMENT 'immut',
  createDate    DATE NOT NULL COMMENT 'immut',
  kpi           INT,
  UNIQUE(xmId, kpiId, createDate)
);
