package eu.asdtech.tradelistener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import eu.asdtech.tradelistener.models.Trade;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TradeListenerLoggingAndSoundImpl implements TradeListener {

	private Clip clip = null;
	private long lastSoundPlayedAt = 0;

	public TradeListenerLoggingAndSoundImpl() {
		InputStream is = App.class.getClass().getResourceAsStream("/sounds/notification.wav");
		if (is == null) {
			log.error("Could not open notification sound");
			return;
		}
		try {
			clip = AudioSystem.getClip();
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(is);
			clip.open(audioInputStream);
		} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
			log.error("Could not prepare audio subsystem. Notification sounds are disabled");
		}
	}

	private void playSound() {
		// only play once every 5 seconds
		if (clip == null || System.currentTimeMillis() <= lastSoundPlayedAt + 5 * 1000) {
			return;
		}

		clip.setMicrosecondPosition(0);
		clip.start();
		lastSoundPlayedAt = System.currentTimeMillis();
	}

	@Override
	public void onNewTrade(Trade trade) {
		log.info("{}", trade);
		playSound();
	}

}
