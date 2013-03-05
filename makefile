JFLAGS =

JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        CoinFlip.java \
	SealedDES.java \
	BruteForceDES.java
				
default: classes
			
classes: $(CLASSES:.java=.class)
		
clean:		
	$(RM) *.class
		

