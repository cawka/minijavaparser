#!/usr/bin/gawk

{
    FS=",";
    printf "first %s, ___ %s, ___ %s, ___ %s\n", $1, $2, $3, $4;
}
