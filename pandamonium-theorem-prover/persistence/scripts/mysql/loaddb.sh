#!/usr/bin/env bash

for sqlScript in $( find . -name "*.sql" -print | sort);
   do
      echo "**** $sqlScript ****"
      mysql -u panda -psecret < $sqlScript
   done