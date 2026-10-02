package dev.shimmer.tictactoe

import android.app.Activity
import android.widget.Button
import android.widget.TextView
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.graphics.Color

class MainActivity : Activity() {

    private val btnIds = intArrayOf(
	R.id.btn1, R.id.btn2, R.id.btn3,
	R.id.btn4, R.id.btn5, R.id.btn6,
	R.id.btn7, R.id.btn8, R.id.btn9,
    )

    private lateinit var txt_score: TextView
    private lateinit var txt_status: TextView
    private lateinit var btn_reset: Button
    private lateinit var btns: List<Button>
    
    private val board = charArrayOf(' ',' ',' ',' ',' ',' ',' ',' ',' ')

    val delay_sec = 2
    var currentPlayer = 'O'
    var player_scores = mutableMapOf('X' to 0, 'O' to 0)
    var can_place = true
    var pattern = intArrayOf(0,0,0)

    override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	setContentView(R.layout.activity_main)

	txt_score = findViewById(R.id.txt_score)
	txt_status = findViewById(R.id.txt_status)
	btn_reset = findViewById(R.id.btn_reset)
	btns = btnIds.map { findViewById<Button>(it) }

	btn_reset.setOnClickListener {gameReset()}
	for (item in btns) { item.setOnClickListener {onBtnClick(item)} }
    }
    private fun onBtnClick(btn: Button) {
        val index = btnIds.indexOf(btn.id)
	if (board[index] != ' ' || can_place == false) return

        board[index] = currentPlayer
	btn.text = currentPlayer.toString()
	var plr_color = if (currentPlayer == 'O') R.color.player_O else R.color.player_X
	btn.setTextColor(getColor(plr_color))

if (board[0] == currentPlayer && board[1] == currentPlayer && board[2] == currentPlayer) {pattern = intArrayOf(0,1,2)}
else if (board[3] == currentPlayer && board[4] == currentPlayer && board[5] == currentPlayer) {pattern = intArrayOf(3,4,5)}
else if (board[6] == currentPlayer && board[7] == currentPlayer && board[8] == currentPlayer) {pattern = intArrayOf(6,7,8)}
else if (board[0] == currentPlayer && board[3] == currentPlayer && board[6] == currentPlayer) {pattern = intArrayOf(0,3,6)}
else if (board[1] == currentPlayer && board[4] == currentPlayer && board[7] == currentPlayer) {pattern = intArrayOf(1,4,7)}
else if (board[2] == currentPlayer && board[5] == currentPlayer && board[8] == currentPlayer) {pattern = intArrayOf(2,5,8)}
else if (board[0] == currentPlayer && board[4] == currentPlayer && board[8] == currentPlayer) {pattern = intArrayOf(0,4,8)}
else if (board[2] == currentPlayer && board[4] == currentPlayer && board[6] == currentPlayer) {pattern = intArrayOf(2,4,6)}
else {
    currentPlayer = if (currentPlayer == 'O') 'X' else 'O'
    txt_status.text = if (currentPlayer == 'O') "O" else "X"
    plr_color = if (currentPlayer == 'O') R.color.player_O else R.color.player_X
    txt_status.setTextColor(getColor(plr_color))
}
	if (!pattern.contentEquals(intArrayOf(0,0,0))) {resultWon(currentPlayer)}
	else if (!board.contains(' ')) resultTie()
    }

    private fun gameReset() {
	currentPlayer = 'O'
	for (item in btns) item.text = ""
	board.fill(' ')
	btns[pattern[0]].setBackgroundColor(getColor(R.color.cell_normal))
	btns[pattern[1]].setBackgroundColor(getColor(R.color.cell_normal))
        btns[pattern[2]].setBackgroundColor(getColor(R.color.cell_normal))
        txt_status.setTextColor(getColor(R.color.player_O))
        txt_score.setTextColor(getColor(R.color.score_normal))
        pattern = intArrayOf(0,0,0)
        txt_status.text = "O"
        can_place = true
    }

    private fun resultWon(winner: Char) {
        can_place = false
        val currentColor = if (winner == 'O') R.color.player_O else R.color.player_X
        btns[pattern[0]].setBackgroundColor(getColor(currentColor))
        btns[pattern[1]].setBackgroundColor(getColor(currentColor))
        btns[pattern[2]].setBackgroundColor(getColor(currentColor))
	txt_status.setTextColor(getColor(currentColor))
	txt_score.setTextColor(getColor(currentColor))
        txt_status.text = "$winner Won!"
        player_scores[winner] = player_scores[winner]!! + 1
	txt_score.text = "${player_scores['O'] ?: 0} - ${player_scores['X'] ?: 0}"
        Handler(Looper.getMainLooper()).postDelayed({
    	gameReset()
        }, delay_sec * 1000L)
    }

    private fun resultTie() {
        can_place = false
        txt_status.text = "tie"
        Handler(Looper.getMainLooper()).postDelayed({
	   gameReset()
        }, delay_sec * 1000L)
    }

}
