all: setup gradebookadd gradebookdisplay

JFLAGS= -g -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar
JC = javac

#Use to incorporate libraries at run-time
CPFLAGS = -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar

gradebookadd:
	$(JC) $(JFLAGS) gradebookadd.java Gradebook.java
	echo "#!/bin/bash\n java $(CPFLAGS) gradebookadd \$$@" > gradebookadd
	chmod +x gradebookadd

gradebookdisplay:
	$(JC) $(JFLAGS) gradebookdisplay.java Gradebook.java
	echo "#!/bin/bash\n java $(CPFLAGS) gradebookdisplay \$$@" > gradebookdisplay
	chmod +x gradebookdisplay

setup:
	$(JC) $(JFLAGS) setup.java Gradebook.java
	echo "#!/bin/bash\n java $(CPFLAGS) setup \$$@" > setup
	chmod +x setup

clean:
	rm -f *.class
	rm -rf gradebookadd gradebookdisplay setup
