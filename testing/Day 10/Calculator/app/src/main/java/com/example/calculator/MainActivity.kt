package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView

    private var currentExpression = ""
    private var isNewCalculation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Views
        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)

        // Set Click Listeners for Digits
        setDigitListener(R.id.btn0, "0")
        setDigitListener(R.id.btn1, "1")
        setDigitListener(R.id.btn2, "2")
        setDigitListener(R.id.btn3, "3")
        setDigitListener(R.id.btn4, "4")
        setDigitListener(R.id.btn5, "5")
        setDigitListener(R.id.btn6, "6")
        setDigitListener(R.id.btn7, "7")
        setDigitListener(R.id.btn8, "8")
        setDigitListener(R.id.btn9, "9")
        setDigitListener(R.id.btnDecimal, ".")

        // Set Click Listeners for Operators
        setOperatorListener(R.id.btnAdd, "+")
        setOperatorListener(R.id.btnSubtract, "−")
        setOperatorListener(R.id.btnMultiply, "×")
        setOperatorListener(R.id.btnDivide, "÷")

        // Set Click Listeners for Special Buttons
        findViewById<Button>(R.id.btnAC).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { deleteLast() }
        findViewById<Button>(R.id.btnPercent).setOnClickListener { applyPercent() }
        findViewById<Button>(R.id.btnPlusMinus).setOnClickListener { toggleSign() }
        findViewById<Button>(R.id.btnEqual).setOnClickListener { calculate() }
    }

    /**
     * Listener for digit and decimal buttons
     */
    private fun setDigitListener(id: Int, digit: String) {
        findViewById<Button>(id).setOnClickListener {
            if (isNewCalculation) {
                currentExpression = if (digit == ".") "0." else digit
                isNewCalculation = false
            } else {
                if (digit == "." && getLastNumber().contains(".")) return@setOnClickListener
                currentExpression += digit
            }
            updateDisplay()
        }
    }

    /**
     * Listener for operator buttons (+, -, *, /)
     */
    private fun setOperatorListener(id: Int, operator: String) {
        findViewById<Button>(id).setOnClickListener {
            if (currentExpression.isEmpty()) return@setOnClickListener

            if (isOperator(currentExpression.last().toString())) {
                // Replace last operator if user changes their mind
                currentExpression = currentExpression.dropLast(1) + operator
            } else {
                currentExpression += operator
            }
            isNewCalculation = false
            updateDisplay()
        }
    }

    private fun isOperator(char: String): Boolean = char == "+" || char == "−" || char == "×" || char == "÷"

    /**
     * Extracts the last number from the expression string
     */
    private fun getLastNumber(): String {
        val parts = currentExpression.split("+", "−", "×", "÷")
        return parts.last()
    }

    private fun updateDisplay() {
        tvExpression.text = currentExpression
        val lastNum = getLastNumber()
        tvResult.text = if (lastNum.isEmpty() || lastNum == "-") "0" else formatResult(lastNum)
    }

    /**
     * AC Button logic: Clears everything
     */
    private fun clearAll() {
        currentExpression = ""
        tvExpression.text = ""
        tvResult.text = "0"
        isNewCalculation = true
    }

    /**
     * DEL Button logic: Removes last character
     */
    private fun deleteLast() {
        if (currentExpression.isNotEmpty()) {
            currentExpression = currentExpression.dropLast(1)
            updateDisplay()
        }
    }

    /**
     * Percentage Button logic: Divides current number by 100
     */
    private fun applyPercent() {
        if (currentExpression.isEmpty()) return
        val lastNum = getLastNumber()
        if (lastNum.isEmpty() || lastNum == "-") return
        
        try {
            val percentValue = lastNum.toDouble() / 100
            currentExpression = currentExpression.dropLast(lastNum.length) + formatResult(percentValue.toString())
            updateDisplay()
        } catch (_: Exception) { /* Ignore parsing errors */ }
    }

    /**
     * Toggle sign button (+/-)
     */
    private fun toggleSign() {
        if (currentExpression.isEmpty()) return
        val lastNum = getLastNumber()
        if (lastNum.isEmpty()) return

        val toggled = if (lastNum.startsWith("-")) {
            lastNum.substring(1)
        } else {
            "-$lastNum"
        }
        currentExpression = currentExpression.dropLast(lastNum.length) + toggled
        updateDisplay()
    }

    /**
     * Equal Button logic: Evaluates the expression
     */
    private fun calculate() {

        if (currentExpression.isEmpty() || isOperator(currentExpression.last().toString()))
            return

        try {

            val result = evaluate(currentExpression)
            val formattedResult = formatResult(result.toString())

            // Show only result
            tvExpression.text = ""
            tvResult.text = formattedResult

            // Store result for next calculation
            currentExpression = formattedResult

            isNewCalculation = true

        } catch (_: ArithmeticException) {

            tvExpression.text = ""
            tvResult.text = "Cannot divide by zero"

            currentExpression = ""
            isNewCalculation = true

        } catch (_: Exception) {

            tvExpression.text = ""
            tvResult.text = "Error"

            currentExpression = ""
            isNewCalculation = true
        }
    }

    /**
     * Evaluates the mathematical expression string
     */
    private fun evaluate(expression: String): Double {
        val tokens = mutableListOf<String>()
        var currentToken = StringBuilder()

        var i = 0
        while (i < expression.length) {
            val c = expression[i]
            if (c == '+' || c == '−' || c == '×' || c == '÷') {
                if (currentToken.isNotEmpty()) {
                    tokens.add(currentToken.toString())
                    currentToken = StringBuilder()
                }
                tokens.add(c.toString())
            } else {
                currentToken.append(c)
            }
            i++
        }
        if (currentToken.isNotEmpty()) tokens.add(currentToken.toString())

        if (tokens.isEmpty()) return 0.0

        val values = mutableListOf<Double>()
        val ops = mutableListOf<String>()

        var idx = 0
        while (idx < tokens.size) {
            when (val token = tokens[idx]) {
                "×", "÷" -> {
                    val left = values.removeAt(values.size - 1)
                    val right = if (idx + 1 < tokens.size) tokens[idx + 1].toDouble() else 0.0
                    val res = if (token == "×") left * right else {
                        if (right == 0.0) throw ArithmeticException("Divide by zero")
                        left / right
                    }
                    values.add(res)
                    idx += 2
                }
                "+", "−" -> {
                    ops.add(token)
                    idx++
                }
                else -> {
                    values.add(token.toDouble())
                    idx++
                }
            }
        }

        var result = if (values.isNotEmpty()) values[0] else 0.0
        for (j in ops.indices) {
            val op = ops[j]
            val valNext = if (j + 1 < values.size) values[j + 1] else 0.0
            if (op == "+") result += valNext else result -= valNext
        }

        return result
    }

    /**
     * Formats the result string for display
     */
    private fun formatResult(result: String): String {
        return try {
            val number = result.toDouble()
            val df = DecimalFormat("###.##########")
            df.format(number)
        } catch (_: Exception) {
            result
        }
    }
}
