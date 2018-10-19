package mimicmod;

import basemod.*;
import basemod.interfaces.PostInitializeSubscriber;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class MimicModInit implements PostInitializeSubscriber {

	public static final float SLIDER_X_POS = 450f;

	@Override
	public void receivePostInitialize() {
		MimicMod.load();

		ModPanel settingsPanel = new ModPanel();

		ModLabel spawnRateLabel = new ModLabel("Spawn Rate in Chest Rooms.",
			400f,
			700f, settingsPanel, (me) -> {});

		ModSlider spawnRateSlider = new ModSlider("",
			SLIDER_X_POS,
			665f,
			100, "%", settingsPanel, (me) -> {
			int val = Math.round(me.value * me.multiplier);
			MimicMod.spawnRate = val;
			MimicMod.save();
		});

		ModLabel eventSpawnRateLabel = new ModLabel("Spawn Rate in ? Rooms.",
			400f,
			600f, settingsPanel, (me) -> {});

		ModSlider eventSpawnRateSlider = new ModSlider("",
			SLIDER_X_POS,
			565f,
			100, "%", settingsPanel, (me) -> {
			int val = Math.round(me.value * me.multiplier);
			MimicMod.eventSpawnRate = val;
			MimicMod.save();
		});

		ModLabeledToggleButton eliteButton = new ModLabeledToggleButton(
			"Relic effects count Mimics as Elites",
			350f,
			500f, Color.WHITE, FontHelper.buttonLabelFont, MimicMod.areElites, settingsPanel, (me) -> {}, (me) -> {
			MimicMod.areElites = me.enabled;
			MimicMod.save();
		});

		eventSpawnRateSlider.setValue((float)MimicMod.eventSpawnRate / 100f);
		spawnRateSlider.setValue((float)MimicMod.spawnRate / 100f);

		settingsPanel.addUIElement(spawnRateLabel);
		settingsPanel.addUIElement(spawnRateSlider);
		settingsPanel.addUIElement(eventSpawnRateLabel);
		settingsPanel.addUIElement(eventSpawnRateSlider);
		settingsPanel.addUIElement(eliteButton);
		
		BaseMod.registerModBadge(ImageMaster.loadImage("img/mimicmod/modbadge.png"), "Mimic Mod", "Blank The Evil, The_Evil_Pickle", "Adds Mimics.", settingsPanel);
	}
}
