-- Create table
create table PM_USER
(
  pid      VARCHAR2(60) not null,
  username VARCHAR2(60),
  password VARCHAR2(60),
  token    VARCHAR2(60)
)
tablespace SYSWARE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 8K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table PM_USER
  is '打包的用户表';
-- Add comments to the columns 
comment on column PM_USER.username
  is '用户姓名';
comment on column PM_USER.password
  is '用户密码';
comment on column PM_USER.token
  is '用户对应的gitlab服务器上面的通行标示(git使用)';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PM_USER
  add constraint UPK primary key (PID)
  using index 
  tablespace SYSWARE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
