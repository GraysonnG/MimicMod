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
	
	//Shoutout to Anthony for implementing use of eliteTrigger so we don't have to fuck around patching relics as much.
	
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
