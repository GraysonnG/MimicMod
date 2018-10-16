package mimicmod.rooms;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BlackStar;
import com.megacrit.cardcrawl.relics.PreservedInsect;
import com.megacrit.cardcrawl.relics.Sling;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import com.megacrit.cardcrawl.rewards.chests.LargeChest;
import com.megacrit.cardcrawl.rewards.chests.MediumChest;
import com.megacrit.cardcrawl.rewards.chests.SmallChest;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;

import basemod.ReflectionHacks;
import mimicmod.MimicMod;
import mimicmod.monsters.Mimic;

public class MimicRoom extends AbstractRoom {

	public MimicChest chest;

	public MimicRoom() {
		this.phase = RoomPhase.INCOMPLETE;
		this.mapSymbol = "MIM";
		this.mapImg = ImageMaster.MAP_NODE_TREASURE;
		this.mapImgOutline = ImageMaster.MAP_NODE_TREASURE_OUTLINE;
		this.eliteTrigger = MimicMod.areElites;
	}

	@Override
	public void onPlayerEntry() {
		this.playBGM(null);
		chest = new MimicChest(getRandomMimicType());
		AbstractDungeon.overlayMenu.proceedButton.show();
	}

	public static Mimic.MimicType getRandomMimicType(){
		if(AbstractDungeon.bossCount < 1)
			return Mimic.MimicType.SMALL;
		else if(AbstractDungeon.bossCount == 1)
			return Mimic.MimicType.MEDIUM;
		else if(AbstractDungeon.bossCount == 2)
			return Mimic.MimicType.LARGE;
		else {
			switch(AbstractDungeon.miscRng.random(2)) {
			case 0:
				return Mimic.MimicType.SMALL;
			case 1:
				return Mimic.MimicType.MEDIUM;
			default:
				return Mimic.MimicType.LARGE;
			}
		}
	}

	@Override
	public void update(){
		super.update();
		if(chest != null && this.phase != RoomPhase.COMBAT){
			chest.update();
			if((chest.hb.hovered && InputHelper.justClickedLeft) || CInputActionSet.select.isJustPressed()) {
				AbstractDungeon.overlayMenu.proceedButton.hide();
				if (this.monsters == null) {
					this.monsters = AbstractDungeon.getMonsterForRoomCreation();
				}
				this.monsters.monsters.clear();
				this.monsters.add(new Mimic(chest.type));
				this.monsters.init();
				this.addGoldToRewards(AbstractDungeon.eventRng.random(30, 40));
				this.addRelicToRewards(AbstractDungeon.returnRandomRelic(MimicMod.getRelicTierFromMimicType(chest.type)));
				for(AbstractRelic relic : AbstractDungeon.player.relics){
					relic.onChestOpen(false);
					if (MimicMod.areElites) {
						if (relic.relicId.equals(BlackStar.ID)) {
							this.addNoncampRelicToRewards(this.returnRandomRelicTier());
							ReflectionHacks.setPrivate((Object)relic, (Class)AbstractRelic.class, "pulse", (Object)true);
				            relic.beginPulse();
						}
					}
				}
				for(AbstractRelic relic : AbstractDungeon.player.relics){
					relic.onChestOpenAfter(false);
				}
				enterCombat();
			}
		}
	}
	private AbstractRelic.RelicTier returnRandomRelicTier() {
        int roll = AbstractDungeon.relicRng.random(0, 99);
        if (ModHelper.isModEnabled("Elite Swarm")) {
            roll += 10;
        }
        if (roll < 50) {
            return AbstractRelic.RelicTier.COMMON;
        }
        if (roll > 82) {
            return AbstractRelic.RelicTier.RARE;
        }
        return AbstractRelic.RelicTier.UNCOMMON;
    }
	
	public void enterCombat() {
		AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
		AbstractDungeon.getCurrRoom().monsters.init();
		AbstractRoom.waitTimer = 0.1f;
		AbstractDungeon.player.preBattlePrep();
		AbstractDungeon.effectsQueue.add(new SmokeBombEffect(this.chest.hb.cX, this.chest.hb.y));
		CardCrawlGame.sound.play("INTIMIDATE");
	}

	@Override
	public void render(SpriteBatch sb){
		super.render(sb);
		if(chest != null && this.phase != RoomPhase.COMBAT){
			chest.render(sb);
			chest.hb.render(sb);
		}
	}

	@Override
	public AbstractCard.CardRarity getCardRarity(int roll) {
		int rareRate;
		if (AbstractDungeon.player.hasRelic("Nloth's Gift")) {
			rareRate = 9;
		}
		else {
			rareRate = 3;
		}
		if (roll < rareRate) {
			if (AbstractDungeon.player.hasRelic("Nloth's Gift") && roll > 3) {
				AbstractDungeon.player.getRelic("Nloth's Gift").flash();
			}
			return AbstractCard.CardRarity.RARE;
		}
		if (roll < 40) {
			return AbstractCard.CardRarity.UNCOMMON;
		}
		return AbstractCard.CardRarity.COMMON;
	}

	public class MimicChest {
		Hitbox hb;
		Texture img;
		Mimic.MimicType type;

		public MimicChest(Mimic.MimicType type){
			this.type = type;
			switch (type){
				case SMALL:
					(this.hb = new Hitbox(256.0f * Settings.scale, 200.0f * Settings.scale)).move(SmallChest.CHEST_LOC_X, SmallChest.CHEST_LOC_Y - 150.0f * Settings.scale);
					img = ImageMaster.loadImage("images/npcs/smallChest.png");
					break;
				case MEDIUM:
					(this.hb = new Hitbox(256.0f * Settings.scale, 270.0f * Settings.scale)).move(MediumChest.CHEST_LOC_X, MediumChest.CHEST_LOC_Y - 90.0f * Settings.scale);
					img = ImageMaster.loadImage("images/npcs/mediumChest.png");
					break;
				case LARGE:
					(this.hb = new Hitbox(340.0f * Settings.scale, 200.0f * Settings.scale)).move(LargeChest.CHEST_LOC_X, LargeChest.CHEST_LOC_Y - 120.0f * Settings.scale);
					img = ImageMaster.loadImage("images/npcs/largeChest.png");
					break;
			}
		}

		public void update(){
			this.hb.update();
		}

		public void render(SpriteBatch sb){
			sb.setColor(Color.WHITE);
			sb.draw(this.img,
				AbstractChest.CHEST_LOC_X - 256.0f,
				AbstractChest.CHEST_LOC_Y - 256.0f + AbstractDungeon.sceneOffsetY,
				256.0f, 256.0f, 512.0f, 512.0f, Settings.scale, Settings.scale, 0f, 0, 0, 512, 512, false, false);
		}
	}
}
