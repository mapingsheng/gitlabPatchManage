-- Create table
create table PM_PATCHRECORD
(
  pid           VARCHAR2(60) not null,
  startcommitid VARCHAR2(60),
  endcommitid   VARCHAR2(60),
  patchuserid   VARCHAR2(60),
  patchtime     TIMESTAMP(6),
  patchdesc     VARCHAR2(900),
  projectid     VARCHAR2(60),
  projectname   VARCHAR2(200),
  patchfileurl  VARCHAR2(200),
  projecturl    VARCHAR2(100)
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
comment on table PM_PATCHRECORD
  is '补丁包打包记录表';
-- Add comments to the columns 
comment on column PM_PATCHRECORD.pid
  is '主键id';
comment on column PM_PATCHRECORD.startcommitid
  is '开始提交id';
comment on column PM_PATCHRECORD.endcommitid
  is '结束提交id';
comment on column PM_PATCHRECORD.patchuserid
  is '打包用户id';
comment on column PM_PATCHRECORD.patchtime
  is '打包时间';
comment on column PM_PATCHRECORD.patchdesc
  is '打包描述信息';
comment on column PM_PATCHRECORD.projectid
  is '关联的项目id';
comment on column PM_PATCHRECORD.projectname
  is '关联的项目名称';
comment on column PM_PATCHRECORD.patchfileurl
  is '补丁包路径(供下载使用)';
comment on column PM_PATCHRECORD.projecturl
  is '项目在gitlab中对应的仓库地址';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PM_PATCHRECORD
  add constraint PK primary key (PID)
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
