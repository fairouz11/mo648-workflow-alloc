#!/bin/bash
#Plot all graphs

datadir=data/results
graphdir=data/report/graphs
tmp=/tmp/plot

dirs=($datadir/*)

function getName() {

    if [ "$1" == "random" ]
    then
        echo "Random"
    elif [ "$1" == "roundrobim" ]
    then
        echo "Round-Robin"
    elif [ "$1" == "pso" ]
    then
        echo "PSO"
    elif [ "$1" == "pcs" ]
    then
        echo "PCS"
    else
        echo "NONE"
    fi
}

function doPlot() {

# 1 - output image file (just file path, no extension ex. data/img/img01)
# 2 - input data

outputprefix=$1  #without extension
inputfile=$2

for t in cost time
do

    outputfile=$outputprefix"_"$t

    if [ "$t" == "cost" ]
    then
        datacol=3
        errcol=4
        label="Cost ($)"
    else
        datacol=5
        errcol=6
        label="Time (s)"
    fi

    echo "
set terminal jpeg enhanced
set output '${outputfile}.jpeg'

set style data histogram
set style histogram cluster gap 1

set boxwidth 0.5

set style fill transparent solid 0.5 noborder
set xrange [0:5]
set yrange [0:*]

set ylabel '${label}'
set xlabel ''

plot '${inputfile}' using 1:${datacol}:${errcol}:xtic(2) t col w boxerror lc rgb '#444444' lw 1.5
" | gnuplot

done

}

mkdir -p $graphdir

for graph in ${dirs[@]}
do
    #diretorios
    graphs=($(ls $graph))

    i=${#datadir}
    i=$((i+1))
    graphname=${graph:$i}

    echo -n "Ploting $graph..."

    echo -e "ID\tAlgorithm\tCost\tErroCost\tTime\tErrorTime" > $tmp

    i=0

    for r in ${graphs[@]}
    do
        #resultados

        i=$((i+1))

        index=$(expr index "$r" ".")
        algo=${r:$index}

        name=$(getName $algo)

        data=($(cat $graph/$r | tail -n+3))

        echo -e "${i}\t${name}\t${data[0]}\t${data[2]}\t${data[1]}\t${data[3]}" >> $tmp

    done

    # input data done, now plot script

    doPlot $graphdir"/"$graphname $tmp

    echo -ne "OK\n"

done