#!/bin/bash
# Script para execução automática de workflows

function help(){

    echo "$0 algorithm_name workflow_type output_folder deadline execution_number t-student"

}

if [ $# -lt 6 ]
then
    help
    exit 0
fi


#INPUT
executions=$5
type=$2    # input type (folder inside data/workflows/"
datacenter=data/datacenters/0
deadline=$4
algorithm=$1
outdir=$3
student=$6

# DO NOT CHANGE FROM HERE

fresult="result.data"

l=${#outdir}
[ "${outdir:l-1}" != "/" ] && outdir=$outdir"/"

# processor numbeer
num_cores=$(cat /proc/cpuinfo | grep processor | wc -l)

indir="data/workflows/"

indir=$indir$type"/"
outdir=$outdir$type"/"

#create output dir
! [ -d $outdir ] && mkdir -p $outdir

#verify if there are data there
d=$(ls $outdir)

if [ ${#d[@]} -gt 0 -a "${d[0]}" != "" ]
then

    yes="0"

    while [ "$yes" != "y" -a "$yes" != "n" -a "$yes" != "Y" -a "$yes" != "N" -a "$yes" != "" ]
    do

        echo -n "There are files in $outdir. I'll remove them all. Continue? [Y/n]: "
        read yes

    done
   
    if [ "$yes" == "n" -o "$yes" == "N" ]
    then
        exit 0
    fi

    echo "Removing files in $outdir"

    for i in $d
    do
        rm -r $outdir$i
    done

fi

workflow=($indir*)

num_work=${#workflow[@]}

if [ $num_work -eq 0 ]
then
    echo "Nothing to do. Exiting"
    exit 0
fi

#get just filename
base=$(dirname ${workflow[0]})
base=${#base}
base=$(($base+1))

for i in $(seq 0 $(($num_work-1)))
do
    s=${workflow[$i]}
    workflow[$i]=${s:base}
done

per_core=$(echo "$num_work/$num_cores" | bc)
remainder=$(echo "$num_work % $num_cores"  | bc)

if [ $num_work -lt $num_cores ]
then
    num_cores=$num_work
    per_core=1
    remainder=0
fi

echo "Script started...";
echo "Running with $num_cores threads"

#########################################################

function doCore(){

#core num $1

len=$per_core

if [ $1 -le $remainder ]
then
    len=$(echo "$len + 1" | bc)
    init=$(echo "($1 - 1) * $len" | bc) 
else
    init=$(echo "($1 - 1) * $len + $remainder" | bc) 
fi

end=$(echo "$init + $len - 1" | bc)

for i in $(seq $init $end)
do
    wf=${workflow[$i]}
    
    execout=$outdir$wf"."$x
    fout=$outdir$wf
 
    echo "# $(date) -- Output simulation of $executions executions of $indir$wf" > $fout
    echo -e "# Cost\\tValue" >> $fout

    for x in `seq 1 $executions`
    do
        echo "Core$1 running $wf.$x"
   	./run.sh $datacenter $indir$wf $execout $algorithm $deadline

        cost=$(sed -n "2p" $execout)
        time=$(sed -n "3p" $execout)
        
        echo -e "$cost\\t$time" >> $fout
        rm -f $execout

    done

    # executions finished - now calc values

done

}

#########################################################

for i in $(seq $num_cores); do

    doCore $i &
    proc[$i]=$!

done


#wait processes
for i in $(seq $num_cores); do

    wait ${proc[$i]}

done

# concat all files on output dir

of=$outdir$fresult
tmp=/tmp/$fresult$(date +%s)

rm -rf $tmp
touch $tmp 

for i in $outdir*
do

    tail -n+3 $i >> $tmp

done

#calc erros

echo "# $(date): Simulation of $indir using $algorithm scheduler and $executions executions with $deadline as deadline" > $of
echo "# costMean timeMean costErr timeErr" >> $of

./statistics.sh $tmp $student >> $of

rm -rf $tmp

echo "Data file for ploting is: $of"

exit 0
