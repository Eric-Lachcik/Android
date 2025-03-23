package com.example.calculadora;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView1;
    private StringBuilder currentInput;
    private boolean hasResult = false;
    private Button buttonDividir, buttonMultiplicar, buttonResta, buttonSuma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView1 = findViewById(R.id.textView);
        currentInput = new StringBuilder();

        // Inicializamos botones de los operadores
        buttonDividir = findViewById(R.id.buttonDividir);
        buttonMultiplicar = findViewById(R.id.buttonMultiplicar);
        buttonResta = findViewById(R.id.buttonResta);
        buttonSuma = findViewById(R.id.buttonSuma);

        updateOperatorButtonsState();
    }

    // Manejamos  clic en los  botones numéricos
    public void clickNumeros(View view) {
        String number = view.getTag().toString();
        if (hasResult) {
            currentInput.setLength(0);
            hasResult = false;
        }
        currentInput.append(number);
        updateDisplay();
        updateOperatorButtonsState();
    }

    // Manejamos el  clic en los botones de los operadores
    public void clickOperadores(View view) {
        String operator = view.getTag().toString();
        if (hasResult) {
            currentInput.setLength(0);
            currentInput.append(textView1.getText().toString());
            hasResult = false;
        }
        currentInput.append(operator);
        updateDisplay();
        updateOperatorButtonsState();
    }

    // Calculamos resultado
    public void calcularOperacion(View view) {
        if (isValidExpression()) {
            calculate();
        }
    }

    // Reseteamos la  calculadora
    public void resetCalc(View view) {
        currentInput.setLength(0);
        textView1.setText("");
        hasResult = false;
        updateOperatorButtonsState();
    }

    // Actualizamos el TextView con la entrada actual
    private void updateDisplay() {
        textView1.setText(currentInput.toString());
    }

    // Validamos la  expresión matemática
    private boolean isValidExpression() {
        if (currentInput.length() == 0) {
            return false;
        }
        int operatorCount = 0;
        int operatorIndex = -1;
        for (int i = 0; i < currentInput.length(); i++) {
            char c = currentInput.charAt(i);
            if (isOperator(c)) {
                operatorCount++;
                operatorIndex = i;
            }
        }
        if (operatorCount != 1 || operatorIndex == 0 || operatorIndex == currentInput.length() - 1) {
            return false;
        }
        return true;
    }

    // Realizamos el cálculo
    private void calculate() {
        String input = currentInput.toString();
        int operatorIndex = -1;
        char operator = ' ';
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (isOperator(c)) {
                operator = c;
                operatorIndex = i;
                break;
            }
        }
        String firstStr = input.substring(0, operatorIndex);
        String secondStr = input.substring(operatorIndex + 1);
        int firstNum, secondNum;
        try {
            firstNum = Integer.parseInt(firstStr);
            secondNum = Integer.parseInt(secondStr);
        } catch (NumberFormatException e) {
            showError("Error");
            return;
        }
        int result = 0;
        switch (operator) {
            case '+':
                result = firstNum + secondNum;
                break;
            case '-':
                result = firstNum - secondNum;
                break;
            case 'X':
                result = firstNum * secondNum;
                break;
            case '/':
                if (secondNum == 0) {
                    showError("Indefinició");
                    return;
                }
                result = firstNum / secondNum;
                break;
        }
        if (result < 0) {
            showError("No es pot fer resta negativa");
            return;
        }
        currentInput.setLength(0);
        currentInput.append(result);
        hasResult = true;
        updateDisplay();
        updateOperatorButtonsState();
    }

    // Mostramos el mensaje de error
    private void showError(String message) {
        textView1.setText(message);
        currentInput.setLength(0);
        hasResult = false;
        updateOperatorButtonsState();
    }

    // Actualizamos estado de los  botones de los operadores
    private void updateOperatorButtonsState() {
        boolean enable = canAddOperator();
        buttonDividir.setEnabled(enable);
        buttonMultiplicar.setEnabled(enable);
        buttonResta.setEnabled(enable);
        buttonSuma.setEnabled(enable);
    }

    // Verificamos si se puede añadir un operador
    private boolean canAddOperator() {
        if (currentInput.length() == 0) {
            return false;
        }
        char lastChar = currentInput.charAt(currentInput.length() - 1);
        return !isOperator(lastChar);
    }

    // Verificamos si un carácter es un  operador
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == 'X' || c == '/';
    }
}