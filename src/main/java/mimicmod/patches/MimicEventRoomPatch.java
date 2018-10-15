package mimicmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.rooms.*;

import mimicmod.MimicMod;
import mimicmod.rooms.MimicRoom;

@SpirePatch(clz=AbstractDungeon.class, method = "generateRoom")
public class MimicEventRoomPatch {
	public static SpireReturn<AbstractRoom> Prefix(final AbstractDungeon __Instance, final EventHelper.RoomResult roomType) {
		if (roomType == EventHelper.RoomResult.TREASURE && AbstractDungeon.mapRng.random(99) <= (MimicMod.eventSpawnRate - 1)) {
			return SpireReturn.Return(new MimicRoom()); 
		}
		return SpireReturn.Continue();
	}
}
