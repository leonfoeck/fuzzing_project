package de.uni_passau.fim.se2.st.fuzzing.instrumentation;

import static org.objectweb.asm.Opcodes.ASM9;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import com.google.common.base.Preconditions;
import de.uni_passau.fim.se2.st.fuzzing.coverage.CoverageTracker;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import org.objectweb.asm.*;

public class Agent {

  private Agent() {}

  public static void premain(String agentArgs, Instrumentation inst) {
    inst.addTransformer(new InstrumentationTransformer());
  }

  static class InstrumentationTransformer implements ClassFileTransformer {

    /**
     * {@inheritDoc}
     *
     * @author Leon Föckersperger
     */
    @Override
    public byte[] transform(
        ClassLoader loader,
        String className,
        Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain,
        byte[] classFileBuffer) {

      Preconditions.checkNotNull(classFileBuffer, "Cannot work with empty class!");

      if (isIgnoredClass(className)) {
        return classFileBuffer;
      }

      ClassReader cr = new ClassReader(classFileBuffer);
      ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
      InstrumentationAdapter ia = new InstrumentationAdapter(cw, className);
      cr.accept(ia, 0);
      return cw.toByteArray();
    }

    private boolean isIgnoredClass(String className) {
      return !className.startsWith("de/uni_passau/fim/se2/st/fuzzing/fuzztarget")
          || className.endsWith("Test");
    }
  }

  static class InstrumentationAdapter extends ClassVisitor {

    private final String className;

    InstrumentationAdapter(ClassWriter classWriter, String className) {
      super(ASM9, classWriter);
      this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(
        int access, String name, String descriptor, String signature, String[] exceptions) {
      MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

      return new MethodVisitor(ASM9, mv) {
        @Override
        public void visitLineNumber(int line, Label start) {
          super.visitLineNumber(line, start);
          CoverageTracker.trackLine(line, className);
          visitLdcInsn(line);
          visitLdcInsn(className);
          visitMethodInsn(
              INVOKESTATIC,
              Type.getInternalName(CoverageTracker.class),
              "trackLineVisit",
              "(ILjava/lang/String;)V",
              false);
        }
      };
    }
  }
}
