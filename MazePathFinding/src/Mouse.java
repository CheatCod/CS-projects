
public class Mouse {
	int posX, posY;
	public Mouse(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}
	void moveUp() {
		this.posY-=1;
	}
	void moveDown() {
		this.posY-=1;
	}
}
