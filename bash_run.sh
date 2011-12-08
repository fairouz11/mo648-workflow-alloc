#!/bin/bash
# Script para execução automática de varios workflows

deadline=9999999999
nexec=1

student=1.725

tmpdir=/tmp/output
resultdir=data/results

algorithms=(random roundrobim)

workflows=(data/workflows/*)

base=$(dirname ${workflows[0]})
base=${#base}
base=$(($base+1))

for alg in ${algorithms[@]}
do

    echo "####### RUNNING $alg"

    rm -rf $tmpdir

    for wf in ${workflows[@]}
    do

        echo -n "## RUNNING $wf..."

        wft=${wf:$base}

        ./run_and_forget.sh $alg $wft $tmpdir $deadline $nexec $student 1>/dev/null 2>&1

        echo -n "OK...Coping resukt..."

        mkdir -p $resultdir/$wft
        cp $tmpdir/$wft/result.data $resultdir/$wft/result.$alg

        echo -en "OK\n"

    done

    echo "####### FINISHED $alg"
    echo -e "\n\n"

done

