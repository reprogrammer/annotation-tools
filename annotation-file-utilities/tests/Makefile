# Very rough testing framework for the annotator that is based on the
# testing framework for the Javarifier.  Running 'make all' will look for
# all myClass.goal files in this directory, run the annotator on the
# corresponding .jaif and .java files, and then output the difference in a
# myClass.diff file in this directory.
#
# To test just one file, use (for example) 'make myClass.diff'.

# Put user-specific changes in your own Makefile.user.
# Make will silently continue if that file does not exist.
-include Makefile.user

# Override these in Makefile.user if the java and javac commands are not on
# your execution path.  Example from Makefile.user:
#   JAVA=${JAVA_HOME}/bin/java
#   JAVAC=${JAVA_HOME}/bin/javac
JAVA?=java -ea
XJAVAC?=javac

export SHELL=/bin/bash -o pipefail


DIFFS := $(wildcard *.goal)
DISABLED := $(shell grep -le "@skip-test" $(DIFFS))
FILTERED := $(filter-out $(DISABLED),$(DIFFS))
DIFFS := $(patsubst %.goal, %.diff, $(FILTERED))

DEBUG :=
# Use this to enable some debugging.
# DEBUG := --debug

default : all

.PHONY: all
all : $(DIFFS) system-test source-extension results

.PHONY: source-extension
source-extension:
	-${MAKE} -C source-extension #The minus(-) indicates that the process should continue if there is a failure

.PHONY: system-test
system-test:
	-${MAKE} -C system-test      #The minus(-) indicates that the process should continue if there is a failure

# Display results of all .diff files.
.PHONY: results
results: bin/VerifyDiffs.class
	@echo ""
	@echo "=== RESULTS ==="
	@echo ""
	@$(JAVA) -cp bin VerifyDiffs --show_all

# Remakes the little java program that checks and compares diffs
bin/VerifyDiffs.class : VerifyDiffs.java
	@$(XJAVAC) -g -cp ../bincompile -d bin VerifyDiffs.java

# Compiles all the test cases (be verbose about this).
compile :
	mkdir -p bin
	$(XJAVAC) -g -cp ../bin -d bin *.java

.PRECIOUS : bin/annotator/tests/%.class
bin/annotator/tests/%.class: %.java
	mkdir -p bin
# Added "-Xlint:-options" to see if it permits Jenkins job to succeed, due to
# problem "target value 1.8 is obsolete and will be removed in a future release"
	$(XJAVAC) -Xlint:-options -g -cp bin:../bin -d bin -sourcepath . $*.java

# Actually runs the annotator to create the annotated java file.
# We are required to put annotation-file-utilities.jar (and ../bin) on the
# bootclasspath so that the JSR 308 javac classes bundled therein are found
# before the stock javac classes that the Mac OS includes on the bootclasspath
# (other platforms do not make such inclusions)
.PRECIOUS: %.output
%.output: %.jaif %.java bin/annotator/tests/%.class ../lib/plume.jar ../bin ../annotation-file-utilities.jar
	$(JAVA) \
        -Xbootclasspath/p:../bin:../annotation-file-utilities.jar \
        -cp bin \
	annotator.Main \
	${DEBUG} \
	--abbreviate=false \
	-d $*-output \
	$*.jaif \
	$*.java \
	2>&1 | tee $*.log
	find "$*-output" -name '*.java' -print | xargs cat > "$*.output"
	rm -rf $*-output

# Compare the output of the annotator and the goal file.
%.diff: %.goal %.output
	-diff -u $*.goal $*.output >& $*.diff

# Remove all .diff, .log files from the tests directory.
.PHONY: clean
clean :
	rm -rf bin
	rm -f *.diff
	rm -f *.log
	rm -f *.output
