package mimicmod;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import mimicmod.cards.MimicStatus;
import mimicmod.monsters.Mimic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class MimicMod implements PostInitializeSubscriber, EditStringsSubscriber, EditCardsSubscriber {

	public static final Logger logger = LogManager.getLogger(MimicMod.class.getName());
	public static final String VERSION = "0.1.2";

	public static int spawnRate = 25;
	public static int eventSpawnRate = 33;
	public static boolean areElites = false;

	public MimicMod(){
		BaseMod.subscribe(this);
		BaseMod.subscribe(new MimicModInit());
	}

	@SuppressWarnings("unused")
	public static void initialize() {
		logger.info("Version: " + VERSION);
		new MimicMod();
	}

	public static void save() {
		try{
			SpireConfig config = new SpireConfig("mimicmod", "settings");
			config.setInt("spawnRate", spawnRate);
			config.setInt("eventSpawnRate", eventSpawnRate);
			config.setBool("areElites", areElites);
			config.save();
		}catch(IOException | NumberFormatException e){
			e.printStackTrace();
		}
	}

	public static void load() {
		try{
			SpireConfig config = new SpireConfig("mimicmod", "settings");
			config.load();
			if (config.has("spawnRate")) {
				spawnRate = config.getInt("spawnRate");
			}
			if (config.has("eventSpawnRate")) {
				eventSpawnRate = config.getInt("eventSpawnRate");
			}
			if (config.has("areElites")) {
				areElites = config.getBool("areElites");
			}
		}catch(IOException | NumberFormatException e){
			e.printStackTrace();
		}
	}


	public static void clear() {
		spawnRate = 25;
		save();
	}

	@Override
	public void receiveEditCards() {
		BaseMod.addCard(new MimicStatus());
	}

	@Override
	public void receivePostInitialize() {

	}

	@Override
	public void receiveEditStrings() {
		String mStrings = Gdx.files.internal("localization/mimic/MimicMonsterStrings.json").readString(
        		String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(MonsterStrings.class, mStrings);
		String cStrings = Gdx.files.internal("localization/mimic/MimicCardStrings.json").readString(
        		String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cStrings);
		String pStrings = Gdx.files.internal("localization/mimic/MimicPowerStrings.json").readString(
        		String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, pStrings);
	}

	public static AbstractRelic.RelicTier getRelicTierFromMimicType(Mimic.MimicType type){
		switch (type){
			case SMALL:
				return AbstractRelic.RelicTier.COMMON;
			case MEDIUM:
				return AbstractRelic.RelicTier.UNCOMMON;
			case LARGE:
				return AbstractRelic.RelicTier.RARE;
				default:
					return AbstractRelic.RelicTier.COMMON;
		}
	}
}
