package annotator.find;

import plume.Pair;

/**
 * Specifies an annotation to be inserted into a source file.
 */
public class AnnotationInsertion extends Insertion {

    /**
     * The annotation to insert.
     */
    private final String annotation;
    private String type;

    /**
     * Creates a new insertion.
     *
     * @param annotation the annotation to insert
     * @param criteria where to insert the annotation
     * @param separateLine whether to insert the annotation on its own
     */

    public AnnotationInsertion(String annotation, Criteria criteria, boolean separateLine) {
        super(criteria, separateLine);
        this.annotation = annotation;
        type = null;
    }

    /**
     * Creates a new insertion with an empty criteria and the text inserted on
     * the same line.
     *
     * @param annotation the text to insert
     */
    public AnnotationInsertion(String annotation) {
        this(annotation, new Criteria(), false);
    }

    /**
     * Gets the insertion text.
     *
     * @param comments
     *            if true, the annotation will be surrounded by block comments.
     * @param abbreviate
     *            if true, the package name will be removed from the annotation.
     *            The package name can be retrieved again by calling the
     *            {@link #getPackageName()} method.
     * @return the text to insert
     */
    protected String getText(boolean comments, boolean abbreviate) {
        String result = annotation;
        if (abbreviate) {
            Pair<String, String> ps = removePackage(result);
            String packageName = ps.a;
            result = ps.b;
            if (packageName != null) {
                packageNames.add(packageName);
            }
        }
        if (!result.startsWith("@")) {
            throw new Error("Illegal insertion, must start with @: " + result);
        }
        if (type != null) { result = "new " + result + " " + type; }
        if (comments) {
            return "/*" + result + "*/";
        }
        return result;
    }

    /**
     * Gets the raw, unmodified annotation that was passed into the constructor.
     * @return the annotation.
     */
    public String getAnnotation() {
        return annotation;
    }

    /** {@inheritDoc} */
    protected boolean addLeadingSpace(boolean gotSeparateLine, int pos,
            char precedingChar) {
        if (precedingChar == '.') {
            return false;
        }
        return super.addLeadingSpace(gotSeparateLine, pos, precedingChar);
    }

    /** {@inheritDoc} */
    public Kind getKind() {
        return Kind.ANNOTATION;
    }

    public String toString() {
        return annotation + " " + super.toString();
    }

    public void setType(String s) {
      this.type = s;
    }
}
