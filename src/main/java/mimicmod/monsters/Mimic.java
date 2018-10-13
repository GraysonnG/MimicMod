package mimicmod.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAndDeckAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.EntanglePower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

import mimicmod.cards.MimicStatus;
import mimicmod.powers.MimicSurprisePower;

public class Mimic extends AbstractMonster{

	private static final String ID = "Mimic";
    private static final MonsterStrings monsterStrings;
	private static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final int[] HP = {80, 120, 250};
    private static final int HP_DV = 2;
    private static final int A_HP = 15;
	private static final int[] LV_ARMOR_MAX = {6, 10, 15};
	private static final int[] LV_ARMOR_GAIN = {4, 5, 5};
	private static final int[] LV_DOUBLESTRIKE_DMG = {2, 2, 3};
	private static final int A_DOUBLESTRIKE_DMG = 1;
	private static final int[] LV_BIGSTRIKE_DMG = {9, 11, 13};
	private static final int A_BIGSTRIKE_DMG = 2;
	private static final int[] LV_STR = {1, 1, 2};
	private static final int[] LV_THORN = {1, 2, 2};
	private static final int[] LV_DAZE = {1, 2, 2};
	private static final int[] LV_MAD = {2, 3, 3};
	private int lv;
    private int lid_armor_gain;
    private int lid_armor_max;
    private int lid_artifact;
    private int doubleStrike_dmg;
    private int doubleStrike_slime;
    private int bigHit_dmg;
    private int selfBuff_str;
    private int selfBuff_thrns;
    private int boo_surprise;
    private int boo_dazed;
	private int mad_duration;
    private static final byte CLOSE_LID = 1;
    private static final byte DOUBLE_STRIKE = 2;
    private static final byte BLADE_MIMICRY = 3;
    private static final byte MAD_MIMICRY = 4;
    private static final byte BIG_HIT = 5;
    private static final byte BOO = 6;
    private boolean firstMove;

	private MimicType type;

	public enum MimicType {
		SMALL,
		MEDIUM,
		LARGE
	}

