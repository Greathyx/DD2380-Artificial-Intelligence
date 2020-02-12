import java.util.Vector;

public class Player {

    private static final int DEPTH = 4;
    private static final int NaN = Integer.MAX_VALUE;
    private int currentPlayer;

    /**
     * Performs a move
     *
     * @param gameState the current state of the board
     * @param deadline  time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState gameState, final Deadline deadline) {

        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);

        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }

        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */

//        Random random = new Random();
//        return nextStates.elementAt(random.nextInt(nextStates.size()));

        currentPlayer = gameState.getNextPlayer();
        int nextPlayer;
        if (currentPlayer == Constants.CELL_X)
            nextPlayer = Constants.CELL_O;
        else
            nextPlayer = Constants.CELL_X;

        int maxHeuristicVal = -NaN;
        int maxValIndex = 0;
        int heuristicVal;

        int alpha = -NaN;
        int beta = NaN;

        for (int i = 0; i < nextStates.size(); i++) {
            heuristicVal = alphaBeta(nextStates.elementAt(i), DEPTH, alpha, beta, nextPlayer);
            if (heuristicVal > maxHeuristicVal) {
                maxHeuristicVal = heuristicVal;
                maxValIndex = i;
            }
        }

        return nextStates.elementAt(maxValIndex);
    }

    /**
     * @param gameState the current state we are analyzing
     * @param depth     the current depth we are in
     * @param alpha     the current best value achievable by A
     * @param beta      the current best value achievable by B
     * @param player    the current player
     * @return the minimax value of the state
     */
    private int alphaBeta(GameState gameState, int depth, int alpha, int beta, int player) {

        Vector<GameState> nextStates = new Vector<>();
        gameState.findPossibleMoves(nextStates);

        if (depth == 0 || nextStates.size() == 0) {
            return getHeuristicVal(gameState, player);
        }

        int value;

        // player = A (CELL_X)
        if (player == Constants.CELL_X) {
            value = -NaN;
            for (int i = 0; i < nextStates.size(); i++) {
                value = Math.max(value, alphaBeta(nextStates.elementAt(i),
                        depth - 1, alpha, beta, Constants.CELL_O));
                alpha = Math.max(alpha, value);
                if (beta <= alpha)
                    break; // β prune
            }
        }

        // player = B (CELL_O)
        else {
            value = NaN;
            for (int i = 0; i < nextStates.size(); i++) {
                value = Math.min(value, alphaBeta(nextStates.elementAt(i),
                        depth - 1, alpha, beta, Constants.CELL_X));
                beta = Math.min(beta, value);
                if (beta <= alpha)
                    break; // α prune
            }
        }

        return value;
    }

    private int getHeuristicVal(GameState gameState, int player) {
        int heuristicVal;
        int rowMarks = 0;
        int colMarks = 0;
        int digMarks;

        // game over
        if (gameState.isEOG()) {
            // draw
            if (gameState.getMove().isDraw()) {
                heuristicVal = 0;
            }
            // X or O wins
            else if ((gameState.getMove().isXWin() && currentPlayer == Constants.CELL_X)
                    ||
                    (gameState.getMove().isOWin() && currentPlayer == Constants.CELL_O)) {
                heuristicVal = NaN;
            }
            // X or O loses
            else {
                heuristicVal = -NaN;
            }
            return heuristicVal;
        }

        // check rows
        for (int i = 0; i < DEPTH; i++) {
            for (int j = 0; j < DEPTH; j++) {
                if (gameState.at(i, j) == player) {
                    rowMarks++;
                    break;
                }
            }
        }

        // check columns
        for (int i = 0; i < DEPTH; i++) {
            for (int j = 0; j < DEPTH; j++) {
                if (gameState.at(j, i) == player) {
                    colMarks++;
                    break;
                }
            }
        }

        // check diagonals
        int dig = 0;
        int dig_off = 0;
        for (int i = 0; i < DEPTH; i++) {
            for (int j = 0; j < DEPTH; j++) {
                if (i == j && gameState.at(i, j) == player) {
                    dig = 1;
                }
                if ((i + j == 3) && (gameState.at(i, j) == player)) {
                    dig_off = 1;
                }
            }
        }
        digMarks = dig + dig_off;

        heuristicVal = rowMarks + colMarks + digMarks;

        if (currentPlayer == player) {
            return heuristicVal;
        } else {
            return -heuristicVal;
        }
    }

}
