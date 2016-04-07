package com.dabaicong.program;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.caucho.hessian.io.Hessian2StreamingOutput;

public class pro {

	public static void main(String[] args) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Hessian2StreamingOutput hos = new Hessian2StreamingOutput(bos);
		byte[] testByte = new byte[4090];
		for (int i = 0; i < 4090; i++) {
			testByte[i] = (byte) i;
		}
		hos.writeObject(testByte);
	}

}
