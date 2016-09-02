-- Create the user 
create user SYSWARE_PATCHMANAGE
  default tablespace SYSWARE
  temporary tablespace TEMP
  profile DEFAULT;
-- Grant/Revoke role privileges 
grant connect to SYSWARE_PATCHMANAGE;
grant dba to SYSWARE_PATCHMANAGE;
grant resource to SYSWARE_PATCHMANAGE;
-- Grant/Revoke system privileges 
grant unlimited tablespace to SYSWARE_PATCHMANAGE;