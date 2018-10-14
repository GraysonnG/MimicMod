package mimicmod;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.ModSlider;
import basemod.interfaces.PostInitializeSubscriber;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class MimicModInit implements PostInitializeSubscriber {
	@Override
	public void receivePostInitialize() {
		MimicMod.load();

		ModPanel settingsPanel = new ModPanel();

		ModSlider spawnRateSlider = new ModSlider("Spawn Rate",
			0f,
			650f,
			100, "%", settingsPanel, (me) -> {
			int val = Math.round(me.value * me.multiplier);
			MimicMod.spawnRate = val;
			MimicMod.save();
		});

		spawnRateSlider.setValue((float)MimicMod.spawnRate / 100f);

		settingsPanel.addUIElement(spawnRateSlider);

		ModLabeledToggleButton eliteButton = new ModLabeledToggleButton("Mimics count as Elites", 300f, 500f, Color.WHITE, FontHelper.buttonLabelFont, MimicMod.areElites, settingsPanel, (me) -> {}, (me) -> {
			MimicMod.areElites = me.enabled;
			MimicMod.save();
		});
		settingsPanel.addUIElement(eliteButton);
		
		BaseMod.registerModBadge(ImageMaster.loadImage("img/mimicmod/modbadge.png"), "Mimic Mod", "Blank The Evil, The_Evil_Pickle", "Adds Mimics.", settingsPanel);
	}
}
