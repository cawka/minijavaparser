#!/usr/bin/awk -f
{
    if( $0 ~ /^\.basedir/ ) printf ".basedir \"%s\"\n", basedir; else print $0;
}
