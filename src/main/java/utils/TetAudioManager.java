package main.java.utils;

import java.io.File;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javax.sound.sampled.*;

/**
 * TetAudioManager - Quản lý nhạc nền Tết
 */
public class TetAudioManager {
    private static TetAudioManager instance;
    private Clip audioClip;
    private boolean isPlaying = false;
    private Thread playThread;
    
    private static final String TET_AUDIO_PATH = "src/main/resources/assets/audio/huzani.wav";
    
    private TetAudioManager() {
    }
    
    public static TetAudioManager getInstance() {
        if (instance == null) {
            instance = new TetAudioManager();
        }
        return instance;
    }
    
    public void play() {
        if (isPlaying) {
            return;
        }
        
        playThread = new Thread(() -> {
            try {
                File audioFile = new File(TET_AUDIO_PATH);
                if (!audioFile.exists()) {
                    System.err.println("File nhạc Tết không tồn tại: " + audioFile.getAbsolutePath());
                    return;
                }
                
                // Load audio
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(new FileInputStream(audioFile))
                );
                
                // Get clip
                audioClip = AudioSystem.getClip();
                audioClip.open(audioStream);
                
                setVolume(0.5f);
                
                // Loop vô hạn
                audioClip.loop(Clip.LOOP_CONTINUOUSLY);
                audioClip.start();
                
                isPlaying = true;
                System.out.println("Đang phát nhạc Tết: " + audioFile.getName());
                
                // Giữ thread chạy
                while (isPlaying && audioClip.isRunning()) {
                    Thread.sleep(100);
                }
                
            } catch (UnsupportedAudioFileException e) {
                System.err.println("Định dạng audio không hỗ trợ. Vui lòng dùng file WAV.");
                System.err.println("   Chuyển đổi MP3 sang WAV tại: https://cloudconvert.com/mp3-to-wav");
            } catch (Exception e) {
                System.err.println("Không thể load nhạc Tết: " + e.getMessage());
            }
        });
        
        playThread.setDaemon(true);
        playThread.start();
    }
    
    public void stop() {
        isPlaying = false;
        if (audioClip != null) {
            try {
                audioClip.stop();
                audioClip.close();
            } catch (Exception e) {
                System.err.println("Lỗi khi dừng nhạc: " + e.getMessage());
            }
            audioClip = null;
            System.out.println("Đã dừng nhạc Tết");
        }
        if (playThread != null) {
            playThread.interrupt();
            playThread = null;
        }
    }
    
    public void pause() {
        if (audioClip != null && isPlaying) {
            audioClip.stop();
            System.out.println("Đã tạm dừng nhạc Tết");
        }
    }

    public void resume() {
        if (audioClip != null) {
            audioClip.start();
            System.out.println("Tiếp tục phát nhạc Tết");
        }
    }
    
    public void setVolume(float volume) {
        if (audioClip != null && audioClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            // Convert 0-1 to decibels
            float dB = (float) (Math.log(Math.max(0.0001, volume)) / Math.log(10.0) * 20.0);
            dB = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), dB));
            gainControl.setValue(dB);
        }
    }
    
    public boolean isPlaying() {
        return isPlaying;
    }
}
