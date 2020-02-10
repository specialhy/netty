package com.hy.nio;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/***
 * 字符编解码
 */
public class NioTest12 {
    public static void main(String[] args) throws Exception{
        String inputFile = "NioTest12_In.txt";
        String outputFile = "NioTest12_Out.txt";

        RandomAccessFile inputRandomAccessFile = new RandomAccessFile(inputFile,"r");
        RandomAccessFile outputRandomAccessFile = new RandomAccessFile(outputFile,"rw");

        long inputLength = new File(inputFile).length();

        FileChannel inputFileChannel = inputRandomAccessFile.getChannel();
        FileChannel outputFileChannel = outputRandomAccessFile.getChannel();

        //内存映射
        MappedByteBuffer inputData = inputFileChannel.map(FileChannel.MapMode.READ_ONLY,0,inputLength);

        System.out.println("============");

        Charset.availableCharsets().forEach((k,v) -> {
            System.out.println(k + "," + v);
        });

        System.out.println("============");

        Charset charset = Charset.forName("iso-8859-1");
        //decoder把字节数组转换为字符串 解码 主要是转换为utf-8
        CharsetDecoder decoder = charset.newDecoder();
        //encoder把字符串转换为字节数组 编码
        CharsetEncoder encoder = charset.newEncoder();
        //decoder把字节数组转换为字符串 解码
        CharBuffer charBuffer = decoder.decode(inputData);
        //encoder把字符串转换为字节数组 编码
        ByteBuffer outputData = encoder.encode(charBuffer);

        outputFileChannel.write(outputData);

        inputRandomAccessFile.close();
        outputRandomAccessFile.close();

        /***
         *ASCII(阿斯克码)  iso-8859-1   utf-8   utf-16   utf-16LE(小端)  utf-16BE(大端)  GBK  uncode(范围最大)
         *
         * 阿斯克码(美国信息交换标准代码)
         *   7bit来表示一个字符 共计可以表示128字符
         *   之后发现阿斯克码不够用
         * ISO-8859-1(完全兼容阿斯克码)
         *   8 bit表示一个字符(不会浪费,完全利用) 即用一个字节(byte)(8 bit)来表示一个字符,共计看可以表示256个字符
         *   发现不能满足中国和其他一些国家的语言
         *
         * gb2312(中国)
         *   2个字节表示一个汉字
         *   发现不能满足一些生僻字
         * gbk(gb2312的延生)
         *   包含的汉字数量完全大于gb2312
         * bg18030(gbk的延生)
         *   最全的文字
         * big5
         *   针对与繁体中文
         *
         * 每一个国家都搞了自己的一套标准,不行,国际标准化出现了,将全世界编码汇总
         *
         * unicode(统一采用两个字节来存储字符)
         *    发现存储容量膨胀,西方国家采用的是英文字符,原来一个字节可以存一个字符,现在必须要两个字节存储,原来1MB变为2MB
         *    不适合存储英文
         *utf出现
         *    unicode本身是一种编码方式(比如汉字我,使用两个字节表示),utf本身一个存储格式,utf都是unicode的实现方式之一
         *utf-16LE（little endian）小端
         *utf-16BE(big endian)大端
         *   Zero Width No-Brek Space(0宽度不换行的)实际不存在,会有一个相应的字符编码,文件开头会有BOM开头,如果文件开头是0xFRFF(BE),如果是0xFFFE(LE)
         *   由于utf-16是两个字节表示一个字符,用的也比较少
         *utf-8 变长的字节形式表示
         *   如果是一个英文，就用一个字节表示,如果是中文,就用三个字节表示,和阿斯克码是一模一样的,兼容于阿斯克码,utf-8没有BOM
         *BOM(Byte Order Mark)
         *    文件开头会有BOM开头,如果文件开头是0xFRFF(BE)utf-16BE(big endian),如果是0xFFFE(LE)utf-16LE（little endian）
         *
         */
    }
}
