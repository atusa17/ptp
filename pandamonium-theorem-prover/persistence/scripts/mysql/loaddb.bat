SET MYSQL_PWD=secret
FOR /R %%s IN (.\*.sql) do (
    echo **** %%s ****
    echo mysql --batch --quick --raw --line-numbers --force --user=panda < %%s
)
