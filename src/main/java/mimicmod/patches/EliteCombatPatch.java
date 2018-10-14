package mimicmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BlackStar;
import com.megacrit.cardcrawl.relics.PreservedInsect;
import com.megacrit.cardcrawl.relics.Sling;

import basemod.ReflectionHacks;
import mimicmod.MimicMod;
import mimicmod.rooms.MimicRoom;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class EliteCombatPatch {
	@SpirePatch(clz=AbstractPlayer.class, method = "applyStartOfCombatLogic")
	public static class StartCombatPatch {
		public static void Prefix(AbstractPlayer __Instance) {
			if (MimicMod.areElites && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom() instanceof MimicRoom) {
				for (AbstractRelic relic : __Instance.relics) {
					if (relic.relicId.equals(PreservedInsect.ID)) {
						relic.flash();
			            for (final AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
			                if (m.currentHealth > (int)(m.maxHealth * (0.75f))) {
			                    m.currentHealth = (int)(m.maxHealth * (0.75f));
			                    m.healthBarUpdatedEvent();
			                }
			            }
			            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(__Instance, relic));
					}
					if (relic.relicId.equals(Sling.ID)) {
						relic.flash();
			            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 2), 2));
			            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, relic));
					}
				}
			}
		}
	}
	@SpirePatch(clz=BlackStar.class, method = "onVictory")
	public static class BlackstarPatch {
		public static void Postfix(BlackStar __Instance) {
			if (MimicMod.areElites && AbstractDungeon.getCurrRoom() instanceof MimicRoom) {
				__Instance.flash();
				ReflectionHacks.setPrivate((Object)__Instance, (Class)AbstractRelic.class, "pulse", (Object)false);
	        }
		}
	}
}
