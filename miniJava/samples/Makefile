SOURCES=BinaryTree-error.java   Factorial.java                         TestClasses.java \
BinaryTree.java         LinearSearch-error.java  \
LinearSearch.java       MoreThan4.java          TreeVisitor.java \
BubbleSort.java         LinkedList-error.java   QuickSort-error.java	\
Factorial-error.java    LinkedList.java         QuickSort.java	\

a1: $(SOURCES:.java=.a1) $(SOURCES:.java=.a1.datalog)

datalog:
	rm *.a1.datalog
	make a1

%.a1: %.java
	java -jar ../parser.jar ../benchmark/a1/$(@:.a1=) $(@:.a1=.java) > ../benchmark/a1/$(@:.a1=)/_rules.txt
	cp ../benchmark/a1/$(@:.a1=)/_rules.txt $@

%.a1.datalog: ../ci.datalog
	awk -v basedir=../benchmark/a1/$(@:.a1.datalog=) -f ../rebase-datalog.awk ../ci.datalog > $@
	java -jar ../bdd.jar $@

clean:
	rm *.a1
	rm *.a1.datalog
