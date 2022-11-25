package com.tuling.nio.old;

import java.nio.ByteBuffer;

public class InternalInputBuffer {

    byte [] buf = new byte[8 * 1024];
    int pos = 0 ;
    int lastValid = 0 ;
    ByteBuffer readbuf = ByteBuffer.allocate(8192);
    /*public boolean fill(){
        int nRead = nioChannel.read(readbuf);
        if (nRead > 0 ){
            readbuf.get(buf,pos,nRead);
            lastValid = pos + nRead;
        }
        return nRead > 0 ;
    }*/
}
