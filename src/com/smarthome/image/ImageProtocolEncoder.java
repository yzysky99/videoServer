package com.smarthome.image;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class ImageProtocolEncoder extends ProtocolEncoderAdapter {
	private final String charset;
	public ImageProtocolEncoder(String charset) {
		this.charset = charset;
	}

	public void encode(IoSession session, Object message,
					   ProtocolEncoderOutput out) throws Exception {
		System.out.println("encode: " + message);
		
//		ByteBuffer byteBuffer = null;
//		if (byteBuffer != null) {
//			byteBuffer.flip();
//			IoBuffer ioBuf = IoBuffer.wrap(byteBuffer);
//			out.write(ioBuf);
//		}
	}

	public void dispose() throws Exception {
		
	}
}
