# System test of annotation tools

ANNCAT = ../../../scene-lib/anncat
XJAVAC = ../../../../jsr308-langtools/dist/bin/javac -g

all: clean results

results: out1 check-out2 out3 out4.class check-out5

clean:
	rm -rf out[1-5]*


# Step 1: Compile the source with annotations.
out1:
	mkdir out1
	${XJAVAC} -d out1 source-ann/AnnotationTest.java || (\rm -rf out1 && false)

# Step 2: Convert the annotated class file to an annotation file.
# Do we get the right annotations?
out2.jann:
	${ANNCAT} --class out1/annotations/tests/AnnotationTest.class --out --index out2.jann

out2.diff: out2.jann
	diff -u expected-annos.jann out2.jann > out2.diff

.PHONY: check-out2
# Fail if out2.diff is empty
check-out2: out2.diff
	[ ! -s out2.diff ] || (cat out2.diff && false)

# Step 3: Compile the source without annotations.
out3:
	mkdir out3
	${XJAVAC} -d out3 source-plain/AnnotationTest.java || (\rm -rf out3 && false)

# Step 4: Insert annotations into the class file.
out4.class:
	${ANNCAT} --index expected-annos.jann --out --class out3/annotations/tests/AnnotationTest.class --to out4.class

# Step 5: Convert the annotation-inserted class file to an annotation file.
# Do we get the right annotations?
# (The annotation-compiled and annotation-inserted class files tend to differ
# for stupid reasons (e.g., order of items in the constant pool), so we don't
# compare them.)
out5.jann:
	${ANNCAT} --class out4.class --out --index out5.jann

out5.diff: out5.jann
	diff -u expected-annos.jann out5.jann > out5.diff

.PHONY: check-out5
# Fail if out5.diff is empty
check-out5: out5.diff
	[ ! -s out5.diff ] || (cat out5.diff && false)