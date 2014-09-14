package integration;

import base.Iterator;
import base.Next;
import base.Stop;
import scala.util.Either;
import scala.util.Right;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;

public class FileIteratorAdaptor implements Iterator<User> {

    private BufferedReader bufferedReader;

    public FileIteratorAdaptor(BufferedReader bufferedReader) {

        this.bufferedReader = bufferedReader;
    }

//    @Override
//    public boolean hasNext() {
//        return bufferedReader.readLine()
//    }


    @Override
    public Next<User> next() throws IOException {
        String s = bufferedReader.readLine();
        System.out.println(s);
        if (s == null) return new Stop();
        return new Next<>(new User(s.split(",")));

//        String s = bufferedReader.readLine();
//        System.out.println(s);
//        if (s == null) return new Stop;
//        return new User(s.split(","));
    }

    @Override
    public void close() throws IOException {
        bufferedReader.close();
    }

}
