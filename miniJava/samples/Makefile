SOURCES=BinaryTree-error.java   Factorial.java                         TestClasses.java \
BinaryTree.java         LinearSearch-error.java  \
LinearSearch.java       MoreThan4.java          TreeVisitor.java \
BubbleSort.java         LinkedList-error.java   QuickSort-error.java	\
Factorial-error.java    LinkedList.java         QuickSort.java	\

ci: $(SOURCES:.java=.ci)

%.ci: %.java
	java -jar ../parser.jar ../benchmark/ci/$(@:.ci=) $(@:.ci=.java) >$@

clean:
	rm *.ci


