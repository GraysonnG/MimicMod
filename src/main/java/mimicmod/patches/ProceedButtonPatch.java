package mimicmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import mimicmod.rooms.MimicRoom;

@SuppressWarnings("unused")
public class ProceedButtonPatch {
	@SpirePatch(clz=ProceedButton.class, method = "update")
	public static class MimicRoomSkip {
		@SpireInsertPatch(
			rloc = 24,
			localvars = {"currentRoom"}
		)
		public static void Insert(ProceedButton __instance, AbstractRoom currentRoom) {
			if(currentRoom instanceof MimicRoom) {
				currentRoom.phase = AbstractRoom.RoomPhase.COMPLETE;
				AbstractDungeon.closeCurrentScreen();
				AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
				AbstractDungeon.dungeonMapScreen.open(false);
			}
		}
	}
}
