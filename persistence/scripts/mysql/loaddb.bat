set mysql_pwd = secret
FOR /R %%s IN (*.sql) do (
    echo **** %%s ****
    mysql -u panda -psecret < %%s
)
