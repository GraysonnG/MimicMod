package mimicmod;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class MimicMod implements PostInitializeSubscriber {

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
}
