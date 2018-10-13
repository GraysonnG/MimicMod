package mimicmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapGenerator;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import javassist.CtBehavior;
import mimicmod.rooms.MimicRoom;

import java.util.ArrayList;
import java.util.List;

@SpirePatch(clz=AbstractDungeon.class, method = "generateMap")
public class CreateMimicRoomPatch {
	@SpireInsertPatch(locator = Locator.class)
	public static void AddMimicsToMap(){
		List<MapRoomNode> chestNodes = new ArrayList<>();
		for(List<MapRoomNode> rows : AbstractDungeon.map) {
			for(MapRoomNode node : rows) {
				if(node.room instanceof TreasureRoom){
					chestNodes.add(node);
				}
			}
		}

		if(AbstractDungeon.mapRng.random(9) == 0) {
			for (MapRoomNode treasureRoom : chestNodes) {
				treasureRoom.setRoom(new MimicRoom());
			}
		}
	}

	private static class Locator extends SpireInsertLocator {


		@Override
		public int[] Locate(CtBehavior ctBehavior) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(MapGenerator.class, "toString");
			return LineFinder.findInOrder(ctBehavior, finalMatcher);
		}
	}
}
