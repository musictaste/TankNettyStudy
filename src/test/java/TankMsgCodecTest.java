import org.junit.Assert;
import org.junit.Test;

import com.mashibing.nettystudy.s13.TankMsg;
import com.mashibing.nettystudy.s13.TankMsgDecoder;
import com.mashibing.nettystudy.s13.TankMsgEncoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

public class TankMsgCodecTest {

	@Test
	public void testTankMsgEncoder() {
		TankMsg msg = new TankMsg(10, 10);
		EmbeddedChannel ch = new EmbeddedChannel(new TankMsgEncoder());
		ch.writeOutbound(msg);//�����ByteBuf
		
		ByteBuf buf = (ByteBuf)ch.readOutbound();//���Զ�������ByteBuf
		int x = buf.readInt();
		int y = buf.readInt();
		
		Assert.assertTrue(x == 10 && y == 10);
		buf.release();
		
	}
	
	@Test
	public void testTankMsgEncoder2() {
		ByteBuf buf = Unpooled.buffer();
		TankMsg msg = new TankMsg(10, 10);
		buf.writeInt(msg.x);
		buf.writeInt(msg.y);
		//����Ĵ��뽫TankMsgת��ΪByteBuf
		
		EmbeddedChannel ch = new EmbeddedChannel(new TankMsgEncoder(), new TankMsgDecoder());

		//ע�ⷽ���Ǵ�Decoder������潫ByteBufд����
		//ע��ByteBuf����TankMsgDecoder���������ͣ�������TankMsgEncoder���������ͣ�Ҫ���������TankMsg��
		//����������ֻ������TankMsgDecoder������Ľ����TankMsg
		//д��ʱ����Encoder������ʱ����Decoder
		//��ȻTankMsgEncoderû���ã�ΪʲôҪ�ӣ���Ϊ������Դ���û���ã���Ĳ��Դ�����ܻ���
		ch.writeInbound(buf.duplicate());

		TankMsg tm = (TankMsg)ch.readInbound();
		
		
		Assert.assertTrue(tm.x == 10 && tm.y == 10);
		
	}

}
