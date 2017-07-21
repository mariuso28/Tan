select * from baseuser;

delete from transaction;
delete from xaction;

delete from authority where baseuserid <> (select id from baseuser where email = 'rpadmin@test.com');
delete from account where baseuserid <> (select id from baseuser where email = 'rpadmin@test.com');
delete from baseuser where id <> (select id from baseuser where email = 'rpadmin@test.com');