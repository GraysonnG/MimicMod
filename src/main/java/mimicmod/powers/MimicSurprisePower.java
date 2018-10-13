package mimicmod.powers;

import com.megacrit.cardcrawl.actions.utility.*;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.badlogic.gdx.graphics.*;

public class MimicSurprisePower
  extends AbstractPower
{
	public static final String POWER_ID = "MimicSurprisePower";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("MimicSurprisePower");
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	public static final int ENERGY_AMT = 1;
    private boolean justApplied;
	
	
	public MimicSurprisePower(AbstractCreature owner) {
		this (owner, 1, false);
	}
	public MimicSurprisePower(AbstractCreature owner, final int amount) {
		this (owner, amount, false);
	}
	public MimicSurprisePower(AbstractCreature owner, final int amount, final boolean justApplied)
	{
		this.name = NAME;
		this.ID = "MimicSurprisePower";
		this.owner = owner;
		this.amount = amount;
		this.justApplied = justApplied;
        this.isTurnBased = true;
		this.description = DESCRIPTIONS[0];
		this.loadRegion("confusion");
		this.updateDescription();
	}
	
	@Override
	public void updateDescription()
	{
		this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
	}
	
    /*@Override
    public void atEndOfTurn(final boolean isPlayer) {
		if (this.justApplied) {
			this.justApplied = false;
			return;
		}
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, "MimicSurprisePower"));
    }*/
	
	@Override
    public void atEndOfTurn(boolean isPlayer) {
		if(isPlayer) {
			if (this.justApplied) {
				this.justApplied = false;
				return;
			}
			if (this.amount == 0) {
				AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, "MimicSurprisePower"));
			} else {
				AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
			}
		}
    }
	
	@Override
	public void onEnergyRecharge() {
		this.flash();
        AbstractDungeon.player.loseEnergy(this.amount);//AbstractDungeon.player.energy.energy
	}
	
}
