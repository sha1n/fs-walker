#!/usr/bin/env bash

red="\x1b[31;01m"
green="\x1b[32;01m"
reset="\x1b[0m"

ERROR="${red}ERROR: ${reset}"
INFO="${green}INFO: ${reset}"

function generate() {
for file in "$1"/*
do
    if [ -d "${file}" ] && [ ${file: -4} == ".txt" ]; then
        create_test_copy "$file"
        echo -e "${ERROR} ${file} is a directory"
    elif [ ! -d "${file}" ] && [ ${file: -4} == ".txt" ]; then
        create_test_copy "$file"
#    elif [ ${file: -4} == ".jar" ]; then
#        echo -e "${ERROR} ${file} is a directory"
    elif [ -d "${file}" ]; then
#        echo "entering recursion with: ${file}"
        generate "${file}"
    fi
done
}

function create_test_copy() {
    CMD="cat $1 >> $1.test"
    echo -e $CMD
    docker exec bazel-dev bash -c "$CMD"
}
function main() {
    generate "$1"
}

main "$1"