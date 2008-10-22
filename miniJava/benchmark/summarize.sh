#!/bin/bash

ALGOS=`ls`

for tmp in $ALGOS; do
    PROGS=`ls $tmp`
    break;
done

echo Analysis times
for prog in $PROGS; do
    echo $prog
    for algo in $ALGOS; do
	if [ ! -d $algo ]; then continue; fi
	cat $algo/$prog/_bdd.log | awk '
BEGIN {
    time=0;
}
{
    FS="[(| ms)]";
    if( $0 ~ /^done\./ ) if( $3 ~ /[0-9]+/ ) time=time+$3;
#    // printf "Total time %s, %s, %s\n", $1, $2,$3;
}
END {
    printf " & %d", time;
}
	'
    done
    echo \ \\\\ \\hline
done

echo 
echo Information about benchmarks
for prog in $PROGS; do
    echo $prog
	wc a4/$prog/T.map | awk '{ printf " & %s", $1; }'
	wc a4/$prog/M.map | awk '{ printf " & %s", $1; }'
	wc a4/$prog/V.map | awk '{ printf " & %s", $1; }'
	wc a4/$prog/H.map | awk '{ printf " & %s", $1; }'
	cat a4/$prog/mIc.tuples| awk '
	BEGIN {
	    contexts=0;
	}
	{ 
	    if( $0 ~ /^[0-9]+/ ) { contexts=$4; }
	}
	END {
	    printf " & %d",contexts;
	}
	'
    echo \ \\\\ \\hline
done
