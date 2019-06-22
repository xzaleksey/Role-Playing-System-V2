package com.alekseyvalyakin.roleplaysystem.data.sound;

// based on http://soundfile.sapp.org/doc/WaveFormat/

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class FormatWAV implements Encoder {
    public static String FORMAT_NAME = ".wav";

    private int numSamples;
    private EncoderInfo info;
    private int bytesPerSample;
    private RandomAccessFile outFile;
    private ByteOrder order = ByteOrder.LITTLE_ENDIAN;


    public FormatWAV(EncoderInfo info, File out) {
        this.info = info;
        numSamples = 0;

        bytesPerSample = info.getBps() / 8;

        try {
            outFile = new RandomAccessFile(out, "rw");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        save();
    }

    public void save() {
        int subChunk1Size = 16;
        int subChunk2Size = numSamples * info.getChannels() * bytesPerSample;
        int chunkSize = 4 + (8 + subChunk1Size) + (8 + subChunk2Size);

        write("RIFF");
        write(chunkSize, order);
        write("WAVE");

        int byteRate = info.getSampleRate() * info.getChannels() * bytesPerSample;
        short audioFormat = 1; // PCM = 1 (i.e. Linear quantization)
        int blockAlign = bytesPerSample * info.getChannels();

        write("fmt ");
        write(subChunk1Size, order);
        write((short) audioFormat, order); //short
        write((short) info.getChannels(), order); // short
        write(info.getSampleRate(), order);
        write(byteRate, order);
        write((short) blockAlign, order); // short
        write((short) info.getBps(), order); // short

        write("data");
        write(subChunk2Size, order);
    }

    public void write(String str) {
        try {
            byte[] cc = str.getBytes(StandardCharsets.UTF_8);
            ByteBuffer bb = ByteBuffer.allocate(cc.length);
            bb.order(ByteOrder.BIG_ENDIAN);
            bb.put(cc);
            bb.flip();

            outFile.write(bb.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(int i, ByteOrder order) {
        ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
        bb.order(order);
        bb.putInt(i);
        bb.flip();

        try {
            outFile.write(bb.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(short i, ByteOrder order) {
        ByteBuffer bb = ByteBuffer.allocate(Short.SIZE / Byte.SIZE);
        bb.order(order);
        bb.putShort(i);
        bb.flip();

        try {
            outFile.write(bb.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void encode(@NonNull short[] buf) {
        numSamples += buf.length / info.getChannels();
        try {
            ByteBuffer bb = ByteBuffer.allocate(buf.length * (Short.SIZE / Byte.SIZE));
            bb.order(order);
            for (short aBuf : buf) bb.putShort(aBuf);
            bb.flip();
            outFile.write(bb.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            outFile.seek(0);
            save();
            outFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public EncoderInfo getInfo() {
        return info;
    }

}