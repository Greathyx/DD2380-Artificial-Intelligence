#include "player.hpp"
#include <cstdlib>

#define NaN 0x7fffffff
#define DEPTH 2

namespace TICTACTOE3D {

    GameState Player::play(const GameState &pState, const Deadline &pDue) {


        vector<GameState> lNextStates;
        pState.findPossibleMoves(lNextStates);

        if (lNextStates.size() == 0) {
            return GameState(pState, Move());
        }

        /*
        * Here you should write your clever algorithms to get the best next move, ie the best
        * next state. This skeleton returns a random move instead.
        */

//      return lNextStates[rand() % lNextStates.size()];

        currentPlayer = pState.getNextPlayer();
        uint8_t nextPlayer;
        if (currentPlayer == CELL_X) {
            nextPlayer = CELL_O;
        } else {
            nextPlayer = CELL_X;
        }

        int maxHeuristicVal = -NaN;
        int maxValIndex = 0;
        int heuristicVal;
        int alpha = -NaN;
        int beta = NaN;

        for (int i = 0; i < lNextStates.size(); i++) {
            heuristicVal = alphaBeta(lNextStates[i], nextPlayer, DEPTH, alpha, beta);
            if (heuristicVal > maxHeuristicVal) {
                maxHeuristicVal = heuristicVal;
                maxValIndex = i;
            }
        }
        return lNextStates[maxValIndex];
    }


    int Player::getHeuristicVal(const GameState &pState, const uint8_t player) {
        int heuristicVal = 0;

        uint8_t nextPlayer;
        if (player == CELL_X) {
            nextPlayer = CELL_O;
        } else {
            nextPlayer = CELL_X;
        }
        // game over
        if (pState.isEOG()) {
            // draw
            if (pState.isDraw()) {
                heuristicVal = 0;
            }
                // X or O wins
            else if ((pState.isXWin() && currentPlayer == CELL_X) || (pState.isOWin() && currentPlayer == CELL_O)) {
                heuristicVal = NaN;
            }
                // X or O loses
            else if ((pState.isOWin() && currentPlayer == CELL_X) || (pState.isXWin() && currentPlayer == CELL_O)) {
                heuristicVal = -NaN;
            }
            return heuristicVal;
        }

        int currentPlayerMarkers = 0;
        int nextPlayerMarkers = 0;

        for (int i = 0; i < 76; i++) {
            currentPlayerMarkers = 0;
            nextPlayerMarkers = 0;
            for (int j = 0; j < 4; j++) {
                if (pState.at(winCases[i][j]) == player) {
                    currentPlayerMarkers++;
                }
                if (pState.at(winCases[i][j]) == nextPlayer) {
                    nextPlayerMarkers++;
                }
            }
            if (currentPlayerMarkers == 3) {
                heuristicVal++;
            }
            if (nextPlayerMarkers == 3) {
                heuristicVal--;
            }
        }

        if (player == currentPlayer) {
            return heuristicVal;
        } else {
            return -heuristicVal;
        }

    }


/**
 * @param gameState the current state we are analyzing
 * @param depth     the current depth we are in
 * @param alpha     the current best value achievable by A
 * @param beta      the current best value achievable by B
 * @param player    the current player
 * @return the minimax value of the state
 */
    int Player::alphaBeta(const GameState &pState, const uint8_t player, int depth, int alpha, int beta) {

        if (depth == 0) {
            return getHeuristicVal(pState, player);
        }

        vector<GameState> lNextStates;
        pState.findPossibleMoves(lNextStates);

        if (lNextStates.size() == 0) {
            return getHeuristicVal(pState, player);
        }

        int value;

        if (player == CELL_X) {
            value = -NaN;
            for (int i = 0; i < lNextStates.size(); i++) {
                value = max(value, alphaBeta(lNextStates[i], CELL_O, depth - 1, alpha, beta));
                alpha = max(alpha, value);
                if (beta <= alpha) {
                    break;
                }
            }
        } else {
            value = NaN;
            for (int i = 0; i < lNextStates.size(); i++) {
                value = min(value, alphaBeta(lNextStates[i], CELL_X, depth - 1, alpha, beta));
                beta = min(beta, value);
                if (beta <= alpha) {
                    break;
                }
            }
        }

        return value;
    }

/*namespace TICTACTOE3D*/ }