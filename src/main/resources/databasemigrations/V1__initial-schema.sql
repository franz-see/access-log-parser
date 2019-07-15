create table access_log_entry (
  id int not null primary key auto_increment,
  timestamp datetime not null,
  ip_address varchar(255) not null,
  http_call varchar(255) not null,
  http_status_code int not null,
  user_agent varchar(1024) not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8