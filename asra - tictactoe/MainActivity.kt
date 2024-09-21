package com.example.temp_tictactoe

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.temp_tictactoe.databinding.ActivityMainBinding
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var gameStarted = false //check if game started

    // will keep track of current player
    private var currentPlayer = 1
    private var currentSymbol = "X"
    private var playersColor = R.color.orange

    // initialize the game board
    private val gameboard = Array(3) {Array(3) {""}}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize variable for view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun startBtnHandler(view: View){
        if(!gameStarted){
            gameStarted = true
            binding.updateTextview.text = getString(R.string.playerStr, currentPlayer)
            binding.updateTextview.setTextColor(getColor(R.color.orange))
        }
    }

    fun chooseSquareBtn(view: View){
        // check if the game began
        if(!gameStarted){
            return // returns if game hasn't started
        }

        // initialize position variable
        var position = Pair(-1, -1)

        // will determine which button was pressed and its position
        when (view.id) {
            R.id.gridBtn_0_0 -> {
                position = Pair(0,0)
            }
            R.id.gridBtn_0_1 -> {
                position = Pair(0,1)
            }
            R.id.gridBtn_0_2 -> {
                position = Pair(0,2)
            }
            R.id.gridBtn_1_0 -> {
                position = Pair(1,0)
            }
            R.id.gridBtn_1_1 -> {
                position = Pair(1,1)
            }
            R.id.gridBtn_1_2 -> {
                position = Pair(1,2)
            }
            R.id.gridBtn_2_0 -> {
                position = Pair(2,0)
            }
            R.id.gridBtn_2_1 -> {
                position = Pair(2,1)
            }
            R.id.gridBtn_2_2 -> {
                position = Pair(2,2)
            }
        }

        // determine row and column
        val (r,c) =  position

        // check if space was already filled
        if(gameboard[r][c] != ""){
            return
        }
        else{
            // update the game board array
            gameboard[r][c] = currentSymbol
        }

        // map of buttons, used to update the game board
        val buttonMap:MutableMap<String,Button> = mutableMapOf(
        "00" to binding.gridBtn00,
        "01" to binding.gridBtn01,
        "02" to binding.gridBtn02,
        "10" to binding.gridBtn10,
        "11" to binding.gridBtn11,
        "12" to binding.gridBtn12,
        "20" to binding.gridBtn20,
        "21" to binding.gridBtn21,
        "22" to binding.gridBtn22
        )

        // use map to determine the button that was clicked
        val pressedButton = buttonMap["" + r + c]

        // will update the game board
        pressedButton?.text = currentSymbol
        pressedButton?.setTextColor(getColor(playersColor))

        // will check if current player won before switching to next player
        val playerWon = checkForWin()
        if(playerWon != 0){
            // check if there was a draw
            if(playerWon == 1){
                // will alert user of winner
                binding.updateTextview.text = getString(R.string.winnerStr, currentPlayer)
            }
            else{
                // will alert user of a draw
                binding.updateTextview.text = getString(R.string.drawStr)
                binding.updateTextview.setTextColor(getColor(R.color.white))
            }

            // will stop game and display restart button
            gameStarted = false
            binding.restartBtn.visibility = View.VISIBLE
            return
        }

        // will update to the next player
        if(currentPlayer == 1){
            currentPlayer = 2
            currentSymbol = "O"
            playersColor = R.color.purple
        }
        else{
            currentPlayer = 1
            currentSymbol = "X"
            playersColor = R.color.orange
        }

        binding.updateTextview.text = getString(R.string.playerStr, currentPlayer)
        binding.updateTextview.setTextColor(getColor(playersColor))
    }

    // will check if one of the players won
    fun checkForWin(): Int {
        // check row and columns
        for(i in gameboard.indices){
            // checks rows
            var pos = gameboard[i][0]
            if(pos != "" && pos == gameboard[i][1] && pos == gameboard[i][2]){
                return 1
            }

            // checks columns
            pos = gameboard[0][i]
            if(pos != "" && pos == gameboard[1][i] && pos == gameboard[2][i]){
                return 1
            }
        }

        // will diagonals
        var pos = gameboard[0][0]
        if(pos != "" && pos == gameboard[1][1] && pos == gameboard[2][2]){
            return 1
        }

        pos = gameboard[0][2]
        if(pos != "" && pos == gameboard[1][1] && pos == gameboard[2][0]){
            return 1
        }

        // will check if their is a draw
        if(gameboard.all { row -> row.all {it != ""}}){
            return -1
        }

        return 0
    }

    // will restart the activity
    fun restartBtnHandler(view: View){
        val intent = intent
        finish()
        startActivity(intent)
    }
}