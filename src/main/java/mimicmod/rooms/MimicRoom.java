package mimicmod.rooms;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import mimicmod.monsters.Mimic;

public class MimicRoom extends EventRoom {



	public MimicRoom() {
		this.phase = RoomPhase.EVENT;
		this.mapSymbol = "MIM";
		this.mapImg = ImageMaster.MAP_NODE_TREASURE;
		this.mapImgOutline = ImageMaster.MAP_NODE_TREASURE_OUTLINE;
	}

	@Override
	public void onPlayerEntry() {
		this.playBGM(null);

	}

	public static Mimic.MimicType getRandomMimicType(){
		return Mimic.MimicType.SMALL;
	}

	public class MimicChest {
		public MimicChest(){

		}


	}


}
