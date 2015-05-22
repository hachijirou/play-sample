# --- !Ups
create table m_user (
  id VARCHAR(20) NOT NULL,
  account VARCHAR(32) NOT NULL,
  password_hash VARCHAR(64) NOT NULL,
  email_address VARCHAR(255) NOT NULL,
  status TINYINT NOT NULL DEFAULT '1',
  created DATETIME NOT NULL,
  updated DATETIME NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY m_user_uidx1(account, password_hash)
);

# --- !Downs
drop table m_user;