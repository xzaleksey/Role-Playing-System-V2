package com.alekseyvalyakin.roleplaysystem.data.sound;

import android.annotation.TargetApi;
import android.media.MediaFormat;

import java.io.File;

import timber.log.Timber;

@TargetApi(21)
public class Format3GP extends MuxerMP4 {

    public Format3GP(EncoderInfo info, File out) {
        MediaFormat format = new MediaFormat();

        // for high bitrate AMR_WB
        {
//            final int kBitRates[] = {6600, 8850, 12650, 14250, 15850, 18250, 19850, 23050, 23850};

//            format.setString(MediaFormat.KEY_MIME, "audio/amr-wb");
//            format.setInteger(MediaFormat.KEY_SAMPLE_RATE, info.sampleRate);
//            format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, info.channels);
//            format.setInteger(MediaFormat.KEY_BIT_RATE, 23850); // set maximum
        }

        // for low bitrate, AMR_NB
        {
//            final int kBitRates[] = {4750, 5150, 5900, 6700, 7400, 7950, 10200, 12200};

            format.setString(MediaFormat.KEY_MIME, "audio/3gpp");
            format.setInteger(MediaFormat.KEY_SAMPLE_RATE, info.getSampleRate()); // 8000 only supported
            format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, info.getChannels());
            format.setInteger(MediaFormat.KEY_BIT_RATE, 12200); // set maximum
        }

        create(info, format, out);
    }

    public static class FileEncoder {
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
}
