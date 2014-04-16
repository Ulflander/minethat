#!/bin/sh
#
# Bump a maven project version
#

MAX_ARGS=1  # Script requires not more than one argument
EX_USAGE=64 # Command line usage error
EX_NOINPUT=66   # Cannot open input
INPUT=pom.xml   # File with version data
VERSION_RE='[0-9]\.[0-9]\.[0-9]\+'  # RegExp to match version

usage () {
    err=$1 || 0
    echo "`basename $0` [ <newversion> | major | minor | patch ]"
    exit $err
}

bump () {
    OLD_VERSION=$(echo $1 | sed 's/\./\\\./g')
    NEW_VERSION=$2

    sed '/<version>/s/'$OLD_VERSION'/'$NEW_VERSION'/' $INPUT \
        > $INPUT.tmp && mv $INPUT.tmp $INPUT && \
        grep '<version>' $INPUT | sed 1q | grep -o $VERSION_RE
}

if [ $# -le "$MAX_ARGS" ]
then
    # Check for input file
    if [ -e $INPUT ]
    then
        CUR_VERSION=$(grep '<version>' $INPUT | sed 1q | grep -o $VERSION_RE)

        if [ $# -eq "0" ]
        then
            # Show current version if no arguments
            echo $CUR_VERSION
            exit 0
        fi

        case "$1" in
            "major")
                NEW_VERSION=$(echo $CUR_VERSION | awk 'BEGIN{FS="."}{print $1+1"."$2"."$3}')
                bump $CUR_VERSION $NEW_VERSION
                ;;

            "minor")
                NEW_VERSION=$(echo $CUR_VERSION | awk 'BEGIN{FS="."}{print $1"."$2+1"."$3}')
                bump $CUR_VERSION $NEW_VERSION
                ;;

            "patch")
                NEW_VERSION=$(echo $CUR_VERSION | awk 'BEGIN{FS="."}{print $1"."$2"."$3+1}')
                bump $CUR_VERSION $NEW_VERSION
                ;;

            *)
                if [ -n "$(echo $1 | grep -o $VERSION_RE)" ]
                then
                    NEW_VERSION=$1
                    bump $CUR_VERSION $NEW_VERSION
                else
                    usage
                fi
                ;;
        esac
    else
        echo "$INPUT not found in the current directory" >&2
        exit $EX_NOINPUT
    fi
else
    usage $EX_USAGE
fi