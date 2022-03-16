#!/bin/bash
set -e

cd "$(dirname "$0")"

if [ $# -eq 0 ]
  then
    echo "Version not provided as the first argument. E.g. 1.0.0"
    exit 1
fi

git tag "v$1-windows"
echo Tagged "v$1-windows"

git tag "v$1-macos"
echo Tagged "v$1-macos"

git tag "v$1-linux"
echo Tagged "v$1-linux"