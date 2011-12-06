#!/bin/bash
# Script para execução automática do pso

# Variáveis Globais
workflow="0 1 2 3 4"
executions=10

echo "Script started...";

function getResults(){
  minValue=0
  maxValue=0
  sumValue=0
  counter=0
  mean=0
  
  for x in `seq 1 $executions`; do
  	value=$(sed -n "$2""p" resultado_pso/"$1"/"$x")
  	if [ $(echo "$minValue == 0" | bc) -eq 1 ] || [ $(echo "$value < $minValue" | bc) -eq 1 ]; then
    		minValue=$value
    	fi
    	
    	if [ $(echo "$value > $maxValue" | bc) -eq 1 ]; then
    		maxValue=$value
    	fi
    	
    	((counter++))
    	sumValue=$(echo "$sumValue + $value" | bc)
  done
  
  # Calcula a média dos valores
  mean=$(echo $sumValue/$counter | bc -l)
  
  # Calcula a Variância
  variance=0;
  aux=0;
  for i in `seq 1 $executions`; do
     value=$(sed -n "$2""p" resultado_pso/"$1"/"$i")
     aux=$(echo "($value - $mean)^2" | bc -l)
     variance=$(echo "($variance + $aux)" | bc -l);
  done
  variance=$(echo "$variance/$counter" | bc -l)
  
  # Resultados
  echo "$w $mean $minValue $maxValue $variance";
}

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



echo "Criando arquivos para a geração dos gráficos";
# Calcula o custo médio do pso para cada workflow
for w in $workflow
do
	getResults $w 2 >> resultado_pso/custo_workflow.dat
done

# Calcula o tempo médio do pso para cada workflow
for w in $workflow
do
	getResults $w 3 >> resultado_pso/tempo_workflow.dat
done

echo "Gerando gráficos com Gnuplot";
gnuplot custo_workflow.gpi
gnuplot tempo_workflow.gpi
echo "Fim do script.";