	public Mimic(MimicType mimType){
		super(NAME, ID, 80, 0.0f, 0.0f, 200f, 200f, null);
		int lv = 0;

		switch(mimType){
			case SMALL:
				lv = 0;
				this.setImage(ImageMaster.loadImage("img/mimicmod/monsters/mimicSmall.png"),295f, 153f);
				break;
			case MEDIUM:
				lv = 1;
				this.setImage(ImageMaster.loadImage("img/mimicmod/monsters/mimicMedium.png"), 273f, 252f);
				break;
			case LARGE:
				lv = 2;
				this.setImage(ImageMaster.loadImage("img/mimicmod/monsters/mimicLarge.png"), 453f, 317f);
				break;
		}

		this.dialogX = 0f;//SET THESE LATER
		this.dialogY = 0f;

		this.lid_artifact = 1;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(HP[lv] - HP_DV + A_HP, HP[lv] - HP_DV + A_HP);
			if (this.lv > 0) {
				this.lid_artifact = 2;
			}
        }
        else {
            this.setHp(HP[lv] - HP_DV, HP[lv] - HP_DV);
        }
        if (AbstractDungeon.ascensionLevel >= 3) {
			this.doubleStrike_dmg = LV_DOUBLESTRIKE_DMG[lv] + A_DOUBLESTRIKE_DMG;
            this.bigHit_dmg = LV_BIGSTRIKE_DMG[lv] + A_BIGSTRIKE_DMG;
        }
        else {
            this.doubleStrike_dmg = LV_DOUBLESTRIKE_DMG[lv];
            this.bigHit_dmg = LV_BIGSTRIKE_DMG[lv];
        }
		this.lid_armor_gain = LV_ARMOR_GAIN[lv];
		this.lid_armor_max = LV_ARMOR_MAX[lv];
		this.mad_duration = LV_MAD[lv];
		this.boo_dazed = LV_DAZE[lv];
		this.selfBuff_str = LV_STR[lv];
		this.selfBuff_thrns = LV_THORN[lv];
        this.damage.add(new DamageInfo(this, this.doubleStrike_dmg));
        this.damage.add(new DamageInfo(this, this.bigHit_dmg));
		this.boo_surprise = 2;
	}


	public void setImage(Texture img, float hb_w, float hb_h){
		this.img = img;
		this.hb_w = hb_w * Settings.scale;
		this.hb_h = hb_h * Settings.scale;
		this.hb = new Hitbox(this.hb_w, this.hb_h);
		this.healthHb = new Hitbox(this.hb_w, 72f * Settings.scale);
		this.refreshHitboxLocation();
		this.refreshIntentHbLocation();
	}

    @Override
    public void usePreBattleAction() {
		AbstractPower offGuard = new EntanglePower(AbstractDungeon.player);
		offGuard.name = Mimic.DIALOG[0];
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, offGuard));
		this.boo_surprise = AbstractDungeon.player.energy.energy - 1;
    }

	private void setMoveNow(byte nextTurn) {
		switch (nextTurn) {
            case CLOSE_LID: {
				this.setMove(MOVES[0], nextTurn, Intent.BUFF);
				break;
			}
            case DOUBLE_STRIKE: {
				this.setMove(nextTurn, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 2, true);
				break;
			}
            case BLADE_MIMICRY: {
				this.setMove(MOVES[1], nextTurn, Intent.BUFF);
				break;
			}
			case MAD_MIMICRY: {
				this.setMove(MOVES[2], nextTurn, Intent.DEBUFF);		
				break;
			}
			case BIG_HIT: {
				this.setMove(nextTurn, Intent.ATTACK, this.damage.get(1).base);
				break;
			}
			case BOO: {
				this.setMove(MOVES[3], nextTurn, Intent.STRONG_DEBUFF);		
				break;
			}
			default: {
				this.setMove(nextTurn, Intent.NONE);	
				break;	
			}
		}
	}
	
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case CLOSE_LID: {
				if (!this.hasPower(PlatedArmorPower.POWER_ID) || this.getPower(PlatedArmorPower.POWER_ID).amount + this.lid_armor_gain <= this.lid_armor_max) {
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PlatedArmorPower(this, this.lid_armor_gain), this.lid_armor_gain));
				} else if (this.getPower(PlatedArmorPower.POWER_ID).amount < this.lid_armor_max) {
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PlatedArmorPower(this, this.lid_armor_max - this.getPower(PlatedArmorPower.POWER_ID).amount), this.lid_armor_max - this.getPower(PlatedArmorPower.POWER_ID).amount));
				}
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, this.lid_artifact), this.lid_artifact));
				AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
                break;
            }
            case DOUBLE_STRIKE: {
				AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                for (int i = 0; i < 2; ++i) {
					AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-25.0f, 25.0f) * Settings.scale, AbstractDungeon.player.hb.cY + MathUtils.random(-25.0f, 25.0f) * Settings.scale, Color.GOLD.cpy()), 0.0f));
					AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                }
				AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAndDeckAction(new Slimed()));
				AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
                break;
            }
            case BIG_HIT: {
				AbstractDungeon.actionManager.addToBottom(new AnimateHopAction(this));
				AbstractDungeon.actionManager.addToBottom(new WaitAction(0.2f));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
				AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
                break;
            }
            case BLADE_MIMICRY: {
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ThornsPower(this, this.selfBuff_thrns), this.selfBuff_thrns));
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.selfBuff_str), this.selfBuff_str));
				AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
                break;
            }
            case MAD_MIMICRY: {
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.mad_duration, true), this.mad_duration));
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.mad_duration, true), this.mad_duration));
				AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAndDeckAction(new MimicStatus()));
				AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
                break;
            }
            case BOO: {
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new MimicSurprisePower(AbstractDungeon.player, this.boo_surprise, false), this.boo_surprise));
				AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Dazed(), this.boo_dazed));
				AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
                break;
            }
        }
    }

    @Override
    protected void getMove(final int num) {
		
		if (this.lastMove(CLOSE_LID)) {
			this.setMoveNow(DOUBLE_STRIKE);
			return;
		}
		if (this.lastMove(DOUBLE_STRIKE)) {
			if (num > 66 || GameActionManager.turn < 5) {
				this.setMoveNow(BLADE_MIMICRY);
			} else {
				this.setMoveNow(MAD_MIMICRY);
			}
			return;
		}
		if (this.lastMove(BLADE_MIMICRY) || this.lastMove(MAD_MIMICRY)) {
			this.setMoveNow(BIG_HIT);
			return;
		}
		if (this.lastMove(BIG_HIT)) {
			this.setMoveNow(BOO);
			return;
		}
		this.setMoveNow(CLOSE_LID);
    }
	
	static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(Mimic.ID);
        NAME = Mimic.monsterStrings.NAME;
        MOVES = Mimic.monsterStrings.MOVES;
        DIALOG = Mimic.monsterStrings.DIALOG;
    }
}
