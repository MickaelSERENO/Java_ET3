JCC = javac
SRCPATH = src/
JFLAGS = -g -sourcepath src/ -cp bin -d bin
CLASSES =  

OBJ = $(CLASSES:.java=.class)

all: classes

classes: $(OBJ)

%.class:%.java
	$(JCC) $(JFLAGS) $<

run:
	java -cp bin com.et3
