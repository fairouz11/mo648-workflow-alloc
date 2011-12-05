echo "./run.sh graphNum minSize maxSize minTaskCost maxTaskCost minCommCost maxCommCost"

java -cp src:src/jgrapht-0.5.3.jar:src/jgraph-5.5.1-lgpl.jar randomDAG.randomDAG $1 $2 $3 $4 $5 $6 $7 $8 $9
