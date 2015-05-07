.PHONY: build clean

FILES = src/Chris.java
SOURCEPATH = src/
BUILDPATH = out/classes
BIN = out/build/Christal.jar
MAIN = src.Chris
MANIFEST = out/manifest
ARGS = Test -o output

run: jar
	java -jar $(BIN) $(ARGS)

jar: build manifest
	jar cfm $(BIN) $(MANIFEST) -C $(BUILDPATH) .

build:
	javac -sourcepath $(SOURCEPATH) -d $(BUILDPATH) $(FILES)

manifest:
	echo Main-Class: $(MAIN)>$(MANIFEST)

clean:
	rm -rf $(BUILDPATH)/* $(BIN) $(MAINFEST)
