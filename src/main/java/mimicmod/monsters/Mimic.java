package mimicmod.monsters;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Mimic extends AbstractMonster{

	private static final String NAME = "Mimic";
	private static final String ID = "Mimic";

	private MimicType type;

	private enum MimicType {
		SMALL,
		MEDIUM,
		LARGE
	}

	public Mimic(){
		super(NAME, ID, 80, 0.0f, 0.0f, 100f, 100f, null);
		switch (AbstractDungeon.monsterRng.random(2)){
			case 0:
				this.type = MimicType.SMALL;
				break;
			case 1:
				this.type = MimicType.MEDIUM;
				break;
			case 2:
				this.type = MimicType.LARGE;
				break;
		}

		setHp(80);
	}

	@Override
	public void takeTurn() {

	}

	@Override
	protected void getMove(int i) {

	}
}
