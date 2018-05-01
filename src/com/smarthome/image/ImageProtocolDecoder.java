package com.smarthome.image;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class ImageProtocolDecoder extends CumulativeProtocolDecoder {
	
	private final String charset;
	public ImageProtocolDecoder(String charset) {
		this.charset = charset;
	}
	
	@Override
	protected boolean doDecode(IoSession session, IoBuffer buff,
							   ProtocolDecoderOutput out) throws Exception {
		System.out.println("doDecode limit: " + buff.limit() +" remaining: " + buff.remaining());
		
		return true;
	}


}
