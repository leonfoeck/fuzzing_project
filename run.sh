#!/usr/bin/env bash

set -e

function help_message {
  echo "${0} [-t|--timeout <seconds>] [-q|--quiet]"
}

function error_message {
  echo "\033[0;31m${1}\033[0m"
}

# Parse command line arguments
argline="java -javaagent:target/coverage-guided-fuzzing-1.0-jar-with-dependencies.jar"
argline="${argline} -cp target/coverage-guided-fuzzing-1.0-jar-with-dependencies.jar"
argline="${argline} de.uni_passau.fim.se2.st.fuzzing.CoverageGuidedFuzzer"
# argline="${argline} -c Rational -p de.uni_passau.fim.se2.st.fuzzing.fuzztarget"

while (( "$#" ));
do
  case "$1" in
    -t|--timeout)
      argline="${argline} -t $2"
      shift 2
      ;;
    -q|--quiet)
      argline="${argline} -q"
      shift
      ;;
    -c|--class)
      argline="${argline} -c $2"
      shift 2
      ;;
    -p|--package)
      argline="${argline} -p $2"
      shift 2
      ;;
    -h|--help)
      help_message
      exit 0
      ;;
    *)
      error_message "Unknown argument: ${1}"
      help_message
      exit 1
      ;;
  esac
done

# Build the project if necessary
if [[ ! -f target/coverage-guided-fuzzing-1.0-jar-with-dependencies.jar ]]; then
  mvn clean package
fi

# Cleanup old results if necessary
if [[ -d fuzzing-report ]]; then
  rm -rf fuzzing-report
fi

exec ${argline}