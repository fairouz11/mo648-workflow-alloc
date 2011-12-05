all: src/randomDAG/randomDAG.java src/randomDAG/Task.java
	javac src/randomDAG/*.java -cp "src/jgrapht-0.5.3.jar:src/jgraph-5.5.1-lgpl.jar"

run: all
	java -cp "src:src/jgrapht-0.5.3.jar:src/jgraph-5.5.1-lgpl.jar" randomDAG.randomDAG
