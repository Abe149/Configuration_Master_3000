#!/usr/bin/env bash

if [ "$0" = sh -o "$0" = /bin/sh -o "$0" = /usr/bin/sh -o "$0" = bash -o "$0" = -bash -o "$0" = /bin/bash -o "$0" = /usr/bin/bash ]; then
  echo 'Please do _not_ source this file!'
  return 1
fi

# export CONFIG_SERVER_URL='https://localhost:4430/' # commented out to test the improvements made to "get_config.bash" on April 16 2020

# tests that are expected to succeed when not _too_ strict

echo 'testing "test key name for a positive integer with redundancy"...'
if [ -n "$DEBUG" ] && [ $DEBUG -gt 0 ]; then
  CONFIG_MATURITY_LEVEL=5 bash -x ./get_config.bash 'should be present in all namespaces' 'test key name for a positive integer with redundancy'
else
  CONFIG_MATURITY_LEVEL=5         ./get_config.bash 'should be present in all namespaces' 'test key name for a positive integer with redundancy'
fi

echo 'testing "foo"::"test good positive_integer"...'
if [ -n "$DEBUG" ] && [ $DEBUG -gt 0 ]; then
  CONFIG_MATURITY_LEVEL=5 bash -x ./get_config.bash foo 'test good positive_integer'
else
  CONFIG_MATURITY_LEVEL=5         ./get_config.bash foo 'test good positive_integer'
fi



echo
echo
echo



# tests that are expected to fail if/when the server is in strict mode

echo 'testing "test key name for a positive integer with at least one conflict"...'
if [ -n "$DEBUG" ] && [ $DEBUG -gt 0 ]; then
  CONFIG_MATURITY_LEVEL=5 bash -x ./get_config.bash 'should be present in all namespaces' 'test key name for a positive integer with at least one conflict'
else
  CONFIG_MATURITY_LEVEL=5         ./get_config.bash 'should be present in all namespaces' 'test key name for a positive integer with at least one conflict'
fi


echo
echo


# --- test the Python client --- #

for Python_test in `find ./Python3_sources/Configuration_Master_client/ -iname 'test_*.py' -executable | sort`; do
  echo "testing ''$Python_test''..."
  if [ -n "$DEBUG" ] && [ $DEBUG -gt 9 ]; then # only when DEBUG > 9 since the debug output in this one [from the Python interpreter itself] is particularly long
    python3 -v "$Python_test"
  else
               "$Python_test"
  fi

  echo
  echo
done
