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
		ch.writeOutbound(msg);//结果是ByteBuf
		
		ByteBuf buf = (ByteBuf)ch.readOutbound();//所以读到的是ByteBuf
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
		//上面的代码将TankMsg转化为ByteBuf
		
		EmbeddedChannel ch = new EmbeddedChannel(new TankMsgEncoder(), new TankMsgDecoder());

		//注意方向：是从Decoder这个方面将ByteBuf写进来
		//注意ByteBuf符合TankMsgDecoder的数据类型，不符合TankMsgEncoder的数据类型（要求输入的是TankMsg）
		//所以责任链只经过了TankMsgDecoder，输出的结果是TankMsg
		//写的时候用Encoder，读的时候用Decoder
		//既然TankMsgEncoder没有用，为什么要加？因为这个测试代码没有用，别的测试代码可能会用
		ch.writeInbound(buf.duplicate());

		TankMsg tm = (TankMsg)ch.readInbound();
		
		
		Assert.assertTrue(tm.x == 10 && tm.y == 10);
		
	}

}
