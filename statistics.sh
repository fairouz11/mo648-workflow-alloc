#!/bin/bash
# Script para calculo de media, variancia e intervalo de confianca

#usar ./statistics.sh nome_arquivo_entrada t-student [start_line]

[ $# -lt 2 ] && echo "./statistics.sh nome_arquivo_entrada t-student [start_line]" && exit 0

#INPUT
input=$1
line=$3
student=$2

[ "$line" == "" ] && line=1

# DO NOT CHANGE FROM HERE

if ! [ -a $input ]
then
    echo "Nothing to do. Exiting"
    exit 0
fi

n=0

costSum=0
timeSum=0

costMean=0
timeMean=0

costVar=0
timeVar=0

costErr=0
timeErr=0

lnum=($(wc -l $input))

for i in `seq $line $lnum`
do
    value=($(sed -n "${i}p" $input))

    cost=${value[0]}
    time=${value[1]}

    costSum=$(echo "$costSum + $cost" | bc -l)
    timeSum=$(echo "$timeSum + $time" | bc -l)
   
    n=$((n+1))
 
done

# Calcula a média dos valores
costMean=$(echo $costSum/$n | bc -l)
timeMean=$(echo $timeSum/$n | bc -l)

# Calcula a Variância

for i in `seq $line $lnum`
do
    value=($(sed -n "${i}p" $input))

    cost=${value[0]}
    time=${value[1]}

    costVar=$(echo "$costVar + ($cost - $costMean)^2" | bc -l)
    timeVar=$(echo "$timeVar + ($time - $timeMean)^2" | bc -l)
    
done


costVar=$(echo "$costVar / $n" | bc -l)    
timeVar=$(echo "$timeVar / $n" | bc -l)    

costErr=$(echo "$student * sqrt($costVar) / sqrt($n)" | bc -l)    
timeErr=$(echo "$student * sqrt($timeVar) / sqrt($n)" | bc -l)    

# Resultados
echo -e "$costMean\\t$timeMean\\t$costErr\\t$timeErr"