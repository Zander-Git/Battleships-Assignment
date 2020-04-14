package Model;


public class BoardModel {
	
	public static final int BOARD_DIMENSION = 10;
    private CellModel[][] cells;
	
    public BoardModel() {

        cells = new CellModel[BOARD_DIMENSION][BOARD_DIMENSION];
        // populates the squares array
        for (int i = 0; i < BOARD_DIMENSION; i++) {
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                cells[i][j] = new CellModel(i, j);
            }
        }
        System.out.println(getCell(1, 1+4).getX());
    }
	
    public CellModel getCell(int x, int y) {
        return cells[x][y];
    }
}
