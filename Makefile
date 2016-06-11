JCC = javac
SRCPATH = src/
JFLAGS = -g -sourcepath src/ -cp bin -d bin
CLASSES = $(wildcard src/com/et3/Engine/*.java)

OBJ = $(CLASSES:.java=.class)

all: classes

classes: $(OBJ)

%.class:%.java
	$(JCC) $(JFLAGS) $<

run:
	java -cp bin com.et3
