#!/bin/bash
set -e

cd "$(dirname "$0")"

if [ $# -eq 0 ]
  then
    echo "Version not provided as the first argument. E.g. 1.0.0"
    exit 1
fi

git tag -d "latest"
echo Untagged "latest"

git tag "v$1"
echo Tagged "v$1"

git tag "latest"
echo Tagged "latest"
