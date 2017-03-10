package gunlee01.toy.bcitest;

import scouter.repack.org.objectweb.asm.ClassReader;
import scouter.repack.org.objectweb.asm.ClassVisitor;
import scouter.repack.org.objectweb.asm.ClassWriter;
import scouter.repack.org.objectweb.asm.MethodVisitor;
import scouter.repack.org.objectweb.asm.Opcodes;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import static scouter.repack.org.objectweb.asm.Opcodes.ASM5;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 3. 2.
 */
public class TestTransformer implements ClassFileTransformer {

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		if (!"javax/servlet/http/HttpServlet".equals(className)) {
			return null;
		}

		System.out.println("[gun] loading : " + className);

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		ClassVisitor cv = new ClassVisitor(ASM5, cw) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				if(name.equals("service")) {
					return new ServiceMV(mv, name, desc);
				} else {
					return mv;
				}
			}
		};
		ClassReader cr = new ClassReader(classfileBuffer);
		cr.accept(cv, ClassReader.SKIP_FRAMES);

		return cw.toByteArray();
	}

	private class ServiceMV extends MethodVisitor implements Opcodes {
		String name;
		String desc;

		public ServiceMV(MethodVisitor mv, String name, String desc) {
			super(ASM5, mv);
			this.name = name;
			this.desc = desc;
		}

		@Override
		public void visitCode() {
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			mv.visitLdcInsn("[service()]:" + name);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

			mv.visitCode();
		}
	}
}
