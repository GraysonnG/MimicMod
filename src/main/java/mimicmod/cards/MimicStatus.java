package mimicmod.cards;

import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.monsters.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.*;
import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.core.*;
import basemod.abstracts.*;
import mimicmod.actions.MimicHandCardAction;

public class MimicStatus extends CustomCard
{
    public static final String ID = "MimicStatus";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;
    private static final int COST = 1;
    
    public MimicStatus() {
        super("MimicStatus", MimicStatus.NAME, "cards/replay/replayBetaCurse.png", 1, MimicStatus.DESCRIPTION, CardType.STATUS, CardColor.COLORLESS, CardRarity.COMMON, CardTarget.SELF);
        this.exhaust = true;
    }
    
    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        if (p.hasRelic("Medical Kit")) {
            this.useMedicalKit(p);
        } else {
			AbstractDungeon.actionManager.addToBottom(new MimicHandCardAction(AbstractDungeon.player, true));
		}
    }
    
    @Override
    public void triggerWhenDrawn() {
        if (AbstractDungeon.player.hasPower("Evolve") && !AbstractDungeon.player.hasPower("No Draw")) {
            AbstractDungeon.player.getPower("Evolve").flash();
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, AbstractDungeon.player.getPower("Evolve").amount));
        }
    }
    
    @Override
    public AbstractCard makeCopy() {
        return new MimicStatus();
    }
    
    @Override
    public void upgrade() {
    }
    
    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("MimicStatus");
        NAME = MimicStatus.cardStrings.NAME;
        DESCRIPTION = MimicStatus.cardStrings.DESCRIPTION;
    }
}
