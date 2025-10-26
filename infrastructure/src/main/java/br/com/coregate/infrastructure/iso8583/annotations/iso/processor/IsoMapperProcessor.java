package br.com.coregate.infrastructure.iso8583.annotations.iso.processor;

import br.com.coregate.infrastructure.iso8583.annotations.iso.IsoField;
import br.com.coregate.infrastructure.iso8583.annotations.iso.IsoMessage;
import br.com.coregate.infrastructure.iso8583.annotations.iso.IsoVarType;
import com.google.auto.service.AutoService;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;
import java.io.Writer;
import java.util.*;

@SupportedAnnotationTypes({
        "br.com.coregate.infrastructure.iso8583.annotations.iso.IsoMessage",
        "br.com.coregate.infrastructure.iso8583.annotations.iso.IsoField"
})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@AutoService(Processor.class)
public class IsoMapperProcessor extends AbstractProcessor {

    private Filer filer;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
        this.elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(IsoMessage.class)) {
            if (element.getKind() != ElementKind.CLASS) continue;

            TypeElement classElement = (TypeElement) element;
            IsoMessage messageAnnotation = classElement.getAnnotation(IsoMessage.class);
            String mti = messageAnnotation.mti();
            String className = classElement.getSimpleName().toString();
            String packageName = elementUtils.getPackageOf(classElement).getQualifiedName().toString();

            List<FieldSpec> fields = extractFields(classElement);

            try {
                generateMapper(packageName, className, mti, fields);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private static class FieldSpec {
        String name;
        int number;
        int length;
        IsoVarType varType;

        FieldSpec(String name, int number, int length, IsoVarType varType) {
            this.name = name;
            this.number = number;
            this.length = length;
            this.varType = varType;
        }
    }

    private List<FieldSpec> extractFields(TypeElement classElement) {
        List<FieldSpec> fields = new ArrayList<>();
        for (Element enclosed : classElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.FIELD) {
                IsoField annotation = enclosed.getAnnotation(IsoField.class);
                if (annotation != null) {
                    fields.add(new FieldSpec(
                            enclosed.getSimpleName().toString(),
                            annotation.number(),
                            annotation.length(),
                            annotation.varType()
                    ));
                }
            }
        }
        fields.sort(Comparator.comparingInt(f -> f.number));
        return fields;
    }

    private void generateMapper(String packageName, String className, String mti, List<FieldSpec> fields) throws Exception {
        String mapperClassName = className + "Mapper";
        String fullClassName = packageName + "." + mapperClassName;

        JavaFileObject builderFile = filer.createSourceFile(fullClassName);
        try (Writer writer = builderFile.openWriter()) {
            writer.write("package " + packageName + ";\n\n");
            writer.write("import java.util.*;\n\n");
            writer.write("public class " + mapperClassName + " {\n\n");

            // decode()
            writer.write("    public static " + className + " decode(String iso) {\n");
            writer.write("        " + className + " model = new " + className + "();\n");
            writer.write("        int offset = 0;\n");
            writer.write("        String mti = iso.substring(offset, offset + 4);\n");
            writer.write("        offset += 4;\n");
            writer.write("        String bitmapHex = iso.substring(offset, offset + 16);\n");
            writer.write("        offset += 16;\n");
            writer.write("        BitSet bitmap = hexToBitSet(bitmapHex);\n");

            writer.write("        for (int i = 1; i <= 64; i++) {\n");
            writer.write("            if (!bitmap.get(i - 1)) continue;\n");

            for (FieldSpec f : fields) {
                writer.write("            if (i == " + f.number + ") {\n");
                if (f.varType == IsoVarType.FIXED) {
                    writer.write("                String val = iso.substring(offset, offset + " + f.length + ").trim();\n");
                    writer.write("                offset += " + f.length + ";\n");
                } else if (f.varType == IsoVarType.LLVAR) {
                    writer.write("                int len = Integer.parseInt(iso.substring(offset, offset + 2));\n");
                    writer.write("                offset += 2;\n");
                    writer.write("                String val = iso.substring(offset, offset + len);\n");
                    writer.write("                offset += len;\n");
                } else if (f.varType == IsoVarType.LLLVAR) {
                    writer.write("                int len = Integer.parseInt(iso.substring(offset, offset + 3));\n");
                    writer.write("                offset += 3;\n");
                    writer.write("                String val = iso.substring(offset, offset + len);\n");
                    writer.write("                offset += len;\n");
                }
                writer.write("                model.set" + capitalize(f.name) + "(val);\n");
                writer.write("            }\n");
            }

            writer.write("        }\n");
            writer.write("        return model;\n");
            writer.write("    }\n\n");

            // encode()
            writer.write("    public static String encode(" + className + " model) {\n");
            writer.write("        BitSet bitmap = new BitSet(64);\n");
            for (FieldSpec f : fields) {
                writer.write("        bitmap.set(" + (f.number - 1) + ");\n");
            }
            writer.write("        StringBuilder sb = new StringBuilder();\n");
            writer.write("        sb.append(\"" + mti + "\");\n");
            writer.write("        sb.append(bitSetToHex(bitmap));\n");

            for (FieldSpec f : fields) {
                writer.write("        {\n");
                if (f.varType == IsoVarType.FIXED) {
                    writer.write("            sb.append(padRight(model.get" + capitalize(f.name) + "(), " + f.length + "));\n");
                } else if (f.varType == IsoVarType.LLVAR) {
                    writer.write("            String val = model.get" + capitalize(f.name) + "();\n");
                    writer.write("            if (val == null) val = \"\";\n");
                    writer.write("            sb.append(String.format(\"%02d\", val.length()));\n");
                    writer.write("            sb.append(val);\n");
                } else if (f.varType == IsoVarType.LLLVAR) {
                    writer.write("            String val = model.get" + capitalize(f.name) + "();\n");
                    writer.write("            if (val == null) val = \"\";\n");
                    writer.write("            sb.append(String.format(\"%03d\", val.length()));\n");
                    writer.write("            sb.append(val);\n");
                }
                writer.write("        }\n");
            }

            writer.write("        return sb.toString();\n");
            writer.write("    }\n\n");

            // helpers
            writer.write("    private static String padRight(String s, int n) {\n");
            writer.write("        if (s == null) s = \"\";\n");
            writer.write("        return String.format(\"%-\" + n + \"s\", s);\n");
            writer.write("    }\n\n");

            writer.write("    private static String bitSetToHex(BitSet bitSet) {\n");
            writer.write("        byte[] bytes = new byte[8];\n");
            writer.write("        for (int i = 0; i < 64; i++) if (bitSet.get(i)) bytes[i / 8] |= 1 << (7 - (i % 8));\n");
            writer.write("        StringBuilder sb = new StringBuilder();\n");
            writer.write("        for (byte b : bytes) sb.append(String.format(\"%02X\", b));\n");
            writer.write("        return sb.toString();\n");
            writer.write("    }\n\n");

            writer.write("    private static BitSet hexToBitSet(String hex) {\n");
            writer.write("        BitSet bitSet = new BitSet(64);\n");
            writer.write("        for (int i = 0; i < 16; i++) {\n");
            writer.write("            int nibble = Character.digit(hex.charAt(i), 16);\n");
            writer.write("            for (int j = 0; j < 4; j++) if ((nibble & (1 << (3 - j))) != 0) bitSet.set(i * 4 + j);\n");
            writer.write("        }\n");
            writer.write("        return bitSet;\n");
            writer.write("    }\n");

            writer.write("}\n");
        }
    }

    private static String capitalize(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
