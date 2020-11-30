package com.github.mouse0w0.coffeemaker.template.tree;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import org.objectweb.asm.ClassVisitor;

public class BtClass extends BtObject implements AnnotationOwner {

    public static final String VERSION = "version";
    public static final String ACCESS = "access";
    public static final String NAME = "name";
    public static final String SIGNATURE = "signature";
    public static final String SUPER_NAME = "superName";
    public static final String INTERFACES = "interfaces";

    public static final String SOURCE_FILE = "sourceFile";
    public static final String SOURCE_DEBUG = "sourceDebug";

    public static final String MODULE = "module";

    public static final String OUTER_CLASS = "outerClass";
    public static final String OUTER_METHOD = "outerMethod";
    public static final String OUTER_METHOD_DESC = "outerMethodDesc";

    public static final String ANNOTATIONS = "annotations";

    public static final String TYPE_ANNOTATIONS = "typeAnnotations";

    public static final String ATTRIBUTES = "attributes";

    public static final String INNER_CLASSES = "innerClasses";

    public static final String NEST_HOST_CLASS = "nestHostClass";
    public static final String NEST_MEMBERS = "nestMembers";

    public static final String RECORD_COMPONENTS = "recordComponents";
    public static final String FIELDS = "fields";
    public static final String METHODS = "methods";

    public BtClass() {
    }

    @Override
    public BtList<BtAnnotation> getAnnotations() {
        return computeIfNull(ANNOTATIONS, k -> new BtList<>());
    }

    public BtList<BtField> getFields() {
        return computeIfNull(FIELDS, k -> new BtList<>());
    }

    public BtList<BtMethod> getMethods() {
        return computeIfNull(METHODS, k -> new BtList<>());
    }

    public void accept(ClassVisitor classVisitor, Evaluator evaluator) {
        // Visit the header.
        classVisitor.visit(
                computeInt(VERSION, evaluator),
                computeInt(ACCESS, evaluator),
                computeInternalName(NAME, evaluator),
                computeString(SIGNATURE, evaluator),
                computeInternalName(SUPER_NAME, evaluator),
                computeStringArray(INTERFACES, evaluator));
        // Visit the source.
        if (containsKey(SOURCE_FILE) || containsKey(SOURCE_DEBUG)) {
            classVisitor.visitSource(computeString(SOURCE_FILE, evaluator), computeString(SOURCE_DEBUG, evaluator));
        }
//        // Visit the module.
//        if (module != null) {
//            module.accept(classVisitor);
//        }
//        // Visit the nest host class.
//        if (nestHostClass != null) {
//            classVisitor.visitNestHost(nestHostClass);
//        }
//        // Visit the outer class.
//        if (outerClass != null) {
//            classVisitor.visitOuterClass(outerClass, outerMethod, outerMethodDesc);
//        }
        // Visit the annotations.
        BtList<BtAnnotation> annotations = getAnnotations();
        if (annotations != null) {
            for (BtAnnotation annotation : annotations) {
                annotation.accept(classVisitor, evaluator);
            }
        }
//        if (typeAnnotations != null) {
//            for (TypeAnnotationNodeEx typeAnnotation : typeAnnotations.values()) {
//                typeAnnotation.accept(
//                        classVisitor.visitTypeAnnotation(
//                                typeAnnotation.typeRef, typeAnnotation.typePath, typeAnnotation.desc, typeAnnotation.visible));
//            }
//        }
        // Visit the non standard attributes.
        BtList<BtNode> attributes = get(ATTRIBUTES);
        if (attributes != null) {
            for (BtNode node : attributes) {
                classVisitor.visitAttribute(node.computeAttribute(evaluator));
            }
        }
//        // Visit the nest members.
//        if (nestMembers != null) {
//            for (int i = 0, n = nestMembers.size(); i < n; ++i) {
//                classVisitor.visitNestMember(nestMembers.get(i));
//            }
//        }
//        // Visit the permitted subclasses.
//        if (permittedSubclasses != null) {
//            for (int i = 0, n = permittedSubclasses.size(); i < n; ++i) {
//                classVisitor.visitPermittedSubclass(permittedSubclasses.get(i));
//            }
//        }
        // Visit the inner classes.
        BtList<BtInnerClass> innerClasses = get(INNER_CLASSES);
        if (innerClasses != null) {
            for (BtInnerClass innerClass : innerClasses) {
                innerClass.accept(classVisitor, evaluator);
            }
        }
//        // Visit the record components.
//        if (recordComponents != null) {
//            for (int i = 0, n = recordComponents.size(); i < n; ++i) {
//                recordComponents.get(i).accept(classVisitor);
//            }
//        }
        // Visit the fields.
        BtList<BtField> fields = getFields();
        if (fields != null) {
            for (BtField field : fields) {
                field.accept(classVisitor, evaluator);
            }
        }

        // Visit the methods.
        BtList<BtMethod> methods = getMethods();
        if (methods != null) {
            for (BtMethod method : methods) {
                method.accept(classVisitor, evaluator);
            }
        }

        classVisitor.visitEnd();
    }
}
