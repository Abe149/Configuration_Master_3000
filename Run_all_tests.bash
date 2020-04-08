#!/usr/bin/env bash

./test_static-only_expected-to-pass-even-in-strict-mode_tests.bash
exit_code_1=$?

echo

./test_static-only_expected-to-pass-only-in-nonstrict-mode_tests.bash
exit_code_2=$?

./test_static-only_expected-to-fail-even-in-nonstrict-mode_tests.bash
exit_code_3=$?

if [ 0 != $exit_code_1 -o 0 != $exit_code_2 -o 0 != $exit_code_3 ]; then
  echo -e '\033[31mAt least one test script reported a problem.\033[0m'
fi
