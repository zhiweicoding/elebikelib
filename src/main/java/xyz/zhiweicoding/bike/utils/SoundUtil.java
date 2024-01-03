package xyz.zhiweicoding.bike.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Slf4j
public class SoundUtil {
    public static MusicInfo getMusicInfo(String urlStr) {
        MusicInfo musicInfo = new MusicInfo();
        File source = new File(urlStr);
        commonTurn(source, musicInfo);
        return musicInfo;
    }

    public static MusicInfo getMusicInfo(File urlFile) {
        MusicInfo musicInfo = new MusicInfo();
        commonTurn(urlFile, musicInfo);
        return musicInfo;
    }

    private static void commonTurn(File urlFile, MusicInfo musicInfo) {
        try {
            AudioFile read = AudioFileIO.read(urlFile);
            AudioHeader audioHeader = read.getAudioHeader();
            int trackLength = audioHeader.getTrackLength();
            int minInt = trackLength / 60;
            int secondInt = trackLength % 60;
            String trackLengthBeauty = minInt + ":" + secondInt;
            String encodingType = audioHeader.getEncodingType();
            musicInfo.setMusicLengthBeaty(trackLengthBeauty);
            musicInfo.setMusicType(encodingType);
            musicInfo.setMusicName(urlFile.getName());
            musicInfo.setMusicLength((long) trackLength);
        } catch (IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException |
                 CannotReadException e) {
            log.error("获取音乐信息失败,{}", e.getMessage(), e);
        }

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MusicInfo implements Serializable {
        @Serial
        private static final long serialVersionUID = 1112311L;
        private long musicLength;
        private String musicLengthBeaty;
        private String musicName;
        private String musicType;
    }
}
