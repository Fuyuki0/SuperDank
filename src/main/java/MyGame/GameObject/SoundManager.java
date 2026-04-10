package MyGame.GameObject;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

public class SoundManager {

        public static AudioClip dashingSound;
        public static AudioClip jumpingSound;

        public static AudioClip skill1Sound;
        public static AudioClip skill2Sound;
        public static AudioClip jumpingSlashSound;
        public static MediaPlayer ultimatePlayer;

        public static MediaPlayer enteringPlayer;

        public static AudioClip hitSound;

        public static AudioClip swordSound;
        public static AudioClip arrowSound;
        public static AudioClip fireballSound;
        public static AudioClip shurikenSound;
        public static AudioClip auraSound;
        public static AudioClip lightningSound;
        public static List<AudioClip> soundManagersLists = new ArrayList<>();

    public static List<AudioClip> getSoundManagersLists() {
        soundManagersLists.add(dashingSound);
        soundManagersLists.add(jumpingSound);
        soundManagersLists.add(skill1Sound);
        soundManagersLists.add(skill2Sound);
        soundManagersLists.add(jumpingSlashSound);
        soundManagersLists.add(hitSound);
        soundManagersLists.add(swordSound);
        soundManagersLists.add(arrowSound);
        soundManagersLists.add(fireballSound);
        soundManagersLists.add(shurikenSound);
        soundManagersLists.add(auraSound);
        soundManagersLists.add(lightningSound);
        return soundManagersLists;
    }

    public static void initSounds() {
            try {
                dashingSound = new AudioClip(SoundManager.class.getResource("/sfxAsset/dashing.wav").toExternalForm());
                jumpingSound = new AudioClip(SoundManager.class.getResource("/sfxAsset/jumping.wav").toExternalForm());

                skill1Sound = new AudioClip(SoundManager.class.getResource("/sfxAsset/skill1.wav").toExternalForm());
                skill2Sound = new AudioClip(SoundManager.class.getResource("/sfxAsset/skill2.wav").toExternalForm());
                jumpingSlashSound = new AudioClip(SoundManager.class.getResource("/sfxAsset/jumpingslash.wav").toExternalForm());
                try {
                    Media ultimateMedia = new Media(SoundManager.class.getResource("/sfxAsset/ultimate.wav").toExternalForm());
                    ultimatePlayer = new MediaPlayer(ultimateMedia);
                } catch (Exception e) {
                    System.out.println("ultimateplayer");
                }

                try {
                    Media enteringMedia = new Media(SoundManager.class.getResource("/sfxAsset/entering.wav").toExternalForm());
                    enteringPlayer = new MediaPlayer(enteringMedia);
                } catch (Exception e) {
                    System.out.println("enteringplayer");
                }

                hitSound = new AudioClip(SoundManager.class.getResource("/sfxAsset/hit.wav").toExternalForm());

                swordSound = new AudioClip(SoundManager.class.getResource("/sfxAsset/sword.wav").toExternalForm());
                arrowSound = new AudioClip(SoundManager.class.getResource("/sfxAsset/arrow.wav").toExternalForm());
                fireballSound = new AudioClip(SoundManager.class.getResource("/sfxAsset/fireball.wav").toExternalForm());
                shurikenSound = new AudioClip(SoundManager.class.getResource("/sfxAsset/shuriken.wav").toExternalForm());
                auraSound = new AudioClip(SoundManager.class.getResource("/sfxAsset/aura.wav").toExternalForm());
                lightningSound = new AudioClip(SoundManager.class.getResource("/sfxAsset/lightning.wav").toExternalForm());

                // mixer
                dashingSound.setVolume(1.6);
                jumpingSound.setVolume(2.5);
                skill1Sound.setVolume(0.4);
                skill2Sound.setVolume(0.6);
                jumpingSlashSound.setVolume(0.6);
                ultimatePlayer.setVolume(0.2);
                enteringPlayer.setVolume(0.2);
                hitSound.setVolume(64.0);
                swordSound.setVolume(0.4);
                arrowSound.setVolume(1.0);
                fireballSound.setVolume(0.3);
                shurikenSound.setVolume(1.1);
                auraSound.setVolume(0.8);
                lightningSound.setVolume(0.6);
            } catch (Exception e) {
                System.out.println("Warning: Could not load some SFX files!");
            }
        }
    }
