/Applications/MAMP/Library/bin/mysqladmin --host=localhost --user=manager --password=manager drop ebus -f
/Applications/MAMP/Library/bin/mysqladmin --host=localhost --user=manager --password=manager create ebus
/Applications/MAMP/Library/bin/mysql --host=localhost --user=manager --password=manager --database=ebus  0<ebus.sql