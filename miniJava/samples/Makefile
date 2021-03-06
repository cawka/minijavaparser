SOURCES=t1.java Factorial.java  TestClasses.java TestContexts.java \
BinaryTree.java         LinearSearch.java       MoreThan4.java          TreeVisitor.java \
BubbleSort.java         LinkedList-error.java   \
LinkedList.java         QuickSort.java

all: a1 a2 a3 a4 a5

a1: $(SOURCES:.java=.a1) $(SOURCES:.java=.a1.datalog)

a2: $(SOURCES:.java=.a2) $(SOURCES:.java=.a2.datalog)

a3: $(SOURCES:.java=.a3) $(SOURCES:.java=.a3.datalog)

a4: $(SOURCES:.java=.a4) $(SOURCES:.java=.a4.datalog) $(SOURCES:.java=.a4.pdf)

a5: $(SOURCES:.java=.a5) $(SOURCES:.java=.a5.datalog)

datalog:
	rm *.a1.datalog
	make a1

%.a1: %.java
	mkdir -p ../benchmark/a1/$(@:.a1=)
	java -jar ../parser.jar ../benchmark/a1/$(@:.a1=) $(@:.a1=.java) a1 > ../benchmark/a1/$(@:.a1=)/_rules.txt
	cp ../benchmark/a1/$(@:.a1=)/_rules.txt $@

%.a1.datalog: ../a1.datalog
	awk -v basedir=../benchmark/a1/$(@:.a1.datalog=) -f ../rebase-datalog.awk ../a1.datalog > $@
	java -jar ../bdd.jar $@ > ../benchmark/a1/$(@:.a1.datalog=)/_bdd.log

%.a2: %.java
	mkdir -p ../benchmark/a2/$(@:.a2=)
	java -jar ../parser.jar ../benchmark/a2/$(@:.a2=) $(@:.a2=.java) a2 > ../benchmark/a2/$(@:.a2=)/_rules.txt
	cp ../benchmark/a2/$(@:.a2=)/_rules.txt $@

%.a2.datalog: ../a2.datalog
	awk -v basedir=../benchmark/a2/$(@:.a2.datalog=) -f ../rebase-datalog.awk ../a2.datalog > $@
	java -jar ../bdd.jar $@ > ../benchmark/a2/$(@:.a2.datalog=)/_bdd.log

%.a3: %.java
	mkdir -p ../benchmark/a3/$(@:.a3=)
	java -jar ../parser.jar ../benchmark/a3/$(@:.a3=) $(@:.a3=.java) a3 > ../benchmark/a3/$(@:.a3=)/_rules.txt
	cp ../benchmark/a3/$(@:.a3=)/_rules.txt $@

%.a3.datalog: ../a3.datalog
	awk -v basedir=../benchmark/a3/$(@:.a3.datalog=) -f ../rebase-datalog.awk ../a3.datalog > $@
	java -jar ../bdd.jar $@ > ../benchmark/a3/$(@:.a3.datalog=)/_bdd.log

%.a4: %.java
	mkdir -p ../benchmark/a4/$(@:.a4=)
	java -jar ../parser.jar ../benchmark/a4/$(@:.a4=) $(@:.a4=.java) a4 > ../benchmark/a4/$(@:.a4=)/_rules.txt
	cp ../benchmark/a4/$(@:.a4=)/_rules.txt $@

%.a4.datalog: ../a4.datalog
	awk -v basedir=../benchmark/a4/$(@:.a4.datalog=) -f ../rebase-datalog.awk ../a4.datalog > $@
	java -jar ../bdd.jar $@ > ../benchmark/a4/$(@:.a4.datalog=)/_bdd.log

%.a4.pdf:
	dot -Tpdf -o $(@:.a4.pdf=_ad.pdf) ../benchmark/a4/$(@:.a4.pdf=)/call_graph.dot
	dot -Tpdf -o $(@:.a4.pdf=_cg_orig.pdf) ../benchmark/a4/$(@:.a4.pdf=)/call_graph_orig.dot
	dot -Tpdf -o $(@:.a4.pdf=_a4_classes.pdf) ../benchmark/a4/$(@:.a4.pdf=)/class_diagram.dot

%.a5: %.java
	mkdir -p ../benchmark/a5/$(@:.a5=)
	java -jar ../parser.jar ../benchmark/a5/$(@:.a5=) $(@:.a5=.java) a5 > ../benchmark/a5/$(@:.a5=)/_rules.txt
	cp ../benchmark/a5/$(@:.a5=)/_rules.txt $@

%.a5.datalog: ../a5.datalog
	awk -v basedir=../benchmark/a5/$(@:.a5.datalog=) -f ../rebase-datalog.awk ../a5.datalog > $@
	java -jar ../bdd.jar $@ > ../benchmark/a5/$(@:.a5.datalog=)/_bdd.log


clean:
	rm *.a1 || true
	rm *.a1.datalog || true
	rm *.a2 || true
	rm *.a2.datalog || true
	rm *.a3 || true
	rm *.a3.datalog || true
	rm *.a4 || true
	rm *.a4.datalog || true
	rm *.a5 || true
	rm *.a5.datalog || true
	rm *.pdf || true


