import java.util.*;

public class Player {
    /**
     * Performs a move
     *
     * @param pState
     *            the current state of the board
     * @param pDue
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */
   // HashMap<PlayerState, Integer> map = new HashMap<PlayerState, Integer>();
    private int currentPlayer;




    private static final int[] whiteValues  = {
            10, 10, 9, 10,
            8, 8, 7, 7,
            6, 6, 5, 6,
            6, 6, 5, 4,
            5, 4, 5, 6,
            4, 3, 5, 3,
            2, 3, 2, 4,
            3, 2, 3, 4
    };
    private static final int[] redValues =  {
            4, 3, 2, 3,
            4, 2, 3, 2,
            3, 5, 3, 4,
            6, 5, 4, 5,
            4, 5, 6, 6,
            6, 5, 6, 6,
            7, 7, 8, 8,
            10, 9, 10, 10
    };

    private int getValue(GameState state)
    {
//        if(state.isRedWin())
//            return -100000;
//        else if(state.isWhiteWin())
//            return 100000;
//        else if(state.isDraw())
//            return 0;
//        else
//        {
            int whiteScore = 0;
            int redScore = 0;
            //calculate white checkers points
            for(int i = 0; i < GameState.NUMBER_OF_SQUARES; i++)
            {
               int piece = state.get(i);

                if(piece != Constants.CELL_EMPTY)
                {

                    if(piece == 2) //white and not king
                     {
                         whiteScore += whiteValues[i];
                     }
                    else if(piece == 6)
                    {
                        whiteScore += (whiteValues[i]+3);
                    }
                    else if(piece == 1)
                        redScore += redValues[i];
                    else
                        redScore += (redValues[i]+3);

                }

            }
            if(currentPlayer == 1)
            {
                return redScore - whiteScore;
            }
            else
            {
                return whiteScore - redScore;
            }

        //}
    }

    public int[] alphaBeta(GameState state, int depth, int alfa, int beta, int player)
    {
        //printCurrentBoard(getBoardStates(state));
        Vector<GameState> nextStates = new Vector<GameState>();
        state.findPossibleMoves(nextStates);
        if(nextStates.isEmpty())
        {
            if(player == currentPlayer)
            {
                int result[] = {1000000,0};
                return result;
            }

            else
            {
                int result[] = {-1000000,0};
                return result;
            }

        }

        if(depth == 0)
        {
            int val = getValue(state);
           // PlayerState ps = new PlayerState(state, val);
          //  map.put(ps, val);
            int result[] = {val, 0};
            return result;
            //System.err.println("Val = " + val);
        }


        if(player == currentPlayer)
        {

            int[] bestResult = {-100000, 0};
            for(int i = 0; i < nextStates.size(); i++)
            {
              //  PlayerState playerState = new PlayerState(next, player);
             //   if(map.containsKey(playerState))
             //       return map.get(playerState);
                int tmp = alphaBeta(nextStates.elementAt(i), depth-1, alfa, beta, nextStates.elementAt(i).getNextPlayer())[0];

                if(tmp > bestResult[0])
                {
                    bestResult[0] = tmp;
                    bestResult[1] = i;
                    if(alfa < tmp)
                        alfa = tmp;
                }

              //  map.put(playerState, val);

                if(beta <= alfa)
                    break;
            }
            return  bestResult;
        }
        else
        {

            int[] minResult = {100000, 0};
            for(int i = 0; i < nextStates.size(); i++)
            {
              //  PlayerState playerState = new PlayerState(next, player);
//                if(map.containsKey(playerState))
//                    return map.get(playerState);
                int tmp = alphaBeta(nextStates.elementAt(i), depth-1, alfa, beta, nextStates.elementAt(i).getNextPlayer())[0];
                if(tmp < minResult[0])
                {
                    minResult[0] = tmp;
                    minResult[1] = i;
                    if(tmp < beta)
                        beta = tmp;
                }

              //  map.put(playerState, val);

                if(beta <= alfa)
                    break;
            }
            return minResult;

        }

    }

    public GameState play(final GameState pState, final Deadline pDue) {

        currentPlayer = pState.getNextPlayer();


        Vector<GameState> lNextStates = new Vector<GameState>();
        pState.findPossibleMoves(lNextStates);

//        if (lNextStates.size() == 0) {
//            // Must play "pass" move if there are no other moves possible.
//            return new GameState(pState, new Move());
//        }
//        if(lNextStates.size() == 1)
//            return lNextStates.elementAt(0);



        //boolean flag = true;
        int index = 0;
        int mdepth = 0;

        while(pDue.timeUntil() > 750000000)
        {
            //System.err.println("depth = " + mdepth);
            if(mdepth > 11)
                break;
                //mdepth = 11;
            int[] result = alphaBeta(pState, mdepth, Integer.MIN_VALUE, Integer.MAX_VALUE, currentPlayer);

            index = result[1];
            if(result[0] == 1000000 || result[0] == -1000000) //如果游戏能判断输赢，返回。
                return lNextStates.elementAt(index);
            mdepth++;

        }

        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */
        //Random random = new Random();
        return lNextStates.elementAt(index);
    }
}
