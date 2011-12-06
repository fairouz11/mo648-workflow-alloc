#!/bin/bash
# Script para execução automática do pso

# Variáveis
workflow="0 1 2 3 4"
executions=5

echo "Script started...";

# Cria o diretório resultado_pso, caso ele não exista
[ -a resultado_pso ] || mkdir resultado_pso

# Execução do Algoritmo PSO no Cloudsim
for i in $workflow; do
	echo ":: Processando workflow de numero = $i";
	
	for x in `seq 1 $executions`; do
   	    if [ $x = 1 ]; then
   	    	[ -a resultado_pso/"$i" ] || mkdir resultado_pso/"$i"
   	    fi
   	    # Executa o simulador
   	    echo ":: Execução de numero = $x";
   	    [ -a resultado_pso/"$i"/"$x" ] || touch resultado_pso/"$i"/"$x"
   	    ./run.sh data/datacenters/0 data/workflows/pso/"$i" resultado_pso/"$i"/"$x" pso 70
	done
done

