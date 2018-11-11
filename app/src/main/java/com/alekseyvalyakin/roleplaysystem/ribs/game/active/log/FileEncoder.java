package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log;

import com.alekseyvalyakin.roleplaysystem.data.sound.Encoder;
import com.alekseyvalyakin.roleplaysystem.data.sound.RawSamples;

import java.io.File;

import timber.log.Timber;

public class FileEncoder {
    private File in;
    private Encoder encoder;
    private long samples;
    private long cur;

    public FileEncoder(File in, Encoder encoder) {
        this.in = in;
        this.encoder = encoder;
    }

    public void encode() {
        cur = 0;
        Timber.d("encoding file");
        RawSamples rs = new RawSamples(in);
        samples = rs.getSamples();

        short[] buf = new short[1000];

        try {
            rs.open(buf.length);
            long len;
            while ((len = rs.read(buf)) > 0) {
                encoder.encode(buf);
                cur += len;
            }
        } finally {
            encoder.close();
            rs.close();
        }
    }

    public int getProgress() {
        return (int) (cur * 100 / samples);
    }
}