JCC           = javac
SRCPATH       = src/
JFLAGS        = -g -sourcepath src/ -cp bin -d bin
ENGINECLASSES = $(wildcard src/com/et3/Engine/*.java src/com/et3/Game/*.java src/com/et3/Player/*.java)
ENGINEOBJ     = $(ENGINECLASSES:.java=.class)

all: engineclasses

engineclasses: $(ENGINEOBJ)

%.class: %.java
	$(JCC) $(JFLAGS) $<

run:
	java -cp bin com.et3.Game.Main

clean:
	rm -r bin/com
