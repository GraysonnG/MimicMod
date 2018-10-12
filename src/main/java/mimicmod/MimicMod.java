package mimicmod;

import basemod.BaseMod;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;

import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class MimicMod implements PostInitializeSubscriber, EditStringsSubscriber {

	public static final Logger logger = LogManager.getLogger(MimicMod.class.getName());
	public static final String VERSION = "0.0.0";

	public MimicMod(){
		BaseMod.subscribe(this);
	}

	public void initialize() {
		logger.info("Version: " + VERSION);
		new MimicMod();
	}

	@Override
	public void receivePostInitialize() {
		//3 encounter things
		
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
}
