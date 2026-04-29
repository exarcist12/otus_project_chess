package ru.chess.gameservice.service;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChessEngine {

    private String toChesslibCoord(String coord) {
        if (coord == null || coord.length() < 2) return null;
        return coord.substring(0, 1).toUpperCase() + coord.substring(1).toLowerCase();
    }



    public boolean isValidMove(String fen, String from, String to) {
        log.info("=== VALIDATING MOVE ===");
        log.info("FEN: {}", fen);
        log.info("From: {}, To: {}", from, to);

        try {
            Board board = new Board();
            if (fen != null && !fen.isEmpty()) {
                board.loadFromFen(fen);
                log.info("Board loaded successfully");
            }

            String fromCoord = toChesslibCoord(from);
            String toCoord = toChesslibCoord(to);
            log.info("Converted: {} -> {}, {} -> {}", from, fromCoord, to, toCoord);

            Square fromSquare = Square.valueOf(fromCoord);
            Square toSquare = Square.valueOf(toCoord);
            Move move = new Move(fromSquare, toSquare);

            boolean legal = board.isMoveLegal(move, true);
            log.info("Move legal: {}", legal);

            if (!legal) {
                log.info("Available moves: {}", board.legalMoves());
            }

            return legal;
        } catch (Exception e) {
            log.error("Validation error: {}", e.getMessage(), e);
            return false;
        }
    }

    public String applyMove(String fen, String from, String to, String promotion) {
        try {
            Board board = new Board();
            if (fen != null && !fen.isEmpty()) {
                board.loadFromFen(fen);
            }

            String fromCoord = toChesslibCoord(from);
            String toCoord = toChesslibCoord(to);

            Square fromSquare = Square.valueOf(fromCoord);
            Square toSquare = Square.valueOf(toCoord);
            Move move = new Move(fromSquare, toSquare);
            board.doMove(move);
            return board.getFen();
        } catch (Exception e) {
            log.error("Error applying move: {}", e.getMessage());
            return fen; // возвращаем старую позицию
        }
    }

    public boolean isGameOver(String fen) {
        try {
            Board board = new Board();
            board.loadFromFen(fen);
            return board.isMated() || board.isStaleMate();
        } catch (Exception e) {
            log.error("Error checking game over: {}", e.getMessage());
            return false;
        }
    }

    public boolean isCheck(String fen) {
        try {
            Board board = new Board();
            board.loadFromFen(fen);
            return board.isKingAttacked();
        } catch (Exception e) {
            log.error("Error checking check: {}", e.getMessage());
            return false;
        }
    }
}