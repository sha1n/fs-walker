#!/usr/bin/env bash

red="\x1b[31;01m"
green="\x1b[32;01m"
reset="\x1b[0m"

ERROR="${red}ERROR: ${reset}"
INFO="${green}INFO: ${reset}"

function traverse() {
for file in "$1"/*
do
    if [ ! -d "${file}" ] ; then
        echo -e "${INFO} ${file} is a file"
    elif [ ${file: -4} == ".txt" ]; then
        echo -e "${ERROR} ${file} is a directory"
    elif [ ${file: -4} == ".jar" ]; then
        echo -e "${ERROR} ${file} is a directory"
    else
        traverse "${file}"
    fi
done
}

function main() {
    traverse "$1"
}

main "$1"