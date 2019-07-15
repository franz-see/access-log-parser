create table period_summary (
  id int not null primary key auto_increment,
  ip_address varchar(255) not null,
  start_period datetime(6) not null,
  end_period datetime(6) not null,
  cnt int not null,
  comment varchar(1024) not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8