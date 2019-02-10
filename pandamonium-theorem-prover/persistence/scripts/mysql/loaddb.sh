#!/usr/bin/env bash
export MYSQL_PWD=secret

for sqlScript in $( find ../../ -name "*.sql" -print | sort);
   do
      echo "**** $sqlScript ****"
      mysql --batch --quick --raw --line-numbers --force --user=panda < $sqlScript
   done